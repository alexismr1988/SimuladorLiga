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
    private Entrenador entrenador;
    private double presupuesto;

    public Equipo(String nombre) {
        this.nombre = nombre;
        this.plantilla = new ArrayList<>();
        this.puntos = 0;
        this.alineacion=new Alineacion();
        this.entrenador = null;
        this.presupuesto=0;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Entrenador getEntrenador() {
        return entrenador;
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

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + puntos + " pts)";
    }
    
    public void sumarPuntos(int puntos){
        this.puntos += puntos;
    }
    
    public void incrementarPresupuesto(double cantidad) {
        this.presupuesto += cantidad;
    }

    public void reducirPresupuesto(double cantidad) {
        this.presupuesto -= cantidad;
    }
  
}
