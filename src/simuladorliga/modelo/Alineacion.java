
package simuladorliga.modelo;

import java.util.*;


public class Alineacion {
    private Set<Jugador> titulares;
    
    public Alineacion(){
        this.titulares=new LinkedHashSet<>();
    }

    public Set<Jugador> getTitulares() {
        return titulares;
    }

    public void setTitulares(Set<Jugador> titulares) {
        this.titulares = titulares;
    }
    
    public void agregarJugador(Jugador jugador){
        if(titulares.size()>=11){
            throw new IllegalStateException("La alineación ya tiene 11 jugadores.");
        }
        this.titulares.add(jugador);
    }
    
    public boolean validarAlineacion(){
        if(titulares.size()!=11){
            throw new IllegalStateException("El equipo no tiene una alineación válida de 11 jugadores.");
        }
        validarPosiciones();
        return true;
    }
    
    public boolean validarPosiciones(){
        int porteros=0;
        int defensas=0;
        int medios=0;
        int delanteros=0;
        
        for (Jugador titular : titulares) {
            if(titular.getPosicion()==Posicion.PORTERO){
                porteros++;
            } else if(titular.getPosicion()==Posicion.DEFENSA || titular.getPosicion()==Posicion.LATERAL){
                defensas++;
            } else if(titular.getPosicion()==Posicion.MCD || titular.getPosicion()==Posicion.MCO || titular.getPosicion()==Posicion.MEDIOCENTRO){
                medios++;
            } else {
                delanteros++;
            }
        }
        
        if(porteros!=1){
            throw new IllegalStateException("La alineación solo puede tener un portero");
        } else if(defensas<3 || defensas>5){
            throw new IllegalStateException("Solo está permitido un mínimo de 3 defensas y un máximo de 5");
        } else if(medios<3 || medios>6){
            throw new IllegalStateException("Solo está permitido un mínimo de 3 medios y un máximo de 6");
        } else if (delanteros<1 || delanteros>3){
            throw new IllegalStateException("Solo está permitido un mínimo de 1 delantero y un máximo de 3");
        }
        
        return true;
    }
    
    public double calcularMedia(){
        double total=0;
        for (Jugador titular : titulares) {
            total+=titular.getMedia();
        }
        return total/11;
    }
}
