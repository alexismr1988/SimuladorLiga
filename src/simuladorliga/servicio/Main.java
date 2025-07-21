package simuladorliga.servicio;

import simuladorliga.modelo.*;
import simuladorliga.persistencia.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Importar equipos desde CSV
        GestorFicheros gestor = new GestorFicheros();
        List<Equipo> equipos = gestor.importarEquiposDesdeCSV();

        // 2. Crear liga vacía (con nombre y si es ida/vuelta)
        Liga miLiga = new Liga("Liga Personalizada", true); // true para ida y vuelta

        // 3. Agregar equipos a la liga
        for (Equipo equipo : equipos) {
            miLiga.agregarEquipo(equipo);
        }

        // 4. Generar calendario de partidos
        miLiga.generarCalendarioIdaVuelta();

        // 5. (Opcional) Mostrar los equipos y el calendario
        System.out.println("Equipos cargados: " + miLiga.getEquipos().size());
        for (Equipo eq : miLiga.getEquipos()) {
            System.out.println(eq.getNombre() + " | Entrenador: " + eq.getEntrenador().getNombre() + " | Estilo: " + eq.getEntrenador().getEstilo());
        }

        System.out.println("\nCalendario generado:");
        for (int i = 0; i < miLiga.getCalendario().size(); i++) {
            System.out.println("Jornada " + (i+1));
            for (Partido p : miLiga.getCalendario().get(i)) {
                System.out.println("  " + p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre());
            }
        }
    }
}