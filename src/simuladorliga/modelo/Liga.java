package simuladorliga.modelo;

import java.io.File;
import java.util.*;

/**
 * Representa una liga de fútbol compuesta por equipos, partidos y un calendario.
 * Permite generar el calendario (ida y vuelta), actualizar resultados y clasificaciones,
 * y manejar estadísticas y persistencia básica.
 */
public class Liga {
    private String nombre;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private List<List<Partido>> calendario; 
    private boolean ida_vuelta;

    /**
     * Crea una nueva liga con su nombre y configuración de ida/vuelta.
     *
     * @param nombre nombre de la liga
     * @param ida_vuelta true si hay ida y vuelta; false si solo ida
     */
    public Liga(String nombre, boolean ida_vuelta) {
        this.nombre = nombre;
        this.ida_vuelta = ida_vuelta;
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.calendario = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    /**
     * Calcula el número total de jornadas en función del número de equipos
     * y de si la liga es solo ida o ida y vuelta.
     *
     * @return número de jornadas
     */
    public int getJornadas() {
        int equipos = this.equipos.size(); 
        if (equipos < 2) return 0; // no hay liga si solo hay 1 equipo
        return ida_vuelta ? (equipos - 1) * 2 : (equipos - 1);
    }

    public boolean isIda_vuelta() {
        return ida_vuelta;
    }

    public void setIda_vuelta(boolean ida_vuelta) {
        this.ida_vuelta = ida_vuelta;
    }

    public List<List<Partido>> getCalendario() {
        return calendario;
    }

    /**
     * Añade un equipo a la lista de participantes.
     *
     * @param equipo equipo a agregar
     */
    public void agregarEquipo(Equipo equipo) {
        this.equipos.add(equipo);
    }

    /**
     * Ordena los equipos según puntos y diferencia de goles (descendente).
     */
    public void calcularClasificacion() {
        equipos.sort(
            Comparator.comparing(Equipo::getPuntos)
                      .thenComparing(Equipo::getDiferenciaGoles)
                      .reversed()
        );
    }
    
    /**
     * Devuelve un String con la clasificación formateada como tabla.
     *
     * @return String con las estadísticas de todos los equipos ordenados
     */
    public String mostrarEstadisticas() {
        this.calcularClasificacion();
        int posicion = 1;
        StringBuilder clasificacion = new StringBuilder();
        clasificacion.append(String.format("%-4s %-20s %-5s %-5s %-5s %-5s\n", "POS", "EQUIPO", "GF", "GC", "DIF", "PTS"));

        for (Equipo equipo : equipos) {
            clasificacion.append(String.format(
                "%-4d %-20s %-5d %-5d %-5d %-5d\n",
                posicion,
                equipo.getNombre(),
                equipo.getGolesFavor(),
                equipo.getGolesContra(),
                equipo.getDiferenciaGoles(),
                equipo.getPuntos()
            ));
            posicion++;
        }

        return clasificacion.toString();
    }
    
    /**
     * Genera el calendario completo de partidos (ida y vuelta) con rotación estilo round-robin.
     * Los partidos se agrupan por jornada y se guardan tanto en calendario como en lista plana de partidos.
     */
    public void generarCalendarioIdaVuelta() {
        List<Equipo> equiposOriginal = new ArrayList<>(equipos);

        if (equiposOriginal.size() % 2 != 0) {
            equiposOriginal.add(new Equipo("DESCANSA"));
        }

        int numEquipos = equiposOriginal.size();
        int numJornadas = numEquipos - 1;
        int partidosPorJornada = numEquipos / 2;

        List<List<Partido>> calendarioGenerado = new ArrayList<>();

        // IDA
        for (int jornada = 0; jornada < numJornadas; jornada++) {
            List<Partido> partidosJornada = new ArrayList<>();
            for (int i = 0; i < partidosPorJornada; i++) {
                Equipo local = equiposOriginal.get(i);
                Equipo visitante = equiposOriginal.get(numEquipos - 1 - i);

                if (!local.getNombre().equals("DESCANSA") && !visitante.getNombre().equals("DESCANSA")) {
                    partidosJornada.add(new Partido(local, visitante, 0, 0, jornada +1));
                }
            }
            calendarioGenerado.add(partidosJornada);

            // Rotación estilo Round-Robin
            List<Equipo> rotados = new ArrayList<>();
            rotados.add(equiposOriginal.get(0)); // fijo
            rotados.add(equiposOriginal.get(numEquipos - 1));
            for (int i = 1; i < numEquipos - 1; i++) {
                rotados.add(equiposOriginal.get(i));
            }
            equiposOriginal = rotados;
        }

        // VUELTA (local y visitante invertidos)
        for (int j = 0; j < numJornadas; j++) {
            List<Partido> jornadaVuelta = new ArrayList<>();
            for (Partido partidoIda : calendarioGenerado.get(j)) {
                jornadaVuelta.add(new Partido(
                    partidoIda.getEquipoVisitante(),
                    partidoIda.getEquipoLocal(),
                    0,
                    0,
                    numJornadas + j + 1
                ));
            }
            calendarioGenerado.add(jornadaVuelta);
        }

        this.calendario = calendarioGenerado; // Guardamos jornadas completas
        this.partidos = calendarioGenerado.stream().flatMap(List::stream).toList(); // Todos los partidos juntos
    }
    
    /**
     * Reinicia las estadísticas de todos los equipos (puntos y goles).
     */
    public void reiniciarEstadisticas(){
        for (Equipo equipo : equipos) {
            equipo.setPuntos(0);
            equipo.setGolesContra(0);
            equipo.setGolesFavor(0);
        }
    }
    
    /**
     * Actualiza la clasificación tomando como referencia los partidos ya simulados.
     * Llama internamente a `partido.actualizarPuntos()`.
     */
    public void actualizarClasificacionDesdeResultados() {
        // Reiniciar estadísticas
        this.reiniciarEstadisticas();

        for (Partido partido : partidos) {
            if (partido.isSimulado()) {
                partido.actualizarPuntos(); // Ya suma goles y puntos internamente
            }
        }

        this.calcularClasificacion(); // Ordenar la tabla
    }
    
    /**
     * Asigna un presupuesto inicial a todos los equipos de la liga.
     *
     * @param presupuesto cantidad a asignar
     */
    public void asignarPresupuestoinicial(double presupuesto){
        for (Equipo equipo : equipos) {
            equipo.setPresupuesto(presupuesto);
        }
    }
    
    /**
     * Crea la carpeta en disco correspondiente a esta liga en la ruta /ligas/{nombre}.
     * Se usa para exportación u organización de datos persistentes.
     */
    public void crearRuta() {
        // Crear carpeta base si no existe
        File base = new File("ligas");
        if (!base.exists()) {
            base.mkdir();
        }

        // Ruta principal de la liga
        String rutaCarpeta = "ligas/" + this.getNombre();
        File carpetaLiga = new File(rutaCarpeta);

        if (carpetaLiga.mkdirs()) {

            System.out.println("Carpetas creadas correctamente en: " + carpetaLiga.getAbsolutePath());
        } else {
            System.out.println("No se pudo crear la carpeta o ya existe.");
        }
    }
    
    /**
     * Devuelve una lista con los nombres de todos los equipos de la liga.
     *
     * @return lista de nombres de equipo
     */
    public List<String> obtenerNombreEquipos(){
        List<String> nombresEquipos = new ArrayList<>();
        for (Equipo equipo : equipos) {
            nombresEquipos.add(equipo.getNombre());
        }
        return nombresEquipos;
    }
    
    /**
     * Busca un equipo dentro de la liga por su nombre (sin distinguir mayúsculas/minúsculas).
     *
     * @param nombre nombre del equipo
     * @return el objeto Equipo, o null si no se encuentra
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
        for (Equipo eq : equipos) {
            if (eq.getNombre().equalsIgnoreCase(nombre)) {
                return eq;
            }
        }
        return null; // o puedes lanzar una excepción si prefieres
    }
    
    /**
     * Devuelve la lista de partidos correspondiente a una jornada concreta.
     *
     * @param numeroJornada número de jornada (comienza en 1)
     * @return lista de partidos de esa jornada, o vacía si el número no es válido
     */
    public List<Partido> getPartidosDeJornada(int numeroJornada) {
        if (numeroJornada < 1 || numeroJornada > calendario.size()) {
            return new ArrayList<>(); // o lanzar excepción
        }
        return calendario.get(numeroJornada - 1);
    }
    
    /**
     * Método para resetear las estadísticas completas de la liga.
     */
    public void resetearLiga(){
        
        //Recorre los equipos de la liga y resetea sus estadísticas
        for (Equipo equipo : equipos) {
            equipo.resetearEstadisticas();
        }
        
        // Recorre todas las jornadas y sus partidos para resetearlos
        if (calendario != null) {
            for (List<Partido> jornada : calendario) {
                for (Partido partido : jornada) {
                    partido.resetearPartido();
                }
            }
        }
    }

}
