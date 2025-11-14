package utils;

import javax.swing.UIManager;

/**
 * Utilitário para aplicação do tema Nimbus no Swing.
 * <p>
 * Esta classe fornece um método estático que tenta aplicar o Look and Feel
 * Nimbus, caso esteja disponível no sistema. Não pode ser instanciada.
 */
public final class LookAndFeelHelper {

        /**
        * Construtor privado para impedir instanciação.
        */
	private LookAndFeelHelper() {
		// Impede instanciação
	}

        /**
        * Aplica o Look and Feel Nimbus, caso esteja instalado.
        * <p>
        * Se o Nimbus não estiver disponível ou ocorrer alguma falha na aplicação
        * do tema, a exceção é capturada silenciosamente.
        */
	public static void aplicarNimbus() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			// Log opcional ou silencioso
		}
	}
}
