package principal;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import view.TelaLogin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Principal {

	private static final Logger logger = Logger.getLogger(Principal.class.getName());

	public static void main(String[] args) {

		// 1) Mantém o JOptionPane no tema escuro
		try {
			FlatDarkLaf.setup();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Falha ao inicializar FlatDarkLaf", e);
		}

		// 2) Pede ao usuário qual tema usar na aplicação
		String[] options = { "Claro", "Escuro" };
		int escolha = JOptionPane.showOptionDialog(null, "Escolha um tema", "SisUni", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		// 3) Aplica o tema REAL da aplicação
		try {

			if (escolha == 0) { // MODO CLARO
				FlatLightLaf.setup();
				aplicarOverridesClaro(); // inputs brancos, texto preto, botão branco
			} else { // MODO ESCURO
				FlatDarkLaf.setup();
				aplicarOverridesEscuro(); // NÃO mexe nos inputs nem no botão
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Falha ao inicializar o tema selecionado", e);
		}

		// 4) Abre a TelaLogin
		java.awt.EventQueue.invokeLater(() -> new TelaLogin().setVisible(true));
	}

	// ========================
	// TEMA CLARO 
	// ========================
	private static void aplicarOverridesClaro() {

		// inputs brancos
		UIManager.put("TextField.background", java.awt.Color.WHITE);
		UIManager.put("PasswordField.background", java.awt.Color.WHITE);

		// texto preto
		UIManager.put("TextField.foreground", java.awt.Color.BLACK);
		UIManager.put("PasswordField.foreground", java.awt.Color.BLACK);

		// botão branco
		UIManager.put("Button.background", java.awt.Color.WHITE);
		UIManager.put("Button.foreground", java.awt.Color.BLACK);

		// borda fina
		UIManager.put("Component.focusWidth", 1);
	}

	// ========================
	// TEMA ESCURO
	// ========================
	private static void aplicarOverridesEscuro() {
		UIManager.put("Component.focusWidth", 1); // mantém borda azul fina
	}
}
