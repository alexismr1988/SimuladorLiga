package simuladorliga.servicio;

import java.io.*;
import java.util.*;
import simuladorliga.modelo.*;
import simuladorliga.persistencia.GestorFicheros;

/**
 *
 * @author PC
 */
public class Main {
    public static void main(String[] args) {
        // 1. Crear la liga y los equipos
        Liga miLiga = new Liga("Primera", 4, true);

        Equipo madrid = new Equipo("Real Madrid");
        Equipo barcelona = new Equipo("Barcelona");
        Equipo betis = new Equipo("Betis");
        Equipo sevilla = new Equipo("Sevilla");
        
        // 1.1 Asignar entrenadores a cada equipo con diferentes estilos
        madrid.setEntrenador(new Entrenador("Ancelotti", EstiloEntrenador.OFENSIVO));
        barcelona.setEntrenador(new Entrenador("Xavi", EstiloEntrenador.POSESION));
        betis.setEntrenador(new Entrenador("Pellegrini", EstiloEntrenador.CONTRAATAQUE));
        sevilla.setEntrenador(new Entrenador("Mendilibar", EstiloEntrenador.DEFENSIVO));

        // 2. Crear jugadores ficticios para cada equipo (con posiciones variadas y medias realistas)
        for (int i = 1; i <= 11; i++) {
            madrid.agregarJugador(new Jugador("JugadorRM" + i, i, Posicion.values()[i % Posicion.values().length], 75 + (int)(Math.random() * 15)));
            barcelona.agregarJugador(new Jugador("JugadorFCB" + i, i, Posicion.values()[i % Posicion.values().length], 75 + (int)(Math.random() * 15)));
            betis.agregarJugador(new Jugador("JugadorBET" + i, i, Posicion.values()[i % Posicion.values().length], 70 + (int)(Math.random() * 15)));
            sevilla.agregarJugador(new Jugador("JugadorSEV" + i, i, Posicion.values()[i % Posicion.values().length], 72 + (int)(Math.random() * 15)));
        }

        // 3. Asignar una alineación a cada equipo con los titulares (aquí simplemente los 11 primeros)
        for (int i = 0; i < 11; i++) {
            madrid.getAlineacion().agregarJugador(madrid.getPlantilla().get(i));
            barcelona.getAlineacion().agregarJugador(barcelona.getPlantilla().get(i));
            betis.getAlineacion().agregarJugador(betis.getPlantilla().get(i));
            sevilla.getAlineacion().agregarJugador(sevilla.getPlantilla().get(i));
        }

        // 4. Agregar los equipos a la liga
        miLiga.agregarEquipo(madrid);
        miLiga.agregarEquipo(barcelona);
        miLiga.agregarEquipo(betis);
        miLiga.agregarEquipo(sevilla);

        // 5. Generar el calendario (ida y vuelta)
        miLiga.generarCalendarioIdaVuelta();

        // 6. Crear el simulador y simular TODAS las jornadas
        Simulador simulador = new Simulador(miLiga);

        for (int numJornada = 1; numJornada <= miLiga.getCalendario().size(); numJornada++) {
            System.out.println(simulador.simularJornada(numJornada));
        }

        // 7. Mostrar la clasificación y estadísticas finales
        System.out.println(miLiga.mostrarEstadisticas());
    }


}
