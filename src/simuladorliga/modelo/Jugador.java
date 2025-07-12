package simuladorliga.modelo;

import java.util.Objects;

/**
 *
 * @author PC
 */
public class Jugador {
    private String nombre;
    private int dorsal;
    private Posicion posicion;
    private int media;

    public Jugador(String nombre, int dorsal, Posicion posicion, int media) {
        this.nombre = nombre;
        this.dorsal = dorsal;
        this.posicion = posicion;
        setMedia(media);
    }

    public Jugador() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        
        if(media<1 || media>100){
            throw new IllegalArgumentException("la media debe ser entre 1 y 100."); 
        } else this.media = media;       
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Jugador other = (Jugador) obj;
        return Objects.equals(this.nombre, other.nombre);
    }
    
    
  
}
