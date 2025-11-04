package Principal;

import View.TelaLogin;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        String[] options = {"Claro", "Escuro"};
        int n = JOptionPane.showOptionDialog(null, "Escolha um tema", "SisUni", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if (n != 1){
            java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FlatLightLaf.setup();
                } catch (Exception e){
                    e.printStackTrace();
                }
                new TelaLogin().setVisible(true);
            }
            });
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FlatDarkLaf.setup();
                } catch (Exception e){
                    e.printStackTrace();
                }
                new TelaLogin().setVisible(true);
            }
            });
        }
    }
}