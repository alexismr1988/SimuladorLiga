package simuladorliga.modelo;

import java.time.LocalDate;

/**
 * Representa un partido entre dos equipos, incluyendo goles, jornada y estado de simulación.
 * Permite actualizar los puntos y estadísticas de los equipos tras jugarse el partido.
 */
public class Partido {
    private int idPartido; 
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private boolean simulado;
    private int jornada;
    
    private Partido(){
        
    }
    
    /**
     * Crea un partido con equipos, goles iniciales y número de jornada.
     *
     * @param equipoLocal equipo que juega como local
     * @param equipoVisitante equipo visitante
     * @param golesLocal goles iniciales del local (0 en la mayoría de casos)
     * @param golesVisitante goles iniciales del visitante
     * @param jornada número de jornada en la que se juega
     */
    public Partido(Equipo equipoLocal, Equipo equipoVisitante, int golesLocal, int golesVisitante, int jornada) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        simulado=false;
        this.jornada = jornada;
    }
    
    
    /**
     * Actualiza los puntos de cada equipo según el resultado del partido.
     * También actualiza goles a favor y en contra para ambos equipos.
     */ 
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
    
    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }
    
    public void actualizarGoles(){
        equipoLocal.sumarGolesFavor(golesLocal);
        equipoLocal.sumarGolesContra(golesVisitante);
        equipoVisitante.sumarGolesFavor(golesVisitante);
        equipoVisitante.sumarGolesContra(golesLocal);
    }
    
    /**
     * Restablece los goles del partido a 0 y lo marca simulado como false.
     */ 
    public void resetearPartido(){
        this.setGolesLocal(0);
        this.setGolesVisitante(0);
        this.setSimulado(false);
    }
}
