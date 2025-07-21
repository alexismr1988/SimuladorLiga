package simuladorliga.modelo;

import java.time.LocalDate;

/**
 *
 * @author PC
 */
public class Partido {
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private boolean simulado;
    private int jornada;
    
    private Partido(){
        
    }

    public Partido(Equipo equipoLocal, Equipo equipoVisitante, int golesLocal, int golesVisitante, int jornada) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        simulado=false;
        this.jornada = jornada;
    }
    
    
    
    public void actualizarPuntos(){
        if(golesLocal>golesVisitante){
            equipoLocal.sumarPuntos(3);
        } else if (golesLocal<golesVisitante){
            equipoVisitante.sumarPuntos(3);
        } else {
           equipoLocal.sumarPuntos(1); 
           equipoVisitante.sumarPuntos(1);
        }
        
        actualizarGoles();
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public boolean isSimulado() {
        return simulado;
    }

    public void setSimulado(boolean simulado) {
        this.simulado = simulado;
    }
    
    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }
    
    public void actualizarGoles(){
        equipoLocal.sumarGolesFavor(golesLocal);
        equipoLocal.sumarGolesContra(golesVisitante);
        equipoVisitante.sumarGolesFavor(golesVisitante);
        equipoVisitante.sumarGolesContra(golesLocal);
    }
}
