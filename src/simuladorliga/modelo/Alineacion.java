
package simuladorliga.modelo;

import java.util.*;

/**
 * Representa una alineación válida de 11 jugadores titulares para un equipo.
 * Incluye lógica para validar la formación, contar líneas y calcular medias por posición.
 */
public class Alineacion {
    private Set<Jugador> titulares;
    
    /**
     * Crea una alineación vacía con un conjunto ordenado de titulares.
     */
    public Alineacion(){
        this.titulares=new LinkedHashSet<>();
    }

    public Set<Jugador> getTitulares() {
        return titulares;
    }

    public void setTitulares(Set<Jugador> titulares) {
        this.titulares = titulares;
    }
    
    /**
     * Añade un jugador a la alineación, siempre que no se exceda el límite de 11.
     *
     * @param jugador el jugador a añadir como titular
     * @throws IllegalStateException si ya hay 11 jugadores en la alineación
     */
    public void agregarJugador(Jugador jugador){
        if(titulares.size()>=11){
            throw new IllegalStateException("La alineación ya tiene 11 jugadores.");
        }
        this.titulares.add(jugador);
    }
    
    /**
     * Valida que la alineación tenga exactamente 11 jugadores y que cumpla con los criterios de posiciones.
     *
     * @return true si la alineación es válida
     * @throws IllegalStateException si no hay 11 jugadores o si las posiciones no respetan el esquema válido
     */
    public boolean validarTamano(){
        if(titulares.size()!=11){
            throw new IllegalStateException("El equipo no tiene una alineación válida de 11 jugadores.");
        }
        validarPosiciones();
        return true;
    }
    
    public int getDefensas(){
        int numeroDefensas=0;
        for (Jugador titular : titulares) {
            if(titular.getPosicion()==Posicion.DEFENSA || titular.getPosicion()==Posicion.LATERAL) numeroDefensas++;
        }
        return numeroDefensas;
    }
    
    public int getMedios(){
        int numeroMedios=0;
        for (Jugador titular : titulares) {
            if(titular.getPosicion()==Posicion.MCD || titular.getPosicion()==Posicion.MCO || titular.getPosicion()==Posicion.MEDIOCENTRO) numeroMedios++;
        }
        return numeroMedios;
    }
    
    public int getDelanteros(){
        int numeroDelanteros=0;
        for (Jugador titular : titulares) {
            if(titular.getPosicion()==Posicion.SD || titular.getPosicion()==Posicion.DC) numeroDelanteros++;
        }
        return numeroDelanteros;
    }
    
    /**
     * Valida que haya un portero, entre 3 y 5 defensas, entre 3 y 6 centrocampistas y entre 1 y 3 delanteros.
     *
     * @return true si la distribución de posiciones es válida
     * @throws IllegalStateException si alguna línea no cumple los mínimos o máximos establecidos
     */
    public boolean validarPosiciones() {
        int porteros = 0;
        int defensas = getDefensas();
        int medios = getMedios();
        int delanteros = getDelanteros();

        // Cuenta porteros
        for (Jugador titular : titulares) {
            if (titular.getPosicion() == Posicion.PORTERO) {
                porteros++;
            }
        }

        if (porteros != 1) {
            throw new IllegalStateException("La alineación debe tener exactamente un portero.");
        }
        if (defensas < 3 || defensas > 5) {
            throw new IllegalStateException("Solo se permite un mínimo de 3 y un máximo de 5 defensas.");
        }
        if (medios < 3 || medios > 6) {
            throw new IllegalStateException("Solo se permite un mínimo de 3 y un máximo de 6 centrocampistas.");
        }
        if (delanteros < 1 || delanteros > 3) {
            throw new IllegalStateException("Solo se permite un mínimo de 1 y un máximo de 3 delanteros.");
        }
        // Si todo está OK, devuelve true
        return true;
    }
    
    public int mediaPorPosiciones(Posicion... posiciones) {
        int suma = 0;
        int contador = 0;

        for (Jugador jugador : titulares) {
            for (Posicion p : posiciones) {
                if (jugador.getPosicion() == p) {
                    suma += jugador.getMedia();
                    contador++;
                    break;
                }
            }
        }

        return (contador == 0) ? 0 : suma / contador;
    }


    /**
     * Calcula la media global de la alineación (media aritmética de todos los jugadores titulares).
     *
     * @return media total como número decimal
     */
    public double calcularMedia(){
        double total=0;
        for (Jugador titular : titulares) {
            total+=titular.getMedia();
        }
        return total/11;
    }
    
    /**
    * Devuelve un string con la formación generada, validando antes su tamaño.
     * Ejemplo: "443" = 4 defensas, 4 medios, 3 delanteros.
     *
     * @return cadena que representa la formación por líneas
     */
    public String contarLineas(){
        
        this.validarTamano();
        
        int[] lineas = new int[3];
        lineas[0] = this.getDefensas();
        lineas[1] = this.getMedios();
        lineas[2] = this.getDelanteros();
        
        return "" + getDefensas() + getMedios() + getDelanteros();
    }
    
   
}