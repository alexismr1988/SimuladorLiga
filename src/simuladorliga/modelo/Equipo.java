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
    private int id;

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
        return nombre;
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
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Alineacion generarAlineacionAuto() {
        Alineacion alineacion = new Alineacion();

        int[] formacion = new int[3];
        boolean esValida = false;

        // Paso 1: Generar formación aleatoria válida
        while (!esValida) {
            int defensas = valorAleatorio(3, 5);
            int medios = valorAleatorio(3, 6);
            int delanteros = valorAleatorio(1, 3);

            if (defensas + medios + delanteros <= 10) { // +1 portero = 11
                formacion[0] = defensas;
                formacion[1] = medios;
                formacion[2] = delanteros;
                esValida = true;
            }
        }

        int numDefensas = formacion[0];
        int numMedios = formacion[1];
        int numDelanteros = formacion[2];

        // Paso 2: Ordenar plantilla por media de mayor a menor
        List<Jugador> plantilla = new ArrayList<>(this.getPlantilla());
        plantilla.sort(Comparator.comparingInt(Jugador::getMedia).reversed());

        // Paso 3: Seleccionar mejores jugadores por posición
        List<Jugador> porteros = new ArrayList<>();
        List<Jugador> defensas = new ArrayList<>();
        List<Jugador> medios = new ArrayList<>();
        List<Jugador> delanteros = new ArrayList<>();

        for (Jugador j : plantilla) {
            if (j.getPosicion() == Posicion.PORTERO && porteros.size() < 1) {
                porteros.add(j);
            } else if ((j.getPosicion() == Posicion.DEFENSA || j.getPosicion() == Posicion.LATERAL) && defensas.size() < numDefensas) {
                defensas.add(j);
            } else if ((j.getPosicion() == Posicion.MEDIOCENTRO || j.getPosicion() == Posicion.MCO || j.getPosicion() == Posicion.MCD) && medios.size() < numMedios) {
                medios.add(j);
            } else if ((j.getPosicion() == Posicion.DC || j.getPosicion() == Posicion.SD) && delanteros.size() < numDelanteros) {
                delanteros.add(j);
            }
        }

        // Paso 4: Agregar los seleccionados a la alineación
        for (Jugador j : porteros) alineacion.agregarJugador(j);
        for (Jugador j : defensas) alineacion.agregarJugador(j);
        for (Jugador j : medios) alineacion.agregarJugador(j);
        for (Jugador j : delanteros) alineacion.agregarJugador(j);

        // Paso 5: Si faltan jugadores para llegar a 11, añade los mejores restantes
        if (alineacion.getTitulares().size() < 11) {
            for (Jugador j : plantilla) {
                if (!alineacion.getTitulares().contains(j)) {
                    alineacion.agregarJugador(j);
                    if (alineacion.getTitulares().size() == 11) break;
                }
            }
        }

        // Puedes añadir prints para depurar:
        System.out.println("Alineación generada (" + this.getNombre() + "): " + alineacion.getTitulares().size());
        for (Jugador j : alineacion.getTitulares()) {
            System.out.println(j.getNombre() + " - " + j.getPosicion());
        }

        return alineacion;
    }

    public int getCantidadJugadores(){
        return this.getPlantilla().size();
    }
    
    public double getMediaPlantilla(){
        List<Jugador> jugadores = this.plantilla;
        double mediaTotal = 0;
        int tamanoPlantilla = this.getCantidadJugadores();
        
        for (Jugador jugador : jugadores) {
            mediaTotal+=jugador.getMedia();
        }
        return Math.round((mediaTotal / tamanoPlantilla) * 100.0) / 100.0;
    }

    
    //Método auxiliar usado en generar alineaciones automáticas
    public int valorAleatorio(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
    }
 
}
