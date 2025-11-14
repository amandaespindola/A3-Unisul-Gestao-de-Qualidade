package principal;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import view.TelaLogin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * Classe principal responsável por inicializar o sistema e configurar o tema visual
 * utilizando a biblioteca FlatLaf. Após a escolha do tema pelo usuário, a tela de
 * login é exibida.
 */
public class Principal {

        /** Logger utilizado para registrar erros durante a inicialização da aplicação. */
	private static final Logger logger = Logger.getLogger(Principal.class.getName());

        /**
        * Ponto de entrada da aplicação.
        * <p>
        * Este método configura inicialmente o tema escuro como padrão e, em seguida,
        * exibe um diálogo permitindo ao usuário escolher entre o tema claro e o tema
        * escuro. Após a seleção, a interface é inicializada no Event Dispatch Thread
        * (EDT) e a tela de login é apresentada.
        * </p>
        *
        * @param args Parâmetros da linha de comando (não utilizados).
        */
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