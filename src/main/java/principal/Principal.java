package principal;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import view.TelaLogin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Classe principal da aplicação SisUni.
 * <p>
 * Responsável por inicializar o tema visual (claro ou escuro),
 * aplicar personalizações de UI via {@link UIManager} e abrir a
 * tela inicial {@link TelaLogin}.
 * </p>
 */
public class Principal {

	/** Logger utilizado para registrar erros de inicialização e configuração de tema. */
	private static final Logger logger = Logger.getLogger(Principal.class.getName());

	/**
     * Ponto de entrada da aplicação.
     * <p> Este método:
     * <ol>
     *   <li>Inicializa o tema padrão (escuro) para diálogos como {@link JOptionPane};</li>
     *   <li>Solicita ao usuário a escolha entre tema claro ou escuro;</li>
     *   <li>Aplica customizações visuais via {@link UIManager};</li>
     *   <li>Abre a tela de login..</li>
     * </ol>
     *
     * @param args Parâmetros padrão de linha de comando (não utilizados).
     */
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
	
	/**
     * Aplica personalizações visuais específicas do tema claro.
     * <p>
     * Define fundos brancos para inputs, texto preto e botões claros.
     * </p>
     */
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
	
	/**
     * Aplica customizações para o tema escuro.
     * <p>
     * Mantém apenas ajustes mínimos, como a largura da borda de foco.
     * </p>
     */
	private static void aplicarOverridesEscuro() {
		UIManager.put("Component.focusWidth", 1); // mantém borda azul fina
	}
}
