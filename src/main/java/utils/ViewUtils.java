package utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

/**
 * Classe utilitária com métodos auxiliares para criação e configuração de
 * componentes da interface gráfica (Swing), incluindo labels, botões, painéis,
 * menus, formatações e janelas padrão.
 *
 * <p>A classe não pode ser instanciada.</p>
 */
public class ViewUtils {

	/**
     * Construtor privado para impedir instanciação.
     */
	private ViewUtils() {
		// Construtor privado para evitar instanciação
	}

	private static final String DEFAULT_FONT = "Segoe UI";

	// -------------------
	// LABELS E TÍTULOS
	// -------------------

	/**
     * Cria um JLabel simples com texto e nome de identificação.
     *
     * @param texto texto a ser exibido
     * @param nome nome interno do componente
     * @return JLabel configurado
     */
	public static JLabel criarLabel(String texto, String nome) {
		JLabel label = new JLabel(texto);
		label.setName(nome);
		return label;
	}

	/**
     * Cria um JLabel utilizado como título de seção, com fonte maior e negrito.
     *
     * @param texto texto do título
     * @return JLabel estilizado
     */
	public static JLabel criarLabelTitulo(String texto) {
		JLabel label = new JLabel(texto);
		label.setFont(new Font(DEFAULT_FONT, Font.BOLD, 24));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	// -------------------
	// BOTÕES
	// -------------------

	/**
     * Cria um JButton que executa uma ação ao ser clicado.
     *
     * @param texto texto do botão
     * @param acao função executada no clique
     * @return JButton configurado
     */
	public static JButton criarBotao(String texto, Consumer<ActionEvent> acao) {
		JButton botao = new JButton(texto);
		botao.addActionListener(acao::accept);
		return botao;
	}

	// -------------------
	// JANELA PADRÃO
	// -------------------

	/**
     * Configura propriedades padrão para uma janela Swing.
     *
     * @param frame janela a ser configurada
     * @param titulo título da janela
     */
	public static void configurarJanela(JFrame frame, String titulo) {
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle(titulo);
		frame.setResizable(false);
	}

	// -------------------
	// ERROS
	// -------------------

	/**
     * Exibe mensagens amigáveis em casos de erro durante cadastros.
     *
     * @param ex exceção capturada
     */
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

	/**
     * Fecha uma janela se o evento tiver origem em um botão de cancelamento.
     *
     * @param source objeto que disparou a ação
     * @param frame janela a ser fechada
     */
	public static void fecharJanelaAoCancelar(Object source, JFrame frame) {
		if (source instanceof JButton) {
			frame.dispose();
		}
	}

	/**
     * Adiciona um JLabel ao formulário usando GridBagConstraints.
     *
     * @param form painel alvo
     * @param gbc configuração do GridBag
     * @param x posição X
     * @param y posição Y
     * @param texto texto do label
     * @param name nome interno do componente
     */
	public static void addLabel(JPanel form, GridBagConstraints gbc, int x, int y, String texto, String name) {
		gbc.gridx = x;
		gbc.gridy = y;
		form.add(ViewUtils.criarLabel(texto, name), gbc);
	}

	/**
     * Adiciona um componente ao formulário com GridBagLayout.
     *
     * @param form painel alvo
     * @param gbc configurações do GridBag
     * @param x posição X
     * @param y posição Y
     * @param gridWidth largura ocupada
     * @param componente componente a adicionar
     */
	public static void addCampo(JPanel form, GridBagConstraints gbc, int x, int y, int gridWidth,
			JComponent componente) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gridWidth;
		form.add(componente, gbc);
		gbc.gridwidth = 1; // reset
	}

