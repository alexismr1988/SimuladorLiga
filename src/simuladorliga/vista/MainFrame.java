package simuladorliga.vista;

import javax.swing.*;
import simuladorliga.modelo.*;
import simuladorliga.persistencia.GestorBD;

/**
 * Ventana principal del simulador de ligas. 
 * Gestiona el cambio de paneles entre menú, creación y gestión de liga.
 * Inicia y configura la conexión a la base de datos.
 */
public class MainFrame extends JFrame{
    
    private Liga liga = null;
    
    /**
    * Constructor del frame principal.
    * Establece título, tamaño, posición, conexión con base de datos 
    * y panel inicial de menú.
    */
    public MainFrame(){
        setTitle("Simulador Ligas Virtuales SLV");        
        setSize(1000,800);       
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GestorBD gestor = new GestorBD();
        gestor.inicializaBD();
        setContentPane(new MenuPanel(this));
        
        setVisible(true);
    }

    /**
    * Devuelve la liga que, actualmente, está cargada en memoria.
    * 
    * @return Objeto Liga activo
    */
    public Liga getLiga() {
        return liga;
    }
    
    /**
     * Establece la liga actualmente activa en el frame.
     * 
     * @param liga Objeto Liga a usar en la aplicación
     */
    public void setLiga(Liga liga) {
        this.liga = liga;
    }
    
    /**
    * Cambia el contenido del frame al panel de menú principal.
    */
    public void irAMenuPanel() {
        setContentPane(new MenuPanel(this));
        revalidate();
        repaint();
    }
    
    /**
    * Cambia el contenido del frame al panel de gestión de liga.
    */
    public void irAGestionPanel() {
        setContentPane(new GestionPanel(this));
        revalidate();
        repaint();
    }

    /**
     * Cambia el contenido del frame al panel de creación de nueva liga.
     */    
    public void irACrearPanel() {
        setContentPane(new CrearPanel(this));
        revalidate();
        repaint();
    }
    
}
