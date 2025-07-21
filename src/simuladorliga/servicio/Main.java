package simuladorliga.servicio;

import simuladorliga.modelo.*;
import simuladorliga.persistencia.GestorBD;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GestorBD gestor = new GestorBD();

        // Mostrar ligas actuales
        System.out.println("--- LIGAS INICIALES ---");
        List<Liga> ligas = gestor.muestraTodasLigas();
        for (Liga liga : ligas) {
            System.out.println("Liga: " + liga.getNombre());
        }

        // Modificar SOLO el nombre de la primera liga (nunca ida_vuelta)
        if (!ligas.isEmpty()) {
            String nombreLigaActual = ligas.get(0).getNombre();
            int idLiga = gestor.obtenerIdLigaPorNombre(nombreLigaActual);
            System.out.println("\n--- ACTUALIZANDO NOMBRE DE LA LIGA ---");
            gestor.updateLiga(idLiga, "Liga Renombrada");
        }

        // Mostrar ligas tras update
        System.out.println("\n--- LIGAS DESPUÉS DE UPDATE ---");
        ligas = gestor.muestraTodasLigas();
        for (Liga liga : ligas) {
            System.out.println("Liga: " + liga.getNombre());
        }

        // PROBANDO UPDATE EQUIPO
        System.out.println("\n--- PROBANDO UPDATE DE EQUIPO ---");
        List<Equipo> equipos = gestor.muestraTodosEquipos();
        if (!equipos.isEmpty()) {
            String nombreEquipo = equipos.get(0).getNombre();
            int idEquipo = gestor.obtenerIdEquipoPorNombre(nombreEquipo);
            // Actualizamos nombre y presupuesto del primer equipo
            gestor.updateEquipo(idEquipo, nombreEquipo + " CF", 123456789, null, 1); // null para idEntrenador y 1 como ejemplo idLiga
        }

        // Mostrar equipos después de update
        System.out.println("\n--- EQUIPOS DESPUÉS DE UPDATE ---");
        equipos = gestor.muestraTodosEquipos();
        for (Equipo equipo : equipos) {
            System.out.println("Equipo: " + equipo.getNombre());
        }

        // PROBANDO UPDATE JUGADOR
        System.out.println("\n--- PROBANDO UPDATE DE JUGADOR ---");
        if (!equipos.isEmpty()) {
            int idEquipo = gestor.obtenerIdEquipoPorNombre(equipos.get(0).getNombre());
            List<Jugador> jugadores = gestor.recuperarJugadoresPorEquipo(idEquipo);
            if (!jugadores.isEmpty()) {
                Jugador jugador = jugadores.get(0);
                int idJugador = 1; // Debes ajustar esto a cómo recuperas el id del jugador
                gestor.updateJugador(idJugador, jugador.getNombre() + " Jr.", jugador.getDorsal() + 1, jugador.getPosicion().name(), jugador.getMedia() + 1, idEquipo);
            }
        }

        // ELIMINAR EL PRIMER EQUIPO
        System.out.println("\n--- ELIMINANDO EQUIPO ---");
        equipos = gestor.muestraTodosEquipos();
        if (!equipos.isEmpty()) {
            int idEquipo = gestor.obtenerIdEquipoPorNombre(equipos.get(0).getNombre());
            gestor.deleteEquipo(idEquipo);
        }

        // ELIMINAR LA PRIMERA LIGA (ya actualizada antes)
        System.out.println("\n--- ELIMINANDO LIGA ---");
        ligas = gestor.muestraTodasLigas();
        if (!ligas.isEmpty()) {
            int idLiga = gestor.obtenerIdLigaPorNombre(ligas.get(0).getNombre());
            gestor.deleteLiga(idLiga);
        }

        // Mostrar resultados finales
        System.out.println("\n--- LIGAS FINALES EN BD ---");
        ligas = gestor.muestraTodasLigas();
        for (Liga liga : ligas) {
            System.out.println("Liga: " + liga.getNombre());
        }

        System.out.println("\n--- EQUIPOS FINALES EN BD ---");
        equipos = gestor.muestraTodosEquipos();
        for (Equipo equipo : equipos) {
            System.out.println("Equipo: " + equipo.getNombre());
        }

        System.out.println("\n---- PRUEBA DE UPDATE Y DELETE COMPLETADA ----");
    }
}
