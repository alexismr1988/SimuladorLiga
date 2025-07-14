package simuladorliga.modelo;

import java.util.*;

/**
 *
 * @author PC
 */
public class Liga {
    private String nombre;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private List<List<Partido>> calendario; // NUEVO
    private int jornadas;
    private boolean ida_vuelta;

    public Liga(String nombre, int jornadas, boolean ida_vuelta) {
        this.nombre = nombre;
        this.jornadas = jornadas;
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

    public int getJornadas() {
        return jornadas;
    }

    public void setJornadas(int jornadas) {
        this.jornadas = jornadas;
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

    public void agregarEquipo(Equipo equipo) {
        this.equipos.add(equipo);
    }

    public void calcularClasificacion() {
        equipos.sort(
            Comparator.comparing(Equipo::getPuntos)
                      .thenComparing(Equipo::getDiferenciaGoles)
                      .reversed()
        );
    }

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
                    partidosJornada.add(new Partido(local, visitante, 0, 0));
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
                    0
                ));
            }
            calendarioGenerado.add(jornadaVuelta);
        }

        this.calendario = calendarioGenerado; // Guardamos jornadas completas
        this.partidos = calendarioGenerado.stream().flatMap(List::stream).toList(); // Todos los partidos juntos
    }
    public void reiniciarEstadisticas(){
        for (Equipo equipo : equipos) {
            equipo.setPuntos(0);
            equipo.setGolesContra(0);
            equipo.setGolesFavor(0);
        }
    }
    
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

}
