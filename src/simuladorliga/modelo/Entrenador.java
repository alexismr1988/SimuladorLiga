
package simuladorliga.modelo;


/**
 * Representa a un entrenador asociado a un equipo.
 * Cada entrenador tiene un nombre y un estilo táctico que influye en la simulación.
 */
public class Entrenador {
    private String nombre;
    private EstiloEntrenador estilo;

    
    /**
     * Crea un nuevo entrenador con nombre y estilo definidos.
     *
     * @param nombre Nombre del entrenador
     * @param estilo Estilo táctico (Defensivo, Ofensivo, etc.)
     */
    public Entrenador(String nombre, EstiloEntrenador estilo) {
        this.nombre = nombre;
        this.estilo = estilo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstiloEntrenador getEstilo() {
        return estilo;
    }

    public void setEstilo(EstiloEntrenador estilo) {
        this.estilo = estilo;
    }
    
}

