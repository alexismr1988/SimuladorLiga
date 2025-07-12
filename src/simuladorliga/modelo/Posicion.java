package simuladorliga.modelo;

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
