/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package simuladorliga.vista;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import simuladorliga.controlador.GestorTablaResultados;
import simuladorliga.persistencia.GestorBD;
import simuladorliga.servicio.Simulador;
import simuladorliga.modelo.*;

/**
 * Panel de gestión de una liga.
 * Permite simular jornadas, consultar clasificación, plantillas y partidos,
 * modificar presupuestos y realizar traspasos entre equipos.
 * 
 * Se conecta con la base de datos mediante GestorBD y usa el simulador para
 * generar los resultados.
 */
public class GestionPanel extends javax.swing.JPanel {

    private MainFrame frame;
    
    
    /**
    * Constructor del panel de gestión.
    * Inicializa los combos, tabla y configuración visual.
    * 
    * @param frame Referencia al frame principal.
    */
    public GestionPanel(MainFrame frame) {
        initComponents();
        this.frame = frame;    
        GestorBD gestor = new GestorBD();
        int idLiga = gestor.obtenerIdLigaPorNombre(frame.getLiga().getNombre());
        this.nombreLigaLabel.setText("Gestión de Liga: " + frame.getLiga().getNombre());
        nombreLigaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        List<Integer> numJornadas = gestor.obtenerJornadasDeLiga(idLiga);
        tablaResultados.setModel(GestorTablaResultados.modeloClasificacion(frame.getLiga().getEquipos()));
        
        // Ajustes visuales de la tabla
        tablaResultados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaResultados.setRowHeight(24);

        // Cambiar fuente del encabezado
        tablaResultados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Centrado del contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaResultados.getColumnCount(); i++) {
            tablaResultados.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        boolean dark = UIManager.getLookAndFeel().getName().toLowerCase().contains("dark");

        tablaResultados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                        ? (dark ? new Color(55, 55, 55) : Color.WHITE)
                        : (dark ? new Color(65, 65, 65) : new Color(240, 240, 255)));
                }
                return c;
            }
        });


        
        for (Integer jornada : numJornadas) {
            comboNumJornada.addItem(jornada.toString());
        }
        for (Integer jornada : numJornadas) {
            comboJornadas.addItem(jornada.toString());
        }
        List<String> nombresEquipos = this.frame.getLiga().obtenerNombreEquipos();
        for (String nombre : nombresEquipos) {
        comboPlantillas.addItem(nombre);
        }
        for (String nombre : nombresEquipos) {
        comboEquiposPresupuesto.addItem(nombre);
        }
        
        for (String nombre : nombresEquipos) {
        comboEquiposOrigen.addItem(nombre);
        }
        String nombreEquipoTraspaso = comboEquiposOrigen.getSelectedItem().toString();
        Equipo equipoTraspaso = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipoTraspaso);
        List<String> nombresJugadores = equipoTraspaso.obtenerNombreJugadores();
        comboEquiposOrigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String nombreEquipoTraspaso = comboEquiposOrigen.getSelectedItem().toString();
                    Equipo equipoTraspaso = frame.getLiga().buscarEquipoPorNombre(nombreEquipoTraspaso);

                    comboJugadores.removeAllItems(); // limpia los anteriores

                    for (String nombre : equipoTraspaso.obtenerNombreJugadores()) {
                        comboJugadores.addItem(nombre);
                    }
                }
            }
        });
        
        for (String nombre : nombresJugadores) {
        comboJugadores.addItem(nombre);
        }
        for (String nombre : nombresEquipos) {
        comboEquiposDestino.addItem(nombre);
        }

        
         
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        simularLabel = new javax.swing.JLabel();
        comboNumJornada = new javax.swing.JComboBox<>();
        clasificacionLabel = new javax.swing.JLabel();
        plantillaLabel = new javax.swing.JLabel();
        comboPlantillas = new javax.swing.JComboBox<>();
        partidosLabel = new javax.swing.JLabel();
        comboJornadas = new javax.swing.JComboBox<>();
        botonVolver = new javax.swing.JButton();
        botonSimular = new javax.swing.JButton();
        botonClasificacion = new javax.swing.JButton();
        botonPlantilla = new javax.swing.JButton();
        botonPartidos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaResultados = new javax.swing.JTable();
        nombreLigaLabel = new javax.swing.JLabel();
        equiposLabel = new javax.swing.JLabel();
        botonEquipos = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        presupuestoLabel = new javax.swing.JLabel();
        comboEquiposPresupuesto = new javax.swing.JComboBox<>();
        campoCantidad = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        botonAnadir = new javax.swing.JButton();
        botonRestar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        origenLabel = new javax.swing.JLabel();
        comboEquiposOrigen = new javax.swing.JComboBox<>();
        jugadorTraspasoLabel = new javax.swing.JLabel();
        comboJugadores = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        comboEquiposDestino = new javax.swing.JComboBox<>();
        botonTraspasar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1000, 800));

        simularLabel.setText("Simular Jornada");

        clasificacionLabel.setText("Mostrar clasificación");

        plantillaLabel.setText("Mostrar plantilla");

        partidosLabel.setText("Partidos por Jornada");

        botonVolver.setText("VOLVER");
        botonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVolverActionPerformed(evt);
            }
        });

        botonSimular.setText("Simular");
        botonSimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSimularActionPerformed(evt);
            }
        });

        botonClasificacion.setText("Mostrar");
        botonClasificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonClasificacionActionPerformed(evt);
            }
        });

        botonPlantilla.setText("Mostrar");
        botonPlantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlantillaActionPerformed(evt);
            }
        });

        botonPartidos.setText("Mostrar");
        botonPartidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPartidosActionPerformed(evt);
            }
        });

        tablaResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaResultados);

        equiposLabel.setText("Mostrar equipos");

        botonEquipos.setText("Mostrar");
        botonEquipos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEquiposActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        presupuestoLabel.setText("Gestionar presupuesto");

        jLabel1.setText("Selecciona el equipo");

        jLabel2.setText("Cantidad:");

        botonAnadir.setText("+ Añadir");
        botonAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAnadirActionPerformed(evt);
            }
        });

        botonRestar.setText("- Restar");
        botonRestar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRestarActionPerformed(evt);
            }
        });

        jLabel3.setText("Realizar traspaso");

        origenLabel.setText("Equipo de origen");

        comboEquiposOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEquiposOrigenActionPerformed(evt);
            }
        });

        jugadorTraspasoLabel.setText("Jugador a traspasar");

        jLabel4.setText("Equipo de destino");

        botonTraspasar.setText("Aceptar");
        botonTraspasar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTraspasarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(equiposLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(partidosLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(plantillaLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clasificacionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(simularLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(comboNumJornada, javax.swing.GroupLayout.Alignment.LEADING, 0, 121, Short.MAX_VALUE)
                                            .addComponent(comboPlantillas, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(comboJornadas, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(botonSimular, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                                            .addComponent(botonPartidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(botonPlantilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(botonClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(65, 65, 65)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(148, 148, 148)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jugadorTraspasoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(origenLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(presupuestoLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(comboEquiposPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(campoCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(botonRestar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(botonAnadir, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(139, 139, 139))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(comboEquiposOrigen, 0, 94, Short.MAX_VALUE)
                                                    .addComponent(comboJugadores, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(comboEquiposDestino, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(botonTraspasar)
                                                .addGap(0, 0, Short.MAX_VALUE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(botonEquipos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(139, 139, 139)
                                        .addComponent(nombreLigaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(botonVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(nombreLigaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(presupuestoLabel)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(simularLabel)
                                .addComponent(comboNumJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonSimular))
                            .addGap(35, 35, 35)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(clasificacionLabel)
                                .addComponent(botonClasificacion))
                            .addGap(31, 31, 31)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(equiposLabel)
                                .addComponent(botonEquipos))
                            .addGap(37, 37, 37)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(partidosLabel)
                                .addComponent(comboJornadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonPartidos))
                            .addGap(44, 44, 44)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboPlantillas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botonPlantilla)
                                .addComponent(plantillaLabel)))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(comboEquiposPresupuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonAnadir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(campoCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonRestar))
                        .addGap(34, 34, 34)
                        .addComponent(jLabel3)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(origenLabel)
                            .addComponent(comboEquiposOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jugadorTraspasoLabel)
                            .addComponent(comboJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonTraspasar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(comboEquiposDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botonVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
    * Simula los partidos de la jornada seleccionada.
    * Aplica la lógica de simulación y guarda los resultados en la base de datos.
    */
    private void botonSimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSimularActionPerformed
        // TODO add your handling code here:
        int numJornada = Integer.parseInt(comboNumJornada.getSelectedItem().toString());
        GestorBD gestor = new GestorBD();
        int idLiga = gestor.obtenerIdLigaPorNombre(frame.getLiga().getNombre());
        Simulador simulador = new Simulador(frame.getLiga());
        List<Partido> partidosJornada = gestor.obtenerPartidosDeJornada(idLiga, numJornada);
              
        boolean yaSimulada = partidosJornada.stream().allMatch(Partido::isSimulado);
        if (yaSimulada) {
            JOptionPane.showMessageDialog(this, "La jornada ya está simulada.");
            return;
        }
        StringBuilder resultados = new StringBuilder();
        
        for (Partido partido : partidosJornada) {
            if (!partido.isSimulado()) {
                simulador.simularPartido(partido);

                // Actualiza en la base de datos:
                gestor.updatePartido(
                    partido.getIdPartido(),               // ¡Asegúrate que Partido tiene este campo!
                    partido.getGolesLocal(),
                    partido.getGolesVisitante(),
                    partido.isSimulado(),
                    partido.getJornada(),
                    partido.getEquipoLocal().getId(),
                    partido.getEquipoVisitante().getId()
                );

                resultados.append(
                    partido.getEquipoLocal().getNombre() + " " + partido.getGolesLocal() +
                    " - " + partido.getEquipoVisitante().getNombre() + " " + partido.getGolesVisitante() + "\n"
                );
            }
        }

        JOptionPane.showMessageDialog(this, "Jornada simulada:\n" + resultados.toString());
        frame.setLiga(gestor.recuperarLigaCompleta(idLiga));
        frame.getLiga().actualizarClasificacionDesdeResultados(); 
        tablaResultados.setModel(GestorTablaResultados.modeloClasificacion(frame.getLiga().getEquipos()));

    }//GEN-LAST:event_botonSimularActionPerformed

    /**
    * Regresa al menú principal desde el panel de gestión.
    */
    private void botonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVolverActionPerformed
        // TODO add your handling code here:
        frame.irAMenuPanel();
    }//GEN-LAST:event_botonVolverActionPerformed

    /**
    * Muestra la clasificación actualizada en la tabla.
    */
    private void botonClasificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonClasificacionActionPerformed
        // TODO add your handling code here:
        tablaResultados.setModel(GestorTablaResultados.modeloClasificacion(frame.getLiga().getEquipos()));
    }//GEN-LAST:event_botonClasificacionActionPerformed

    /**
    * Muestra información general de los equipos en la tabla.
    */
    private void botonEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEquiposActionPerformed
        // TODO add your handling code here:
        tablaResultados.setModel(GestorTablaResultados.modeloParaEquipos(frame.getLiga().getEquipos()));
    }//GEN-LAST:event_botonEquiposActionPerformed

    /**
    * Muestra la plantilla del equipo seleccionado en el combo correspondiente.
    */
    private void botonPlantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlantillaActionPerformed
        // TODO add your handling code here:
        String nombreEquipo = comboPlantillas.getSelectedItem().toString();
        Equipo equipoSeleccionado = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipo);
        tablaResultados.setModel(GestorTablaResultados.modeloParaPlantillas(equipoSeleccionado));
    }//GEN-LAST:event_botonPlantillaActionPerformed

    /**
    * Muestra los partidos correspondientes a la jornada seleccionada.
    */
    private void botonPartidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPartidosActionPerformed
        // TODO add your handling code here:
        int numJornada = Integer.parseInt(comboJornadas.getSelectedItem().toString());
        GestorBD gestor = new GestorBD();
        int idLiga = gestor.obtenerIdLigaPorNombre(frame.getLiga().getNombre());
        List<Partido> partidosJornada = gestor.obtenerPartidosDeJornada(idLiga, numJornada);
        tablaResultados.setModel(GestorTablaResultados.modeloJornadas(partidosJornada));


    }//GEN-LAST:event_botonPartidosActionPerformed

    /**
    * Añade una cantidad al presupuesto del equipo seleccionado y actualiza en la base de datos.
    */
    private void botonAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAnadirActionPerformed
        // TODO add your handling code here:
        String texto = campoCantidad.getText().trim();
        String nombreEquipo = comboEquiposPresupuesto.getSelectedItem().toString();
        Equipo equipoSeleccionado = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipo);
        GestorBD gestor = new GestorBD();
        
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una cantidad");
            return;
        }
        
        try {
            double cantidad = Double.parseDouble(texto);
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser positiva");
                return;
            }         
            equipoSeleccionado.incrementarPresupuesto(cantidad);
            gestor.updatePresupuestoEquipo(equipoSeleccionado.getId(), equipoSeleccionado.getPresupuesto());
            JOptionPane.showMessageDialog(this, "Presupuesto actualizado");
            campoCantidad.setText("");
            tablaResultados.setModel(GestorTablaResultados.modeloParaEquipos(frame.getLiga().getEquipos()));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introduce una cantidad numérica válida");
        }
        

    }//GEN-LAST:event_botonAnadirActionPerformed

    /**
    * Resta una cantidad al presupuesto del equipo seleccionado y actualiza en la base de datos.
    */
    private void botonRestarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRestarActionPerformed
        // TODO add your handling code here:
        String texto = campoCantidad.getText().trim();
        String nombreEquipo = comboEquiposPresupuesto.getSelectedItem().toString();
        Equipo equipoSeleccionado = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipo);
        GestorBD gestor = new GestorBD();
        
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce una cantidad");
            return;
        }
        
        try {
            double cantidad = Double.parseDouble(texto);
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser positiva");
                return;
            }
            equipoSeleccionado.reducirPresupuesto(cantidad);
            gestor.updatePresupuestoEquipo(equipoSeleccionado.getId(), equipoSeleccionado.getPresupuesto());
            JOptionPane.showMessageDialog(this, "Presupuesto actualizado");
            campoCantidad.setText("");
            tablaResultados.setModel(GestorTablaResultados.modeloParaEquipos(frame.getLiga().getEquipos()));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introduce una cantidad numérica válida");
        }
    }//GEN-LAST:event_botonRestarActionPerformed

    /**
    * Realiza el traspaso de un jugador desde un equipo origen a un equipo destino.
    * Actualiza tanto en memoria como en la base de datos.
    */
    private void botonTraspasarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTraspasarActionPerformed
        // TODO add your handling code here:
        if (comboEquiposOrigen.getSelectedItem() == null ||
            comboEquiposDestino.getSelectedItem() == null ||
            comboJugadores.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar equipo origen, destino y jugador");
            return;
        }

        String nombreEquipoOrigen = comboEquiposOrigen.getSelectedItem().toString();
        String nombreEquipoDestino = comboEquiposDestino.getSelectedItem().toString();
        String nombreJugador = comboJugadores.getSelectedItem().toString();
        GestorBD gestor = new GestorBD();
        
        Equipo equipoOrigen = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipoOrigen);
        Equipo equipoDestino = this.frame.getLiga().buscarEquipoPorNombre(nombreEquipoDestino);
        Jugador jugadorSeleccionado = equipoOrigen.obtenerJugadorPorNombre(nombreJugador);
        int idEquipoDestino = equipoDestino.getId();
        int idJugador = jugadorSeleccionado.getId();
        
        if (nombreEquipoOrigen.equalsIgnoreCase(nombreEquipoDestino)) {
                JOptionPane.showMessageDialog(this, "No puede ser el mismo equipo el de origen y el de destino");
                return;
            }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas traspasar a " + nombreJugador + " al equipo " + nombreEquipoDestino + "?",
            "Confirmar traspaso",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        
        equipoOrigen.getPlantilla().remove(jugadorSeleccionado);
        equipoDestino.agregarJugador(jugadorSeleccionado);
        gestor.actualizarIdJugador(idJugador, idEquipoDestino);
        JOptionPane.showMessageDialog(this, "Traspaso realizado correctamente");
        
        comboJugadores.removeAllItems();
            for (Jugador j : equipoOrigen.getPlantilla()) {
                comboJugadores.addItem(j.getNombre());
        }
        tablaResultados.setModel(GestorTablaResultados.modeloParaPlantillas(equipoDestino));
        
    }//GEN-LAST:event_botonTraspasarActionPerformed

    private void comboEquiposOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEquiposOrigenActionPerformed
        // TODO add your handling code here

    }//GEN-LAST:event_comboEquiposOrigenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAnadir;
    private javax.swing.JButton botonClasificacion;
    private javax.swing.JButton botonEquipos;
    private javax.swing.JButton botonPartidos;
    private javax.swing.JButton botonPlantilla;
    private javax.swing.JButton botonRestar;
    private javax.swing.JButton botonSimular;
    private javax.swing.JButton botonTraspasar;
    private javax.swing.JButton botonVolver;
    private javax.swing.JTextField campoCantidad;
    private javax.swing.JLabel clasificacionLabel;
    private javax.swing.JComboBox<String> comboEquiposDestino;
    private javax.swing.JComboBox<String> comboEquiposOrigen;
    private javax.swing.JComboBox<String> comboEquiposPresupuesto;
    private javax.swing.JComboBox<String> comboJornadas;
    private javax.swing.JComboBox<String> comboJugadores;
    private javax.swing.JComboBox<String> comboNumJornada;
    private javax.swing.JComboBox<String> comboPlantillas;
    private javax.swing.JLabel equiposLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel jugadorTraspasoLabel;
    private javax.swing.JLabel nombreLigaLabel;
    private javax.swing.JLabel origenLabel;
    private javax.swing.JLabel partidosLabel;
    private javax.swing.JLabel plantillaLabel;
    private javax.swing.JLabel presupuestoLabel;
    private javax.swing.JLabel simularLabel;
    private javax.swing.JTable tablaResultados;
    // End of variables declaration//GEN-END:variables
}