	/**
     * Cria o título principal da tela, centralizado e em fonte grande.
     *
     * @param texto texto do título
     * @return JLabel estilizado
     */
	public static JLabel criarTituloTela(String texto) {
		JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
		lbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, 36));
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		return lbl;
	}

	/**
     * Cria painel superior contendo o título da tela.
     *
     * @param texto texto do título
     * @return painel configurado
     */
	public static JPanel criarPainelSuperiorTitulo(String texto) {
		JPanel painelSuperior = new JPanel();
		painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
		painelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		painelSuperior.add(criarTituloTela(texto));
		return painelSuperior;
	}

	/**
     * Cria o painel de botões padrão de gerenciamento:
     * Atualizar, Cadastrar, Editar, Deletar e Exportar.
     *
     * @param btnAtualizar botão atualizar
     * @param btnCadastrar botão cadastrar
     * @param btnEditar botão editar
     * @param btnDeletar botão deletar
     * @param btnExportar botão exportar
     * @return painel configurado com os botões
     */
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

	/**
     * Cria um menu padrão de gerenciamento contendo:
     * - item principal
     * - Sobre
     * - Sair
     *
     * @param itemPrincipalTexto texto do item principal
     * @param acaoPrincipal ação do item principal
     * @param acaoSobre ação da opção "Sobre"
     * @return JMenuBar configurada
     */
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

	/**
     * Adiciona botões de confirmar e cancelar em um painel.
     * Define também o botão confirmar como default da janela.
     *
     * @param painel painel de destino
     * @param acaoConfirmar ação de confirmação
     * @param acaoCancelar ação de cancelamento
     * @param rootPane rootPane da janela
     */
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

	/**
     * Aplica formatação aos campos de CPF, contato e salário,
     * exibindo alerta caso ocorra erro.
     *
     * @param parent componente pai para exibir mensagem
     * @param cpf campo de CPF
     * @param contato campo de contato
     * @param salario campo de salário
     */
	public static void aplicarFormatacaoProfessorComAlerta(Component parent, JFormattedTextField cpf,
			JFormattedTextField contato, JFormattedTextField salario) {
		try {
			ValidadorInput.aplicarFormatacaoProfessor(cpf, contato, salario);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(parent, "Erro ao formatar campos.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
     * Cria o painel superior completo da tela de gerenciamento,
     * contendo título e botões.
     *
     * @param titulo título da tela
     * @param bAtualizar botão atualizar
     * @param bCadastrar botão cadastrar
     * @param bEditar botão editar
     * @param bDeletar botão deletar
     * @param bExportar botão exportar
     * @return painel configurado
     */
	public static JPanel criarPainelGerenciaTopo(String titulo, JButton bAtualizar, JButton bCadastrar, JButton bEditar,
			JButton bDeletar, JButton bExportar) {

		JPanel painelSuperior = criarPainelSuperiorTitulo(titulo);

		JPanel painelBotoes = criarPainelBotoesGerencia(bAtualizar, bCadastrar, bEditar, bDeletar, bExportar);

		painelSuperior.add(painelBotoes);
		return painelSuperior;
	}

	/**
     * Cria o painel base (central) de uma janela.
     *
     * @param frame janela principal
     * @return painel configurado
     */
	public static JPanel criarPainelBase(JFrame frame) {
		JPanel painel = new JPanel(new BorderLayout(10, 0));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		frame.add(painel, BorderLayout.CENTER);
		return painel;
	}

	/**
     * Cria os cinco botões padrão de gerenciamento, aplicando ícones
     * quando disponíveis.
     *
     * @param atualizar ação do botão atualizar
     * @param cadastrar ação do botão cadastrar
     * @param editar ação do botão editar
     * @param deletar ação do botão deletar
     * @param exportar ação do botão exportar
     * @param logger logger para avisos caso ícones não sejam encontrados
     * @return vetor contendo os botões criados
     */
	public static JButton[] criarBotoesGerencia(Runnable atualizar, Runnable cadastrar, Runnable editar,
			Runnable deletar, Runnable exportar, Logger logger) {

		JButton bAtualizar = criarBotao("Atualizar tabela", e -> atualizar.run());
		JButton bCadastrar = criarBotao("Cadastrar novo", e -> cadastrar.run());
		JButton bEditar = criarBotao("Editar", e -> editar.run());
		JButton bDeletar = criarBotao("Deletar", e -> deletar.run());
		JButton bExportar = criarBotao("Exportar para Excel", e -> exportar.run());

		try {
			ImageIcon refreshIcon = new ImageIcon(ViewUtils.class.getResource("/View/refresh.png"));
			Image img = refreshIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			bAtualizar.setIcon(new ImageIcon(img));
			bAtualizar.setHorizontalTextPosition(SwingConstants.RIGHT);
		} catch (Exception e) {
			logger.warning("Ícone não encontrado: " + e.getMessage());
		}

		return new JButton[] { bAtualizar, bCadastrar, bEditar, bDeletar, bExportar };
	}

}
