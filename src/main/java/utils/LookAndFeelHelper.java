package utils;

import javax.swing.UIManager;

/**
 * Classe utilitária responsável por aplicar o Look and Feel "Nimbus"
 * à interface gráfica da aplicação. <br><br>
 *
 * A classe é final para evitar herança e possui um construtor privado
 * para impedir instância, já que sua função é apenas disponibilizar
 * métodos utilitários estáticos.
 */
public final class LookAndFeelHelper {

	/**
     * Construtor privado para impedir a criação de instâncias desta classe.
     * Como se trata de uma classe utilitária, todos os métodos são estáticos.
     */
	private LookAndFeelHelper() {
		// Impede instanciação
	}

	/**
     * Aplica o Look and Feel Nimbus à interface Swing, caso ele esteja disponível
     * no ambiente atual. <br><br>
     *
     * O método percorre a lista de Look and Feels instalados no sistema e,
     * ao encontrar o "Nimbus", configura-o como o padrão da aplicação. <br>
     *
     * Caso ocorra qualquer exceção (por exemplo, Nimbus não disponível ou erro
     * ao aplicar), o método simplesmente ignora silenciosamente, permitindo que
     * a aplicação continue com o Look and Feel padrão.
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
