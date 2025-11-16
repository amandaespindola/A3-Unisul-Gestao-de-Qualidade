package view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TelaLoginTest {

	String HEADLESS_MESSAGE = "Sem suporte gráfico — teste ignorado no CI.";

	@Test
	@DisplayName("TelaLogin deve ser instanciada sem lançar exceções")
	void testCriacaoTela() {
		assumeFalse(GraphicsEnvironment.isHeadless(), HEADLESS_MESSAGE);

		assertDoesNotThrow(TelaLogin::new);
	}

	@Test
	@DisplayName("Layout principal deve conter componentes essenciais")
	void testLayoutInicial() {
		assumeFalse(GraphicsEnvironment.isHeadless(), HEADLESS_MESSAGE);

		TelaLogin tela = new TelaLogin();

		Component[] comps = tela.getContentPane().getComponents();

		assertTrue(comps.length > 0, "A tela deve conter componentes.");
	}

	@Test
	@DisplayName("Título deve existir e estar configurado corretamente")
	void testTitulo() {
		assumeFalse(GraphicsEnvironment.isHeadless(), "Sem suporte gráfico — teste ignorado no CI.");

		TelaLogin tela = new TelaLogin();

		JLabel lbl = localizaPrimeiro(JLabel.class, tela);

		assertNotNull(lbl);
		assertEquals("SisUni - Sistema de Gerenciamento Universitário", lbl.getText());
		assertEquals(Font.BOLD, lbl.getFont().getStyle());
	}

	@Test
	@DisplayName("Campo de usuário deve existir e ter tamanhos definidos")
	void testCampoUsuario() {
		assumeFalse(GraphicsEnvironment.isHeadless(), HEADLESS_MESSAGE);

		TelaLogin tela = new TelaLogin();

		JTextField campo = localizaPrimeiro(JTextField.class, tela);

		assertNotNull(campo);
		assertEquals(320, campo.getPreferredSize().width);
		assertEquals(50, campo.getPreferredSize().height);
	}

	@Test
	@DisplayName("Campo de senha deve existir e ter tamanhos definidos")
	void testCampoSenha() {
		assumeFalse(GraphicsEnvironment.isHeadless(), HEADLESS_MESSAGE);

		TelaLogin tela = new TelaLogin();

		JPasswordField campo = localizaPrimeiro(JPasswordField.class, tela);

		assertNotNull(campo);
		assertEquals(320, campo.getPreferredSize().width);
		assertEquals(50, campo.getPreferredSize().height);
	}

	@Test
	@DisplayName("TelaLogin deve configurar tamanho mínimo corretamente")
	void testDimensoes() {
		assumeFalse(GraphicsEnvironment.isHeadless(), HEADLESS_MESSAGE);

		TelaLogin tela = new TelaLogin();

		Dimension min = tela.getMinimumSize();

		assertEquals(520, min.width);
		assertEquals(500, min.height);
	}

	// método auxiliar para evitar percorrer componentes à mão
	private <T> T localizaPrimeiro(Class<T> clazz, TelaLogin tela) {
		for (Component c : tela.getContentPane().getComponents()) {
			if (clazz.isInstance(c)) {
				return clazz.cast(c);
			}
			if (c instanceof JPanel panel) {
				for (Component sub : panel.getComponents()) {
					if (clazz.isInstance(sub)) {
						return clazz.cast(sub);
					}
				}
			}
		}
		return null;
	}
}