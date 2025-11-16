package utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.swing.UIManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LookAndFeelHelperTest {

	@Test
	@DisplayName("aplicarNimbus não deve lançar exceções")
	void testAplicarNimbusNaoLanca() {
		assertDoesNotThrow(LookAndFeelHelper::aplicarNimbus);
	}

	@Test
	@DisplayName("aplicarNimbus deve manter um LookAndFeel válido")
	void testLookAndFeelValido() {
		assertDoesNotThrow(LookAndFeelHelper::aplicarNimbus);

		assertNotNull(javax.swing.UIManager.getLookAndFeel());
	}

	@Test
	@DisplayName("Construtor privado deve existir e ser privado")
	void testConstructorPrivate() throws Exception {
		var constructor = LookAndFeelHelper.class.getDeclaredConstructor();

		// Deve ser private
		assertFalse(constructor.canAccess(null));

		constructor.setAccessible(true);

		Object instance = constructor.newInstance();
		assertNotNull(instance);
		assertEquals(LookAndFeelHelper.class, instance.getClass());
	}

	@Test
	@DisplayName("aplicarNimbus deve capturar exceções ao tentar carregar Nimbus inválido (caminho catch)")
	void testAplicarNimbusCaminhoCatch() {

		UIManager.LookAndFeelInfo[] fakeInfos = {
				new UIManager.LookAndFeelInfo("Nimbus", "classe.inexistente.NimbusFake") };

		// Substitui os LookAndFeels instalados por um inválido
		UIManager.setInstalledLookAndFeels(fakeInfos);

		// NÃO pode lançar erro: deve passar pelo catch
		assertDoesNotThrow(LookAndFeelHelper::aplicarNimbus);
	}
}
