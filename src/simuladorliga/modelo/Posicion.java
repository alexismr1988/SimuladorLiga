package simuladorliga.modelo;

/**
 * Enum que representa las posibles posiciones de un jugador en el campo.
 * Cada posición incluye una descripción textual para mostrar en interfaces.
 */
public enum Posicion {
    PORTERO("Portero"),
    DEFENSA("Defensa"),
    LATERAL("Lateral"),
    MCD("Mediocentro Defensivo"),
    MEDIOCENTRO("Mediocentro"),
    MCO("Mediocentro Ofensivo"),
    SD("Segundo Delantero"),
    DC("Delantero Centro");

    private final String descripcion;

    /**
     * Devuelve la descripción legible de la posición.
     * @return descripción textual
     */
    Posicion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
