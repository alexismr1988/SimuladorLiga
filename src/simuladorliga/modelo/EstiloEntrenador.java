
package simuladorliga.modelo;

import java.util.List;


public enum EstiloEntrenador {
    DEFENSIVO(List.of("541", "532", "451")),
    OFENSIVO(List.of("433", "343", "352")),
    POSESION(List.of("442", "433", "451")),
    CONTRAATAQUE(List.of("532", "352", "361"));

    private final List<String> tacticasCompatibles;

    EstiloEntrenador(List<String> tacticasCompatibles) {
        this.tacticasCompatibles = tacticasCompatibles;
    }

    public boolean esCompatible(String formacion) {
        return tacticasCompatibles.contains(formacion);
    }

    public List<String> getTacticasCompatibles() {
        return tacticasCompatibles;
    }
}




