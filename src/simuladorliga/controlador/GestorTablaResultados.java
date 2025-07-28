package simuladorliga.controlador;

import java.text.*;
import java.util.*;
import javax.swing.table.*;
import simuladorliga.modelo.*;


/**
 * Clase de utilidad encargada de generar modelos de tabla (DefaultTableModel)
 * para representar visualmente datos de equipos, jugadores, clasificación y jornadas
 * en la interfaz Swing del simulador.
 * 
 * Todos los métodos son estáticos y están pensados para integrarse directamente en JTables.
 */
public class GestorTablaResultados {
    // Constantes para nombres de columnas (opcional, pero recomendable)
    public static final String COL_NOMBRE = "Nombre";
    public static final String COL_PRESUPUESTO = "Presupuesto";
    public static final String COL_ENTRENADOR = "Entrenador";
    public static final String COL_CANTIDAD_JUGADORES = "Nº Jugadores";
    public static final String COL_MEDIA_PLANTILLA= "Media equipo";
    public static final String COL_MEDIA= "Media ";
    public static final String COL_POSICION = "Posicion";
    public static final String COL_GOLES_FAVOR = "GF";
    public static final String COL_GOLES_CONTRA= "GC";
    public static final String COL_DIFERENCIA_GOLES = "DF";
    public static final String COL_PUNTOS = "Puntos";
    public static final String COL_DORSAL = "Dorsal";
    public static final String COL_LOCAL = "Local";
    public static final String COL_VISITANTE = "Visitante";
    public static final String COL_RESULTADO = "Resultado";
    public static final String COL_JORNADA = "Jornada";
    public static final String COL_SIMULADO= "Simulado";
    
    

    /**
     * Genera un modelo de tabla para mostrar equipos de la liga ordenados por la media del equipo.
     * @param equipos Lista de equipos a mostrar
     * @return DefaultTableModel listo para usar en un JTable
     */
    public static DefaultTableModel modeloParaEquipos(List<Equipo> equipos) {
        DefaultTableModel modelo = new DefaultTableModel();
        equipos.sort((e1, e2) -> Double.compare(e2.getMediaPlantilla(), e1.getMediaPlantilla()));

        modelo.addColumn(COL_NOMBRE);
        modelo.addColumn(COL_PRESUPUESTO);
        modelo.addColumn(COL_ENTRENADOR);
        modelo.addColumn(COL_CANTIDAD_JUGADORES);
        modelo.addColumn(COL_MEDIA_PLANTILLA);

        for (Equipo eq : equipos) {
            
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMANY);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        String presupuestoFormateado = nf.format(eq.getPresupuesto());
            modelo.addRow(new Object[]{
                eq.getNombre(),
                presupuestoFormateado,
                eq.getEntrenador() != null ? eq.getEntrenador().getNombre() : "Sin entrenador",
                eq.getCantidadJugadores(),
                eq.getMediaPlantilla()
            });
        }
        return modelo;
    }
    
    /**
     * Genera un modelo de tabla para mostrar la plantilla de un equipo concreto ordenada por el número de dorsal.
     *
     * @param equipo Equipo del cual se mostrarán los jugadores
     * @return DefaultTableModel con nombre, posición, media y dorsal de los jugadores
     */
    public static DefaultTableModel modeloParaPlantillas(Equipo equipo) {
        DefaultTableModel modelo = new DefaultTableModel();
        List<Jugador> jugadores = equipo.getPlantilla();
        jugadores.sort((e1, e2) -> Double.compare(e1.getDorsal(), e2.getDorsal()));

        modelo.addColumn(COL_NOMBRE);
        modelo.addColumn(COL_POSICION);
        modelo.addColumn(COL_MEDIA);
        modelo.addColumn(COL_DORSAL);
        

        for (Jugador jugador : jugadores) {
            modelo.addRow(new Object[]{
                jugador.getNombre(),
                jugador.getPosicion(),
                jugador.getMedia(),
                jugador.getDorsal()
            });
        }
        return modelo;
    }
    
    /**
     * Genera un modelo de tabla con la clasificación ordenada por puntos.
     *
     * @param equipos Lista de equipos a clasificar
     * @return DefaultTableModel con posición, nombre, goles y puntos
     */
    public static DefaultTableModel modeloClasificacion(List<Equipo> equipos) {
        DefaultTableModel modelo = new DefaultTableModel();
        equipos.sort((e1, e2) -> Integer.compare(e2.getPuntos(), e1.getPuntos()));
        
        int posicion = 1;
        modelo.addColumn(COL_POSICION);
        modelo.addColumn(COL_NOMBRE);
        modelo.addColumn(COL_GOLES_FAVOR);
        modelo.addColumn(COL_GOLES_CONTRA);
        modelo.addColumn(COL_DIFERENCIA_GOLES);
        modelo.addColumn(COL_PUNTOS);
        

        for (Equipo eq : equipos) {
            modelo.addRow(new Object[]{
                posicion++,
                eq.getNombre(),
                eq.getGolesFavor(),
                eq.getGolesContra(),
                eq.getDiferenciaGoles(),
                eq.getPuntos()                
            });
        }
        return modelo;
    }
    
    /**
     * Genera un modelo de tabla con los partidos de la jornada elegida.
     *
     * @param partidos Lista de partidos a mostrar
     * @return DefaultTableModel con equipos, resultado, jornada y estado de simulación
     */
    public static DefaultTableModel modeloJornadas(List<Partido> partidos) {
        DefaultTableModel modelo = new DefaultTableModel();
        
        modelo.addColumn(COL_LOCAL);
        modelo.addColumn(COL_VISITANTE);
        modelo.addColumn(COL_RESULTADO);
        modelo.addColumn(COL_JORNADA);
        modelo.addColumn(COL_SIMULADO);

        for (Partido partido : partidos) {
            modelo.addRow(new Object[]{
                partido.getEquipoLocal().getNombre(),
                partido.getEquipoVisitante().getNombre(),
                partido.getGolesLocal() + " - " + partido.getGolesVisitante(), // aquí estaba mal
                partido.getJornada(),
                partido.isSimulado() ? "SI" : "NO",                
            });
        }
        return modelo;
    }
    
    
}
