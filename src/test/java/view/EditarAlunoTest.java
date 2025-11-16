package view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EditarAlunoTest {
	@BeforeAll
	static void configurarAmbiente() {
		// Xvfb exige headless = false
		System.setProperty("java.awt.headless", "false");
	}

	// ------------------------------------------------------------------
	// UTILITÁRIOS (iguais ao seu padrão)
	// ------------------------------------------------------------------

	private Object acessarCampo(Object instancia, String nomeCampo) {
		try {
			Field f = instancia.getClass().getDeclaredField(nomeCampo);
			f.setAccessible(true);
			return f.get(instancia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void chamarMetodo(Object instancia, String metodo) {
		try {
			Method m = instancia.getClass().getDeclaredMethod(metodo);
			m.setAccessible(true);
			m.invoke(instancia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// ------------------------------------------------------------------
	// TESTES
	// ------------------------------------------------------------------

	@Test
	@DisplayName("EditarAluno deve ser criado sem lançar exceções")
	void testCriacaoJanela() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		assertDoesNotThrow(() -> {
			EditarAluno tela = new EditarAluno();
			assertNotNull(tela);
		});
	}

	@Test
	@DisplayName("Formulário principal deve existir após initComponents()")
	void testFormularioExiste() throws InvocationTargetException, InterruptedException {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarAluno[] janela = new EditarAluno[1];
		EventQueue.invokeAndWait(() -> janela[0] = new EditarAluno());

		EditarAluno tela = janela[0];

		Container content = tela.getContentPane();
		assertEquals(1, content.getComponentCount());

		JPanel painel = (JPanel) content.getComponent(0);
		assertNotNull(painel);

		// O formulário está no BorderLayout.CENTER ⇒ componente índice 1
		Component form = painel.getComponent(1);
		assertTrue(form instanceof JPanel, "O formulário deve ser um JPanel.");
	}

	@Test
	@DisplayName("Campos nome, idade, curso e fase devem ser criados corretamente")
	void testCamposCriados() throws InvocationTargetException, InterruptedException {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarAluno[] janela = new EditarAluno[1];
		EventQueue.invokeAndWait(() -> janela[0] = new EditarAluno());

		EditarAluno tela = janela[0];

		JTextField nome = (JTextField) acessarCampo(tela, "nome");
		JTextField idade = (JTextField) acessarCampo(tela, "idade");
		JComboBox<?> curso = (JComboBox<?>) acessarCampo(tela, "curso");
		JComboBox<?> fase = (JComboBox<?>) acessarCampo(tela, "fase");

		assertNotNull(nome);
		assertNotNull(idade);
		assertNotNull(curso);
		assertNotNull(fase);

		assertEquals("", nome.getText());
		assertEquals("", idade.getText());
	}

	@Test
	@DisplayName("preencherCampos() deve preencher os valores corretamente")
	void testPreencherCampos() throws InvocationTargetException, InterruptedException {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarAluno[] janela = new EditarAluno[1];

		String[] dados = { "10", // ID
				"Amanda", // Nome
				"21", // Idade
				"Sistemas de Informação", // Curso
				"5" // Fase
		};

		EventQueue.invokeAndWait(() -> janela[0] = new EditarAluno(dados));

		EditarAluno tela = janela[0];

		JTextField nome = (JTextField) acessarCampo(tela, "nome");
		JTextField idade = (JTextField) acessarCampo(tela, "idade");
		JComboBox<String> curso = (JComboBox<String>) acessarCampo(tela, "curso");
		JComboBox<Integer> fase = (JComboBox<Integer>) acessarCampo(tela, "fase");

		assertEquals("Amanda", nome.getText());
		assertEquals("21", idade.getText());

		assertEquals("Sistemas de Informação", curso.getSelectedItem());
		assertEquals(Integer.valueOf(5), fase.getSelectedItem());
	}

	@Test
	@DisplayName("Cancelar deve fechar a janela sem exceções")
	void testCancelar() throws InvocationTargetException, InterruptedException {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarAluno[] janela = new EditarAluno[1];

		EventQueue.invokeAndWait(() -> janela[0] = new EditarAluno());

		EditarAluno tela = janela[0];

		assertDoesNotThrow(() -> chamarMetodo(tela, "cancelar"));
	}

}