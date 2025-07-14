/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        // 1. Crear la liga y los equipos (como hiciste antes)
        Liga miLiga = new Liga("Primera", 6, true);
        Equipo madrid = new Equipo("Real Madrid");
        Equipo barcelona = new Equipo("Barcelona");
        Equipo betis = new Equipo("betis");
        Equipo sevilla = new Equipo("sevilla");

        for (int i = 1; i <= 11; i++) {
            madrid.agregarJugador(new Jugador("JugadorRM" + i, i, Posicion.MEDIOCENTRO, 75 + (int)(Math.random() * 10)));
            barcelona.agregarJugador(new Jugador("JugadorFCB" + i, i, Posicion.DC, 78 + (int)(Math.random() * 10)));
            betis.agregarJugador(new Jugador("JugadorBET" + i, i, Posicion.DEFENSA, 70 + (int)(Math.random() * 10)));
            sevilla.agregarJugador(new Jugador("JugadorSEV" + i, i, Posicion.MCD, 72 + (int)(Math.random() * 10)));
        }

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

        // 2. Generar el calendario (necesario antes de cargar)
        miLiga.generarCalendarioIdaVuelta();

        // 3. Cargar los resultados desde el archivo
        GestorFicheros.cargarResultados(miLiga);

        // 4. Mostrar por consola los partidos simulados ya cargados
        int jornada = 1;
        for (List<Partido> lista : miLiga.getCalendario()) {
            System.out.println("Jornada " + jornada);
            for (Partido partido : lista) {
                if (partido.isSimulado()) {
                    System.out.println(partido.getEquipoLocal().getNombre() + " " + partido.getGolesLocal()
                            + " - " + partido.getGolesVisitante() + " " + partido.getEquipoVisitante().getNombre());
                }
            }
            jornada++;
            System.out.println();
        }

        // 5. Mostrar la clasificación basada en los resultados cargados
        System.out.println(miLiga.mostrarEstadisticas());
    }

}
