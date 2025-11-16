package view;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class TelaPrincipalTest {
	// Não colocar headless aqui, Xvfb precisa de display real
	// @BeforeAll não define headless no Xvfb

	// Helper para chamar métodos privados
	private void callPrivate(Object obj, String methodName) throws Exception {
		Method m = obj.getClass().getDeclaredMethod(methodName);
		m.setAccessible(true);
		m.invoke(obj);
	}

	@Test
	void testConstrutorCriaComponentes() {
		TelaPrincipal tela = new TelaPrincipal();

		Assertions.assertNotNull(tela);
		Assertions.assertEquals("SisUni - Principal", tela.getTitle());
		Assertions.assertFalse(tela.isResizable());
	}

	@Test
	void testBotoesExistem() {
		TelaPrincipal tela = new TelaPrincipal();

		JButton btnAlunos = getButtonByText(tela, "Alunos");
		JButton btnProfessores = getButtonByText(tela, "Professores");

		Assertions.assertNotNull(btnAlunos);
		Assertions.assertNotNull(btnProfessores);
	}

	// Busca botão pelo texto (navegando pela árvore de componentes)
	private JButton getButtonByText(JFrame tela, String texto) {
		for (Component c : tela.getContentPane().getComponents()) {

			if (c instanceof JButton btn && texto.equals(btn.getText())) {
				return btn;
			}

			if (c instanceof JComponent jc) {
				for (Component sub : jc.getComponents()) {
					if (sub instanceof JButton btn && texto.equals(btn.getText())) {
						return btn;
					}
				}
			}
		}
		return null;
	}

	@Test
	void testAbrirAlunosChamaDispose() throws Exception {

		try (MockedConstruction<GerenciaAlunos> mocked = mockConstruction(GerenciaAlunos.class)) {

			TelaPrincipal tela = spy(new TelaPrincipal());

			doNothing().when(tela).dispose(); // evita fechar janela real

			callPrivate(tela, "abrirAlunos");

			Assertions.assertEquals(1, mocked.constructed().size());
			verify(tela, times(1)).dispose();
		}
	}

	@Test
	void testAbrirProfessoresChamaDispose() throws Exception {

		try (MockedConstruction<GerenciaProfessores> mocked = mockConstruction(GerenciaProfessores.class)) {

			TelaPrincipal tela = spy(new TelaPrincipal());
			doNothing().when(tela).dispose();

			callPrivate(tela, "abrirProfessores");

			Assertions.assertEquals(1, mocked.constructed().size());
			verify(tela, times(1)).dispose();
		}
	}

	@Test
	void testMainNaoLancaErro() {
		Assertions.assertDoesNotThrow(() -> {
			TelaPrincipal.main(new String[] {});
		});
	}

}