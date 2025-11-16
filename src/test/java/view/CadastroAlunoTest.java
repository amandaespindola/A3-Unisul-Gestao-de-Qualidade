package view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CadastroAlunoTest {

	@Test
	@DisplayName("Tela deve ser criada sem lançar exceções")
	void testCriacaoTela() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

		assertDoesNotThrow(() -> {
			CadastroAluno tela = new CadastroAluno();
			assertNotNull(tela);
		});
	}

	@Test
	@DisplayName("Componentes principais devem existir na tela")
	void testComponentesPrincipais() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

		CadastroAluno tela = new CadastroAluno();

		Component root = tela.getContentPane().getComponent(0);
		assertTrue(root instanceof JPanel);

		JPanel painel = (JPanel) root;

		Component centro = painel.getComponent(1);
		assertTrue(centro instanceof JPanel);

		JPanel form = (JPanel) centro;

		// procura nome
		JTextField nome = (JTextField) findField(tela, "nome");
		assertNotNull(nome);
		assertEquals(20, nome.getColumns());

		JComboBox<?> curso = (JComboBox<?>) findField(tela, "curso");
		assertNotNull(curso);
		assertTrue(curso.getItemCount() > 0);

		JComboBox<?> fase = (JComboBox<?>) findField(tela, "fase");
		assertNotNull(fase);
		assertTrue(fase.getItemCount() > 0);

		Object idade = findField(tela, "idade");
		assertNotNull(idade);
	}

	@Test
	@DisplayName("Botões de confirmar/cancelar devem ser adicionados")
	void testBotoesConfirmarCancelar() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

		CadastroAluno tela = new CadastroAluno();

		JPanel raiz = (JPanel) tela.getContentPane().getComponent(0);

		assertEquals(BorderLayout.class, raiz.getLayout().getClass());

		Component sul = raiz.getComponent(2);
		assertTrue(sul instanceof JPanel);

		JPanel painelBotoes = (JPanel) sul;
		assertEquals(2, painelBotoes.getComponentCount());
	}

	// helper para acessar campos privados da tela
	private Object findField(CadastroAluno obj, String name) {
		try {
			Field f = CadastroAluno.class.getDeclaredField(name);
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {
			fail("Campo não encontrado: " + name);
			return null;
		}
	}
}