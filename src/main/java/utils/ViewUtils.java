package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	public static void configurarTelaPadrao(JFrame frame, JLabel labelTitulo, String tituloJanela) {
		configurarJanela(frame, tituloJanela);
		configurarTitulo(labelTitulo, tituloJanela);
	}

	public static void configurarBotoesPadrao(JButton bCancelar, JButton bConfirmar, JFrame frame,
			ActionListener acaoConfirmar) {
		configurarBotaoCancelar(bCancelar, frame);
		configurarBotaoConfirmar(bConfirmar, acaoConfirmar::actionPerformed);
	}

	public static void configurarCombosAluno(JComboBox<String> comboCurso, JComboBox<String> comboFase) {
		comboCurso.setModel(new DefaultComboBoxModel<>(utils.Constantes.CURSOS));
		comboCurso.setName("");
		String[] fasesFormatadas = java.util.Arrays.stream(utils.Constantes.FASES).mapToObj(f -> f + "ª")
				.toArray(String[]::new);
		comboFase.setModel(new DefaultComboBoxModel<>(fasesFormatadas));
	}

	public static void configurarCombosProfessor(JComboBox<String> comboCampus, JComboBox<String> comboTitulo) {
		comboCampus.setModel(new DefaultComboBoxModel<>(utils.Constantes.CAMPUS));
		comboCampus.setName("");
		comboTitulo.setModel(new DefaultComboBoxModel<>(utils.Constantes.TITULOS));
	}

	public static void tratarErroCadastro(Exception ex) {
		if (ex instanceof NumberFormatException) {
			JOptionPane.showMessageDialog(null, "Informe um número válido.");
		} else {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void fecharJanelaAoCancelar(Object source, JFrame frame) {
		if (source instanceof JButton) {
			frame.dispose();
		}
	}

	public static void atualizarTabela(Runnable acaoCarregar) {
		acaoCarregar.run();
	}

	public static void exportarDados(Runnable acaoExportar) {
		try {
			acaoExportar.run();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Erro ao exportar: " + ex.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void configurarBotoesGerencia(JButton refresh, JMenuItem menuRefresh, JMenuItem menuExport,
			Runnable acaoExportar, Runnable acaoAtualizar) {
		refresh.addActionListener(e -> acaoAtualizar.run());
		menuRefresh.addActionListener(e -> acaoAtualizar.run());
		menuExport.addActionListener(e -> acaoExportar.run());
	}

}
