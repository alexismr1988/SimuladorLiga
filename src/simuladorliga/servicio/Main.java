/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorliga.servicio;

import simuladorliga.modelo.*;

/**
 *
 * @author PC
 */
public class Main {
    public static void main(String[] args) {
        Liga miLiga=new Liga("Primera", 6, true);
        Equipo madrid=new Equipo("Real Madrid");
        Equipo barcelona=new Equipo("Barcelona");
        Equipo betis=new Equipo("betis");
        Equipo sevilla=new Equipo("sevilla");
        
        //Creo los jugadores de las plantillas
        for (int i = 1; i <= 11; i++) {
            madrid.agregarJugador(new Jugador("JugadorRM" + i, i, Posicion.MEDIOCENTRO, 75 + (int)(Math.random() * 10)));
            barcelona.agregarJugador(new Jugador("JugadorFCB" + i, i, Posicion.DC, 78 + (int)(Math.random() * 10)));
            betis.agregarJugador(new Jugador("JugadorBET" + i, i, Posicion.DEFENSA, 70 + (int)(Math.random() * 10)));
            sevilla.agregarJugador(new Jugador("JugadorSEV" + i, i, Posicion.MCD, 72 + (int)(Math.random() * 10)));
        }
        
        // Crear alineaciones para cada equipo (primeros 11 jugadores de la plantilla)
        for (int i = 0; i < 11; i++) {
            madrid.getAlineacion().agregarJugador(madrid.getPlantilla().get(i));
            barcelona.getAlineacion().agregarJugador(barcelona.getPlantilla().get(i));
            betis.getAlineacion().agregarJugador(betis.getPlantilla().get(i));
            sevilla.getAlineacion().agregarJugador(sevilla.getPlantilla().get(i));
        }

        
        miLiga.agregarEquipo(madrid);
        miLiga.agregarEquipo(barcelona);
        miLiga.agregarEquipo(betis);
        miLiga.agregarEquipo(sevilla);
        
        miLiga.generarCalendarioIdaVuelta();
        
        Simulador miSimulador=new Simulador(miLiga);
        
        System.out.println(miSimulador.simularJornada(1));
        System.out.println(miSimulador.simularJornada(2));
        System.out.println(miSimulador.simularJornada(3));
        System.out.println(miSimulador.simularJornada(4));
        System.out.println(miSimulador.simularJornada(5));
        System.out.println(miSimulador.simularJornada(6));
        
        
        System.out.println(miLiga.mostrarEstadisticas());

    }
}
