package utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

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

	public static void addLabel(JPanel form, GridBagConstraints gbc, int x, int y, String texto, String name) {
		gbc.gridx = x;
		gbc.gridy = y;
		form.add(ViewUtils.criarLabel(texto, name), gbc);
	}

	public static void addCampo(JPanel form, GridBagConstraints gbc, int x, int y, int gridWidth,
			JComponent componente) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gridWidth;
		form.add(componente, gbc);
		gbc.gridwidth = 1; // reset
	}

	public static JLabel criarTituloTela(String texto) {
		JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
		lbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, 36));
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		return lbl;
	}

	public static JPanel criarPainelSuperiorTitulo(String texto) {
		JPanel painelSuperior = new JPanel();
		painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
		painelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		painelSuperior.add(criarTituloTela(texto));
		return painelSuperior;
	}

	public static JPanel criarPainelBotoesGerencia(JButton btnAtualizar, JButton btnCadastrar, JButton btnEditar,
			JButton btnDeletar, JButton btnExportar) {
		JPanel painelBotoes = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;

		Dimension btnLateral = new Dimension(160, 30);
		Dimension btnCentral = new Dimension(160, 30);

		btnAtualizar.setPreferredSize(btnLateral);
		btnExportar.setPreferredSize(btnLateral);

		btnCadastrar.setPreferredSize(btnCentral);
		btnEditar.setPreferredSize(btnCentral);
		btnDeletar.setPreferredSize(btnCentral);

		// ==== ESQUERDA (Atualizar) ====
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		painelBotoes.add(btnAtualizar, gbc);

		// ==== CENTRO ====
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridx = 1;
		painelBotoes.add(btnCadastrar, gbc);
		gbc.gridx = 2;
		painelBotoes.add(btnEditar, gbc);
		gbc.gridx = 3;
		painelBotoes.add(btnDeletar, gbc);

		// ==== DIREITA (Exportar) ====
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 4;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		painelBotoes.add(btnExportar, gbc);

		return painelBotoes;
	}

	public static JMenuBar criarMenuGerencia(String itemPrincipalTexto, Runnable acaoPrincipal, Runnable acaoSobre) {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuArquivo = new JMenu("Arquivo");

		JMenuItem itemPrincipal = new JMenuItem(itemPrincipalTexto);
		itemPrincipal.addActionListener(e -> acaoPrincipal.run());

		JMenuItem itemSobre = new JMenuItem("Sobre");
		itemSobre.addActionListener(e -> acaoSobre.run());

		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.addActionListener(e -> System.exit(0));

		menuArquivo.add(itemPrincipal);
		menuArquivo.add(itemSobre);
		menuArquivo.add(itemSair);

		menuBar.add(menuArquivo);
		return menuBar;
	}

	public static void adicionarBotoesConfirmarCancelar(JPanel painel, Runnable acaoConfirmar, Runnable acaoCancelar,
			JRootPane rootPane) {
		JButton bConfirmar = criarBotao(Constantes.UIConstants.BTN_CONFIRMAR, e -> acaoConfirmar.run());
		JButton bCancelar = criarBotao(Constantes.UIConstants.BTN_CANCELAR, e -> acaoCancelar.run());

		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		botoes.add(bConfirmar);
		botoes.add(bCancelar);

		painel.add(botoes, BorderLayout.SOUTH);
		rootPane.setDefaultButton(bConfirmar);
	}

	public static void aplicarFormatacaoProfessorComAlerta(Component parent, JFormattedTextField cpf,
			JFormattedTextField contato, JFormattedTextField salario) {
		try {
			ValidadorInput.aplicarFormatacaoProfessor(cpf, contato, salario);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(parent, "Erro ao formatar campos.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static JPanel criarPainelGerenciaTopo(String titulo, JButton bAtualizar, JButton bCadastrar, JButton bEditar,
			JButton bDeletar, JButton bExportar) {

		JPanel painelSuperior = criarPainelSuperiorTitulo(titulo);

		JPanel painelBotoes = criarPainelBotoesGerencia(bAtualizar, bCadastrar, bEditar, bDeletar, bExportar);

		painelSuperior.add(painelBotoes);
		return painelSuperior;
	}

}
