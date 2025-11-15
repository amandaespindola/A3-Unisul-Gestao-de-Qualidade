package utils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class ViewUtils {

	private ViewUtils() {
		// Construtor privado para evitar instanciação
	}

	private static final String DEFAULT_FONT = "Segoe UI";

	// -------------------
	// LABELS E TÍTULOS
	// -------------------

	public static JLabel criarLabel(String texto, String nome) {
		JLabel label = new JLabel(texto);
		label.setName(nome);
		return label;
	}

	public static JLabel criarLabelTitulo(String texto) {
		JLabel label = new JLabel(texto);
		label.setFont(new Font(DEFAULT_FONT, Font.BOLD, 24));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	// -------------------
	// BOTÕES
	// -------------------

	public static JButton criarBotao(String texto, Consumer<ActionEvent> acao) {
		JButton botao = new JButton(texto);
		botao.addActionListener(acao::accept);
		return botao;
	}

	// -------------------
	// JANELA PADRÃO
	// -------------------

	public static void configurarJanela(JFrame frame, String titulo) {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle(titulo);
		frame.setResizable(false);
	}

	// -------------------
	// ERROS
	// -------------------

	public static void tratarErroCadastro(Exception ex) {
		if (ex instanceof NumberFormatException) {
			JOptionPane.showMessageDialog(null, "Informe um número válido.");
		} else {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	// -------------------
	// CANCELAR / FECHAR
	// -------------------

	public static void fecharJanelaAoCancelar(Object source, JFrame frame) {
		if (source instanceof JButton) {
			frame.dispose();
		}
	}

}
