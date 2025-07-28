package simuladorliga.modelo;

import java.util.Objects;

/**
 * Representa un jugador de fútbol con nombre, dorsal, posición, media de habilidad e ID.
 * La media debe estar entre 1 y 100, y puede afectar directamente a la simulación.
 */
public class Jugador {
    private String nombre;
    private int dorsal;
    private Posicion posicion;
    private int media;
    private int id;

    /**
     * Crea un jugador con los atributos esenciales.
     *
     * @param nombre Nombre del jugador
     * @param dorsal Número en la camiseta
     * @param posicion Posición principal (portero, defensa, etc.)
     * @param media Valor numérico entre 1 y 100 que representa la calidad
     */
    public Jugador(String nombre,int dorsal, Posicion posicion, int media) {
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

    /**
     * Establece la media del jugador, con validación de rango.
     *
     * @param media Valor entre 1 y 100
     * @throws IllegalArgumentException si el valor está fuera de rango
     */
    public void setMedia(int media) {   
        if(media<1 || media>100){
            throw new IllegalArgumentException("la media debe ser entre 1 y 100."); 
        } else this.media = media;       
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    /**
     * Compara jugadores por nombre. Dos jugadores con el mismo nombre son considerados iguales.
     *
     * @param obj Otro objeto a comparar
     * @return true si el nombre es igual
     */
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