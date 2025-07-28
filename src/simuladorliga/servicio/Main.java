package simuladorliga.servicio;

import simuladorliga.modelo.*;
import simuladorliga.persistencia.*;
import simuladorliga.vista.*;
import com.formdev.flatlaf.FlatDarkLaf; 
import java.awt.Font;
import javax.swing.UIManager;

public class Main {
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
