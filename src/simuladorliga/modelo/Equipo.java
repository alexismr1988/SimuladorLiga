package simuladorliga.modelo;

import java.util.*;

/**
 *
 * @author PC
 */
public class Equipo {
    private String nombre;
    private List<Jugador> plantilla;
    private int puntos;
    private int golesFavor;
    private int golesContra;
    private Alineacion alineacion;

    public Equipo(String nombre) {
        this.nombre = nombre;
        this.plantilla = new ArrayList<>();
        this.puntos = 0;
        this.alineacion=new Alineacion();
    }
    
    public void agregarJugador(Jugador jugador){
        this.plantilla.add(jugador);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Jugador> getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(List<Jugador> plantilla) {
        this.plantilla = plantilla;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getGolesFavor() {
        return golesFavor;
    }

    public void setGolesFavor(int golesFavor) {
        this.golesFavor = golesFavor;
    }

    public int getGolesContra() {
        return golesContra;
    }

    public void setGolesContra(int golesContra) {
        this.golesContra = golesContra;
    }
    
    public void sumarGolesFavor(int goles){
        this.golesFavor += goles;
    }
    
    public void sumarGolesContra(int goles){
        this.golesContra += goles;
    }

    public int getDiferenciaGoles() {
        return golesFavor - golesContra;
    }

    public Alineacion getAlineacion() {
        return alineacion;
    }

    public void setAlineacion(Alineacion alineacion) {
        this.alineacion = alineacion;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + puntos + " pts)";
    }
    
    public void sumarPuntos(int puntos){
        this.puntos += puntos;
    }
    
}
