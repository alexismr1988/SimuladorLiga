/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorliga.servicio;

import java.util.ArrayList;
import java.util.List;
import simuladorliga.modelo.*;

/**
 *
 * @author PC
 */
public class Simulador {
    private Liga liga;
    
    public Simulador(Liga liga){
        this.liga=liga;
    }
    
    public void simularPartido(Partido partido) {
        Equipo local = partido.getEquipoLocal();
        Equipo visitante = partido.getEquipoVisitante();

        double mediaLocal = local.getAlineacion().calcularMedia();
        double mediaVisitante = visitante.getAlineacion().calcularMedia();

        // Ventaja local
        mediaLocal *= 1.10; // +10% por ser local

        // Normalización
        double totalMedias = mediaLocal + mediaVisitante;
        double controlLocal = mediaLocal / totalMedias;
        double controlVisitante = mediaVisitante / totalMedias;

        // Generar número de ocasiones totales por partido
        int totalOcasiones = 12; 
        int ocasionesLocal = (int) Math.round(controlLocal * totalOcasiones);
        int ocasionesVisitante = totalOcasiones - ocasionesLocal;

        // Probabilidad de que una ocasión acabe en gol
        double probGol = 0.25;

        int golesLocal = 0;
        for (int i = 0; i < ocasionesLocal; i++) {
            if (Math.random() < probGol) golesLocal++;
        }

        int golesVisitante = 0;
        for (int i = 0; i < ocasionesVisitante; i++) {
            if (Math.random() < probGol) golesVisitante++;
        }

        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setSimulado(true);
        partido.actualizarPuntos();
                
    }

    public String simularJornada(int numeroJornada) {
        int jornada = numeroJornada - 1;
        StringBuilder resultadoJornada = new StringBuilder("Jornada " + numeroJornada + "\n");

        if (jornada < 0 || jornada >= liga.getCalendario().size()) {
            throw new IllegalArgumentException("La jornada seleccionada no está en el calendario.");
        }

        List<Partido> partidos = new ArrayList<>(liga.getCalendario().get(jornada));

        // Cabecera alineada
        resultadoJornada.append(String.format("%-15s %2s   %-15s %2s\n", "LOCAL", "", "VISITANTE", ""));

        for (Partido partido : partidos) {
            if (!partido.isSimulado()) {
                simularPartido(partido);
                resultadoJornada.append(String.format("%-15s %2d - %-15s %2d\n",
                    partido.getEquipoLocal().getNombre(), partido.getGolesLocal(),
                    partido.getEquipoVisitante().getNombre(), partido.getGolesVisitante()));
            } else {
                System.out.println("El partido ya fue simulado.");
            }
        }

        return resultadoJornada.toString();
    }

}
