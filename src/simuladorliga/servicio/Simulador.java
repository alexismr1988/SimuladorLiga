package simuladorliga.servicio;

import java.util.ArrayList;
import java.util.List;
import simuladorliga.modelo.*;

/**
 * Clase encargada de la lógica de simulación de partidos y jornadas en una liga.
 * Aplica probabilidades, medias de alineaciones, estilos tácticos y ventaja local.
 */
public class Simulador {

    private Liga liga;

    /**
     * Constructor del simulador que asocia una liga.
     * 
     * @param liga Liga sobre la que se aplicarán las simulaciones
     */
    public Simulador(Liga liga) {
        this.liga = liga;
    }

    /**
    * Simula un partido entre dos equipos, generando goles en función de probabilidades,
    * medias de jugadores, alineaciones y estilo del entrenador.
    * 
    * @param partido Partido a simular (modifica sus valores directamente)
    */
    public void simularPartido(Partido partido) {
        Equipo local = partido.getEquipoLocal();
        Equipo visitante = partido.getEquipoVisitante();
        local.setAlineacion(local.generarAlineacionAuto());
        visitante.setAlineacion(visitante.generarAlineacionAuto());       
        double ventajaLocal = 1.10;      // 10% de ventaja para el equipo local
        double ProbabilidadGolLocal = 0.18;   // Probabilidad de gol por ocasión
        double ProbabilidadGolVisitante = 0.15;   // Probabilidad de gol por ocasión
        int ocasionesLocal = 12; //Ocasiones totales del partido
        int ocasionesVisitante = 12; //Ocasiones totales del partido
        Entrenador entrenadorLocal = local.getEntrenador();
        Entrenador entrenadorVisitante = visitante.getEntrenador();
        String formacionLocal = local.getAlineacion().contarLineas();
        String formacionVisitante = visitante.getAlineacion().contarLineas();
        
        //Calcular el número de posiciones de cada Alineación para aplicar probabilidades
        if(local.getAlineacion().getDefensas()<4) ProbabilidadGolVisitante *= 1.15;
        if(local.getAlineacion().getDefensas()>4) ProbabilidadGolVisitante *= 0.85;
        if(local.getAlineacion().getMedios()==3) ocasionesLocal-=2;
        if(local.getAlineacion().getMedios()==5) ocasionesLocal+=4;
        if(local.getAlineacion().getMedios()==6) ocasionesLocal+=6;
        if(local.getAlineacion().getDelanteros()==1) ProbabilidadGolLocal *= 0.85;
        if(local.getAlineacion().getDelanteros()==3) ProbabilidadGolLocal *= 1.10;
        
        //Calcular el número de posiciones de cada Alineación para aplicar probabilidades
        if(visitante.getAlineacion().getDefensas()<4) ProbabilidadGolLocal *= 1.15;
        if(visitante.getAlineacion().getDefensas()>4) ProbabilidadGolLocal *= 0.85;
        if(visitante.getAlineacion().getMedios()==3) ocasionesVisitante-=2;
        if(visitante.getAlineacion().getMedios()==5) ocasionesVisitante+=4;
        if(visitante.getAlineacion().getMedios()==6) ocasionesVisitante+=6;
        if(visitante.getAlineacion().getDelanteros()==1) ProbabilidadGolLocal *= 0.85;
        if(visitante.getAlineacion().getDelanteros()==3) ProbabilidadGolLocal *= 1.10;

        // Medias del equipo local
        int mediaPorteroLocal = local.getAlineacion().mediaPorPosiciones(Posicion.PORTERO);
        int mediaDefensasLocal = local.getAlineacion().mediaPorPosiciones(Posicion.DEFENSA, Posicion.LATERAL);
        int mediaMediosLocal = local.getAlineacion().mediaPorPosiciones(Posicion.MCD, Posicion.MEDIOCENTRO, Posicion.MCO);
        int mediaDelanterosLocal = local.getAlineacion().mediaPorPosiciones(Posicion.DC, Posicion.SD);

        // Medias del equipo visitante
        int mediaPorteroVisitante = visitante.getAlineacion().mediaPorPosiciones(Posicion.PORTERO);
        int mediaDefensasVisitante = visitante.getAlineacion().mediaPorPosiciones(Posicion.DEFENSA, Posicion.LATERAL);
        int mediaMediosVisitante = visitante.getAlineacion().mediaPorPosiciones(Posicion.MCD, Posicion.MEDIOCENTRO, Posicion.MCO);
        int mediaDelanterosVisitante = visitante.getAlineacion().mediaPorPosiciones(Posicion.DC, Posicion.SD);


        // Reducir/Aumentar parámetros en base a la media
        double reduccionLocal = (mediaPorteroLocal / 100.0) * 0.05 + 0.03;
        int penalizacionOcasionesVisitante = mediaDefensasLocal / 20;  // cada 15 puntos, -1 ocasión
        int incrementoOcasionesLocal = mediaMediosLocal /20; // cada 15 puntos, -1 ocasión
        double porcentajeExtraGolLocal = (mediaDelanterosLocal / 100.0) * 0.09 + 0.01;
        
        double reduccionVisitante = (mediaPorteroVisitante / 100.0) * 0.05 + 0.03;
        int penalizacionOcasionesLocal = mediaDefensasVisitante/ 20;  // cada 15 puntos, -1 ocasión
        int incrementoOcasionesVisitante = mediaMediosVisitante/20; // cada 15 puntos, -1 ocasión
        double porcentajeExtraGolVisitante = (mediaDelanterosVisitante / 100.0) * 0.09 + 0.01;
        
        ProbabilidadGolVisitante *= 1 - reduccionLocal;
        ocasionesVisitante -= penalizacionOcasionesVisitante;
        ocasionesLocal += incrementoOcasionesLocal;
        ProbabilidadGolLocal *= 1 + porcentajeExtraGolLocal;
        
        ProbabilidadGolLocal *= 1 - reduccionVisitante;
        ocasionesLocal -= penalizacionOcasionesLocal;
        ocasionesVisitante += incrementoOcasionesVisitante;
        ProbabilidadGolVisitante *= 1 + porcentajeExtraGolVisitante;
        
        if (entrenadorLocal != null) {
            EstiloEntrenador estiloLocal = entrenadorLocal.getEstilo();
            if (estiloLocal.esCompatible(formacionLocal)) {
                switch (estiloLocal) {
                    case DEFENSIVO -> ProbabilidadGolVisitante *= 0.95;
                    case OFENSIVO, POSESION, CONTRAATAQUE -> ProbabilidadGolLocal *= 1.05;
                }
            }
        }

        if (entrenadorVisitante != null) {
            EstiloEntrenador estiloVisitante = entrenadorVisitante.getEstilo();
            if (estiloVisitante.esCompatible(formacionVisitante)) {
                switch (estiloVisitante) {
                    case DEFENSIVO -> ProbabilidadGolLocal *= 0.95;
                    case OFENSIVO, POSESION, CONTRAATAQUE -> ProbabilidadGolVisitante *= 1.05;
                }
            }
        }

        
        // Simular goles
        int golesLocal = 0;
        for (int i = 0; i < ocasionesLocal; i++) {
            if (Math.random() < ProbabilidadGolLocal) golesLocal++;
        }

        int golesVisitante = 0;
        for (int i = 0; i < ocasionesVisitante; i++) {
            if (Math.random() < ProbabilidadGolVisitante) golesVisitante++;
        }

        // Registrar resultado en el partido
        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setSimulado(true);

        // Actualizar estadísticas de los equipos involucrados
        partido.actualizarPuntos();
    }

