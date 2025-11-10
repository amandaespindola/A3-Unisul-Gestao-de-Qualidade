package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class ViewUtils {
	private ViewUtils() {
		// Construtor privado para evitar instanciação
	}

	public static void configurarJanela(JFrame frame, String titulo) {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle(titulo);
		frame.setResizable(false);
	}

	public static void configurarTitulo(JLabel label, String texto) {
		label.setFont(new Font("Tahoma", Font.BOLD, 24));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(texto);
	}

	public static void configurarBotaoCancelar(JButton botao, JFrame tela) {
		botao.setText("Cancelar");
		botao.addActionListener(evt -> tela.dispose());
	}

	public static void configurarBotaoConfirmar(JButton botao, Consumer<ActionEvent> acao) {
		botao.setText("Confirmar");
		botao.setToolTipText("ENTER");
		botao.addActionListener(acao::accept);
	}

	public static JMenuItem criarMenuItem(String texto, int tecla, Consumer<ActionEvent> acao) {
		JMenuItem item = new JMenuItem(texto);
		item.setAccelerator(KeyStroke.getKeyStroke(tecla, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(acao::accept);
		return item;
	}

	public static JButton criarBotao(String texto, Consumer<ActionEvent> acao) {
		JButton botao = new JButton(texto);
		botao.addActionListener(acao::accept);
		return botao;
	}
}
