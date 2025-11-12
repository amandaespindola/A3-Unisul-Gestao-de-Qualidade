package principal;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import view.TelaLogin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Principal {

	private static final Logger logger = Logger.getLogger(Principal.class.getName());

	public static void main(String[] args) {
		try {
			FlatDarkLaf.setup();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Falha ao inicializar o tema FlatDarklaf", e);
		}

		String[] options = { "Claro", "Escuro" };
		int n = JOptionPane.showOptionDialog(null, "Escolha um tema", "SisUni", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		Runnable runTelaLogin = () -> {
			try {
				if (n != 1) {
					FlatLightLaf.setup();
				} else {
					FlatDarkLaf.setup();
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Falha ao inicializar o tema selecionado", e);
			}
			new TelaLogin().setVisible(true);
		};

		java.awt.EventQueue.invokeLater(runTelaLogin);
	}
}