    /**
    * Simula todos los partidos de una jornada específica y devuelve un resumen formateado.
    * 
    * @param numeroJornada Número de jornada a simular (1-indexado)
    * @return Resumen de resultados simulados como cadena de texto
    * @throws IllegalArgumentException si el número de jornada no existe en el calendario
    */
    public String simularJornada(int numeroJornada) {
        int jornada = numeroJornada - 1;
        StringBuilder resultadoJornada = new StringBuilder("Jornada " + numeroJornada + "\n");

        if (jornada < 0 || jornada >= liga.getCalendario().size()) {
            throw new IllegalArgumentException("La jornada seleccionada no está en el calendario.");
        }

        List<Partido> partidos = new ArrayList<>(liga.getCalendario().get(jornada));

        resultadoJornada.append(String.format("%-15s %2s   %-15s %2s\n", "LOCAL", "", "VISITANTE", ""));

        for (Partido partido : partidos) {
            if (!partido.isSimulado()) {
                simularPartido(partido);
                resultadoJornada.append(String.format("%-15s %2d - %-15s %2d\n",
                        partido.getEquipoLocal().getNombre(), partido.getGolesLocal(),
                        partido.getEquipoVisitante().getNombre(), partido.getGolesVisitante()));
            } else {
                System.out.println("El partido ya fue simulado.");
            }
        }

        return resultadoJornada.toString();
    }
}
