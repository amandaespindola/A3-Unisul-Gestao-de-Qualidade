package utils;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ViewUtils {

	private ViewUtils() {
		// Construtor privado para evitar instanciação
	}

	// Cria componentes de Menu
	public static JMenu criarMenu(String texto, String name) {
		JMenu menu = new JMenu(texto);
		menu.setName(name);
		return menu;
	}

	public static JMenuItem criarMenuItem(String texto, Consumer<ActionEvent> acao, String name) {
		JMenuItem item = new JMenuItem(texto);
		item.setName(name);
		item.addActionListener(acao::accept);
		return item;
	}

	public static JMenuBar criarMenuBar() {
		return new JMenuBar();
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

	public static JLabel criarLabel(String texto, String nome) {
		JLabel label = new JLabel(texto);
		label.setName(nome);
		return label;
	}

	public static JLabel criarLabelTitulo(String texto) {
		JLabel label = new JLabel(texto);
		label.setFont(new Font("Tahoma", Font.BOLD, 24));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
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

	// --------- Config + Builder para telas de cadastro ---------
	public static final class TelaCadastroConfig {

		private final JFrame tela;
		private final JLabel tituloLabel;
		private final String tituloTexto;
		private final JComboBox<?> combo1;
		private final JComboBox<?> combo2;
		private final JButton botaoCancelar;
		private final JButton botaoConfirmar;
		private final ActionListener acaoConfirmar;

		private TelaCadastroConfig(Builder b) {
			this.tela = b.tela;
			this.tituloLabel = b.tituloLabel;
			this.tituloTexto = b.tituloTexto;
			this.combo1 = b.combo1;
			this.combo2 = b.combo2;
			this.botaoCancelar = b.botaoCancelar;
			this.botaoConfirmar = b.botaoConfirmar;
			this.acaoConfirmar = b.acaoConfirmar;
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder {

			private JFrame tela;
			private JLabel tituloLabel;
			private String tituloTexto;
			private JComboBox<?> combo1;
			private JComboBox<?> combo2;
			private JButton botaoCancelar;
			private JButton botaoConfirmar;
			private ActionListener acaoConfirmar;

			public Builder tela(JFrame tela) {
				this.tela = tela;
				return this;
			}

			public Builder tituloLabel(JLabel tituloLabel) {
				this.tituloLabel = tituloLabel;
				return this;
			}

			public Builder tituloTexto(String tituloTexto) {
				this.tituloTexto = tituloTexto;
				return this;
			}

			public Builder combo1(JComboBox<?> combo1) {
				this.combo1 = combo1;
				return this;
			}

			public Builder combo2(JComboBox<?> combo2) {
				this.combo2 = combo2;
				return this;
			}

			public Builder botaoCancelar(JButton botaoCancelar) {
				this.botaoCancelar = botaoCancelar;
				return this;
			}

			public Builder botaoConfirmar(JButton botaoConfirmar) {
				this.botaoConfirmar = botaoConfirmar;
				return this;
			}

			public Builder acaoConfirmar(ActionListener acaoConfirmar) {
				this.acaoConfirmar = acaoConfirmar;
				return this;
			}

			public TelaCadastroConfig build() {
				// validações mínimas
				if (tela == null || tituloLabel == null || tituloTexto == null || botaoCancelar == null
						|| botaoConfirmar == null || acaoConfirmar == null) {
					throw new IllegalArgumentException("Parâmetros obrigatórios não informados em TelaCadastroConfig");
				}
				return new TelaCadastroConfig(this);
			}
		}
	}

	/**
	 * Método auxiliar comum para configurar telas de cadastro e edição, usado para
	 * eliminar duplicação entre configurarTelaCadastro e configurarTelaEdicao.
	 */
	private static void configurarTelaBase(TelaCadastroConfig cfg) {
		configurarTelaPadrao(cfg.tela, cfg.tituloLabel, cfg.tituloTexto);
		configurarBotoesPadrao(cfg.botaoCancelar, cfg.botaoConfirmar, cfg.tela, cfg.acaoConfirmar);

		if (cfg.combo1 != null && cfg.combo2 != null) {
			final String nomeClasse = cfg.tela.getClass().getSimpleName();
			if (nomeClasse.contains("Aluno")) {
				@SuppressWarnings("unchecked")
				JComboBox<String> c1 = (JComboBox<String>) cfg.combo1;
				@SuppressWarnings("unchecked")
				JComboBox<String> c2 = (JComboBox<String>) cfg.combo2;
				configurarCombosAluno(c1, c2);
			} else if (nomeClasse.contains("Professor")) {
				@SuppressWarnings("unchecked")
				JComboBox<String> c1 = (JComboBox<String>) cfg.combo1;
				@SuppressWarnings("unchecked")
				JComboBox<String> c2 = (JComboBox<String>) cfg.combo2;
				configurarCombosProfessor(c1, c2);
			}
		}
	}

	/**
	 * Configura uma tela de cadastro (Aluno/Professor) a partir de um objeto de
	 * configuração, reduzindo a contagem de parâmetros e evitando duplicação nas
	 * telas.
	 */
	public static void configurarTelaCadastro(TelaCadastroConfig cfg) {
		configurarTelaBase(cfg);

	}

	public static void configurarTelaEdicao(TelaCadastroConfig cfg) {
		configurarTelaBase(cfg);
	}

}
