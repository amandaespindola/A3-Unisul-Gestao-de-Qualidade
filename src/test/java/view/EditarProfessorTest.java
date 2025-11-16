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
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Constantes;

class EditarProfessorTest {
	@BeforeAll
	static void configurarAmbiente() {
		System.setProperty("java.awt.headless", "false");
	}

	// -------------------------------------------------------
	// Utilitário para acessar campos privados
	// -------------------------------------------------------
	private Object acessarCampo(Object instancia, String nomeCampo) {
		try {
			Field f = instancia.getClass().getDeclaredField(nomeCampo);
			f.setAccessible(true);
			return f.get(instancia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------
	// Utilitário para chamar métodos privados
	// -------------------------------------------------------
	private void chamarMetodo(Object instancia, String nomeMetodo) {
		try {
			Method m = instancia.getClass().getDeclaredMethod(nomeMetodo);
			m.setAccessible(true);
			m.invoke(instancia);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------
	// TESTES
	// -------------------------------------------------------

	@Test
	@DisplayName("A janela EditarProfessor deve ser criada sem lançar exceções")
	void testCriacaoJanela() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		assertDoesNotThrow(() -> {
			EditarProfessor tela = new EditarProfessor();
			assertNotNull(tela);
		});
	}

	@Test
	@DisplayName("O formulário principal deve existir após initComponents()")
	void testFormularioExiste() throws Exception {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarProfessor[] janela = new EditarProfessor[1];
		EventQueue.invokeAndWait(() -> janela[0] = new EditarProfessor());

		EditarProfessor tela = janela[0];

		Container content = tela.getContentPane();
		assertEquals(1, content.getComponentCount(), "Conteúdo raiz deve ter 1 painel principal.");

		JPanel painel = (JPanel) content.getComponent(0);
		assertNotNull(painel);

		// BorderLayout.CENTER = índice 1
		Component form = painel.getComponent(1);
		assertTrue(form instanceof JPanel, "O formulário deve ser um JPanel.");
	}

	@Test
	@DisplayName("Campos devem ser criados corretamente")
	void testCamposCriados() throws Exception {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarProfessor[] janela = new EditarProfessor[1];
		EventQueue.invokeAndWait(() -> janela[0] = new EditarProfessor());

		EditarProfessor tela = janela[0];

		assertNotNull(acessarCampo(tela, "nome"));
		assertNotNull(acessarCampo(tela, "campus"));
		assertNotNull(acessarCampo(tela, "titulo"));
		assertNotNull(acessarCampo(tela, "cpfFormatado"));
		assertNotNull(acessarCampo(tela, "contatoFormatado"));
		assertNotNull(acessarCampo(tela, "salarioFormatado"));
		assertNotNull(acessarCampo(tela, "idade"));
	}

	@Test
	@DisplayName("preencherCampos() deve preencher os campos corretamente")
	void testPreencherCampos() throws Exception {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarProfessor[] janela = new EditarProfessor[1];

		// CAMPUS E TÍTULO DEVEM EXISTIR NAS LISTAS DO Constantes
		String campusValido = Constantes.getCampus().get(0);
		String tituloValido = Constantes.getTitulos().get(0);

		String[] dados = { "Amanda", // nome
				"30", // idade
				campusValido, // campus
				"12345678901", // cpf
				"48999999999", // contato
				tituloValido, // título
				"8500.50", // salário
				"42" // id
		};

		EventQueue.invokeAndWait(() -> janela[0] = new EditarProfessor(dados));

		EditarProfessor tela = janela[0];

		JTextField nome = (JTextField) acessarCampo(tela, "nome");
		JTextField idade = (JTextField) acessarCampo(tela, "idade");
		JComboBox<?> campus = (JComboBox<?>) acessarCampo(tela, "campus");
		JFormattedTextField cpf = (JFormattedTextField) acessarCampo(tela, "cpfFormatado");
		JFormattedTextField contato = (JFormattedTextField) acessarCampo(tela, "contatoFormatado");
		JComboBox<?> titulo = (JComboBox<?>) acessarCampo(tela, "titulo");
		JFormattedTextField salario = (JFormattedTextField) acessarCampo(tela, "salarioFormatado");

		assertEquals("Amanda", nome.getText());
		assertEquals("30", idade.getText());
		assertEquals(campusValido, campus.getSelectedItem());

		// remove tudo que não for número antes de comparar
		assertEquals("12345678901", cpf.getText().replaceAll("\\D", ""));
		assertEquals("48999999999", contato.getText().replaceAll("\\D", ""));

		assertEquals(tituloValido, titulo.getSelectedItem());
		assertEquals(8500.50, salario.getValue());
	}

	@Test
	@DisplayName("cancelar() deve fechar a janela sem lançar exceção")
	void testCancelar() throws Exception {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado no CI.");

		final EditarProfessor[] janela = new EditarProfessor[1];
		EventQueue.invokeAndWait(() -> janela[0] = new EditarProfessor());

		EditarProfessor tela = janela[0];

		assertDoesNotThrow(() -> chamarMetodo(tela, "cancelar"));
	}

}