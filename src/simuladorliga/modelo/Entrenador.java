
package simuladorliga.modelo;

/**
 *
 * @author PC
 */

public class Entrenador {
    private String nombre;
    private EstiloEntrenador estilo;

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

