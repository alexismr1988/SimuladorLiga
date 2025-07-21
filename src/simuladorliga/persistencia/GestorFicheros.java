package simuladorliga.persistencia;

import java.io.*;
import java.util.*;
import simuladorliga.modelo.*;

public class GestorFicheros {

    public static void guardarEquipos(List<Equipo> equipos, String nombreLiga) {
        File f = new File("ligas/" + nombreLiga + "/plantillas.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (Equipo equipo : equipos) {
                List<Jugador> jugadores = equipo.getPlantilla();
                bw.write("Equipo: " + equipo.getNombre());
                bw.newLine();
                for (Jugador j : jugadores) {
                    bw.write("Jugador: " + j.getNombre() + "|" + j.getDorsal() + "|" + j.getPosicion() + "|" + j.getMedia());
                    bw.newLine();
                }
                bw.write("---");
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Error intentando exportar los datos: " + ex.getMessage());
        }
    }

    public static List<Equipo> cargarEquipos(String nombreLiga) {
        List<Equipo> equipos = new ArrayList<>();
        File f = new File("ligas/" + nombreLiga + "/plantillas.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            Equipo equipoActual = null;

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Equipo: ")) {
                    String nombreEquipo = linea.substring(8).trim();
                    equipoActual = new Equipo(nombreEquipo);
                } else if (linea.startsWith("Jugador: ")) {
                    String datos = linea.substring(9);
                    String[] partes = datos.split("\\|");

                    String nombre = partes[0];
                    int dorsal = Integer.parseInt(partes[1]);
                    String descripcionPosicion = partes[2];
                    int media = Integer.parseInt(partes[3]);

                    Posicion posicion = null;

                    for (Posicion p : Posicion.values()) {
                        if (p.getDescripcion().equals(descripcionPosicion)) {
                            posicion = p;
                            break;
                        }
                    }

                    if (posicion == null) {
                        throw new IllegalArgumentException("Posición desconocida: " + descripcionPosicion);
                    }

                    Jugador jugador = new Jugador(nombre, dorsal, posicion, media);
                    equipoActual.getPlantilla().add(jugador);
                } else if (linea.equals("---")) {
                    if (equipoActual != null) {
                        equipos.add(equipoActual);
                        equipoActual = null;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error procesando los datos: " + e.getMessage());
        }

        return equipos;
    }

    public static void guardarClasificacion(List<Equipo> equipos, String nombreLiga) {
        File f = new File("ligas/" + nombreLiga + "/clasificacion.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            equipos.sort(Comparator.comparingInt(Equipo::getPuntos).reversed());

            int posicion = 1;
            bw.write(String.format("%-4s %-20s %-5s %-5s %-5s %-5s\n", "POS", "EQUIPO", "GF", "GC", "DIF", "PTS"));

            for (Equipo equipo : equipos) {
                bw.write(String.format(
                    "%-4d %-20s %-5d %-5d %-5d %-5d\n",
                    posicion++,
                    equipo.getNombre(),
                    equipo.getGolesFavor(),
                    equipo.getGolesContra(),
                    equipo.getDiferenciaGoles(),
                    equipo.getPuntos()
                ));
            }

        } catch (IOException e) {
            System.out.println("Error al guardar la clasificación: " + e.getMessage());
        }
    }

    public static void guardarResultados(Liga liga) {
        String nombreLiga = liga.getNombre();
        File f = new File("ligas/" + nombreLiga + "/resultados.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write("Jornada|EquipoLocal|GolesLocal|EquipoVisitante|GolesVisitante");
            bw.newLine();

            int numeroJornada = 1;

            for (List<Partido> jornada : liga.getCalendario()) {
                boolean jornadaTienePartidos = false;

                for (Partido partido : jornada) {
                    if (partido.isSimulado()) {
                        if (!jornadaTienePartidos) {
                            bw.write("Jornada " + numeroJornada);
                            bw.newLine();
                            jornadaTienePartidos = true;
                        }

                        bw.write(partido.getEquipoLocal().getNombre() + "|" +
                                 partido.getGolesLocal() + "|" +
                                 partido.getEquipoVisitante().getNombre() + "|" +
                                 partido.getGolesVisitante());
                        bw.newLine();
                    }
                }

                if (jornadaTienePartidos) {
                    bw.newLine();
                }

                numeroJornada++;
            }
        } catch (IOException ex) {
            System.out.println("Error intentando exportar los datos: " + ex.getMessage());
        }
    }

    public static void cargarResultados(Liga liga) {
        String nombreLiga = liga.getNombre();
        File f = new File("ligas/" + nombreLiga + "/resultados.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            int jornadaActual = -1;

            linea = br.readLine();

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Jornada")) {
                    String[] partes = linea.split(" ");
                    jornadaActual = Integer.parseInt(partes[1]) - 1;
                } else if (!linea.trim().isEmpty() && jornadaActual >= 0) {
                    String[] datos = linea.split("\\|");
                    if (datos.length == 4) {
                        String nombreLocal = datos[0];
                        int golesLocal = Integer.parseInt(datos[1]);
                        String nombreVisitante = datos[2];
                        int golesVisitante = Integer.parseInt(datos[3]);

                        List<Partido> jornada = liga.getCalendario().get(jornadaActual);
                        for (Partido p : jornada) {
                            if (p.getEquipoLocal().getNombre().equals(nombreLocal) &&
                                p.getEquipoVisitante().getNombre().equals(nombreVisitante)) {
                                p.setGolesLocal(golesLocal);
                                p.setGolesVisitante(golesVisitante);
                                p.setSimulado(true);
                                break;
                            }
                        }
                    }
                }
            }
            liga.actualizarClasificacionDesdeResultados();

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar resultados: " + e.getMessage());
        }
    }
}
