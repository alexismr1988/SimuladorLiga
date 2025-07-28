package simuladorliga.servicio;

import simuladorliga.modelo.*;
import simuladorliga.persistencia.*;
import simuladorliga.vista.*;
import com.formdev.flatlaf.FlatDarkLaf; 
import java.awt.Font;
import javax.swing.UIManager;
import simuladorliga.util.ConfigBD;

/**
 * Clase principal del simulador de liga.
 * Configura el aspecto visual (FlatLaf) e inicia la interfaz gráfica.
 */
public class Main {
    
    /**
     * Punto de entrada del programa.
     * Aplica el tema visual y lanza el frame principal.
     * 
     * @param args argumentos de la línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); 
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Component.arc", 10);      
            UIManager.put("Button.arc", 15);         
            UIManager.put("TextComponent.arc", 10);  
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf");
        }

        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
