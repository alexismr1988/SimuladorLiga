
package simuladorliga.modelo;

import java.util.List;

/**
 * Enum que representa el estilo táctico de un entrenador.
 * Cada estilo incluye una lista de formaciones compatibles según su filosofía de juego.
 */
public enum EstiloEntrenador {
    DEFENSIVO(List.of("541", "532", "451")),
    OFENSIVO(List.of("433", "343", "352")),
    POSESION(List.of("442", "433", "451")),
    CONTRAATAQUE(List.of("532", "352", "361"));

    private final List<String> tacticasCompatibles;

    /**
     * Constructor privado que asigna las tácticas compatibles al estilo.
     *
     * @param tacticasCompatibles Lista de formaciones compatibles con el estilo
     */
    EstiloEntrenador(List<String> tacticasCompatibles) {
        this.tacticasCompatibles = tacticasCompatibles;
    }
    
    /**
     * Indica si una formación dada es compatible con el estilo táctico.
     *
     * @param formacion Cadena que representa la formación (ej: "433")
     * @return true si la formación es compatible con el estilo
     */
    public boolean esCompatible(String formacion) {
        return tacticasCompatibles.contains(formacion);
    }

    /**
     * Devuelve la lista de formaciones compatibles con este estilo.
     *
     * @return lista de formaciones compatibles
     */
    public List<String> getTacticasCompatibles() {
        return tacticasCompatibles;
    }
}




