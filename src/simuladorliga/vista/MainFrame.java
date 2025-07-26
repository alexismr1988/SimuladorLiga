package simuladorliga.vista;

import javax.swing.*;
import simuladorliga.modelo.*;


public class MainFrame extends JFrame{
    
    private Liga liga = null;
    
    public MainFrame(){
        setTitle("Simulador Ligas Virtuales SLV");
        setSize(1000,800);       
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setContentPane(new MenuPanel(this));
        
        setVisible(true);
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }
    
    public void irAMenuPanel() {
        setContentPane(new MenuPanel(this));
        revalidate();
        repaint();
    }
    
    public void irAGestionPanel() {
        setContentPane(new GestionPanel(this));
        revalidate();
        repaint();
    }
    
}
