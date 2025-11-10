package utils;

import javax.swing.UIManager;

public final class LookAndFeelHelper {

	private LookAndFeelHelper() {
		// Impede instanciação
	}

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
