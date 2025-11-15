package view;

import java.util.logging.Logger;
import java.util.logging.Level;
    
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.AlunoDAO;
import model.Aluno;
import utils.ExcelExporter;
import utils.LookAndFeelHelper;
import utils.TableUtils;
import utils.ValidadorInput;
import utils.ViewUtils;

public class GerenciaAlunos extends JFrame {

        private static final Logger LOGGER = Logger.getLogger(GerenciaAlunos.class.getName());

        
	private JTable jTableAlunos;
	private final transient AlunoDAO alunoDAO = new AlunoDAO();
	private int linhaSelecionada = -1;

	public GerenciaAlunos() {
		initComponents();
		carregarTabela();

		TableUtils.addMouseClickListener(jTableAlunos, this::jTableMouseClick);
	}

	private void initComponents() {

		setTitle("Gerência de Alunos");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1000, 520);
		setLocationRelativeTo(null);

		JPanel painel = new JPanel(new BorderLayout(10, 0));
		painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		add(painel, BorderLayout.CENTER);

		// ======================================================
		// PAINEL SUPERIOR (TÍTULO + BOTÕES)
		// ======================================================
		JPanel painelSuperior = new JPanel();
		painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.Y_AXIS));
		painelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// ====== TÍTULO ======
		JLabel lblTitulo = new JLabel("Cadastro de Alunos", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		painelSuperior.add(lblTitulo);

		// ======================================================
		// BOTÕES — EXATAMENTE COMO EM PROFESSORES
		// ======================================================
		JPanel painelBotoes = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(0, 10, 0, 10);
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;

		// Mesmos tamanhos usados no GerenciaProfessores
		Dimension btnLateral = new Dimension(160, 30);
		Dimension btnCentral = new Dimension(160, 30);

		// Botões
		JButton bAtualizar = ViewUtils.criarBotao("Atualizar tabela", e -> carregarTabela());
		JButton bCadastrar = ViewUtils.criarBotao("Cadastrar novo", e -> abrirCadastro());
		JButton bEditar = ViewUtils.criarBotao("Editar", e -> editar());
		JButton bDeletar = ViewUtils.criarBotao("Deletar", e -> deletar());
		JButton bExportar = ViewUtils.criarBotao("Exportar para Excel", e -> exportarExcel());

		// Ícone Atualizar — mesmo código do GerenciaProfessores
		try {
			ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/View/refresh.png"));
			Image img = refreshIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			refreshIcon = new ImageIcon(img);

			bAtualizar.setIcon(refreshIcon);
			bAtualizar.setHorizontalTextPosition(SwingConstants.RIGHT);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Ícone não encontrado", e);
		}

		// Tamanhos EXATAMENTE iguais
		bAtualizar.setPreferredSize(btnLateral);
		bExportar.setPreferredSize(btnLateral);

		bCadastrar.setPreferredSize(btnCentral);
		bEditar.setPreferredSize(btnCentral);
		bDeletar.setPreferredSize(btnCentral);

		// ====== POSICIONAMENTO DOS BOTÕES ======

		// --- Botão Atualizar (ESQUERDA)
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		painelBotoes.add(bAtualizar, gbc);

		// --- Botões centrais (juntos)
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridx = 1;
		painelBotoes.add(bCadastrar, gbc);

		gbc.gridx = 2;
		painelBotoes.add(bEditar, gbc);

		gbc.gridx = 3;
		painelBotoes.add(bDeletar, gbc);

		// --- Botão Exportar (DIREITA)
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 4;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		painelBotoes.add(bExportar, gbc);

		painelSuperior.add(painelBotoes);
		painel.add(painelSuperior, BorderLayout.NORTH);

		// ======================================================
		// TABELA — MESMO LAYOUT DO GERENCIA PROFESSORES
		// ======================================================
		jTableAlunos = new JTable();
		jTableAlunos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jTableAlunos.setRowHeight(28);

		jTableAlunos.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nome", "Idade", "Curso", "Fase" }));

		JScrollPane scroll = new JScrollPane(jTableAlunos);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

		// Mesma altura da tabela do GerenciaProfessores
		scroll.setPreferredSize(new Dimension(getWidth(), getHeight() - 170));

		painel.add(scroll, BorderLayout.CENTER);

		// ======================================================
		// MENU SUPERIOR
		// ======================================================
		JMenuBar menuBar = new JMenuBar();
		JMenu menuArquivo = new JMenu("Arquivo");

		JMenuItem menuProfessores = new JMenuItem("Gerência de Professores");
		menuProfessores.addActionListener(e -> abrirProfessores());

		JMenuItem menuSobre = new JMenuItem("Sobre");
		menuSobre.addActionListener(e -> new Sobre().setVisible(true));

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(e -> System.exit(0));

		menuArquivo.add(menuProfessores);
		menuArquivo.add(menuSobre);
		menuArquivo.add(menuSair);

		menuBar.add(menuArquivo);
		setJMenuBar(menuBar);
	}

	// -------------------------------------------------------------------------
	// AÇÕES
	// -------------------------------------------------------------------------

	private void abrirCadastro() {
		new CadastroAluno().setVisible(true);
	}

	private void editar() {
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(this, "Selecione um aluno para editar.");
			return;
		}

		String[] dados = new String[5];
		dados[0] = jTableAlunos.getValueAt(linhaSelecionada, 0).toString();
		dados[1] = jTableAlunos.getValueAt(linhaSelecionada, 1).toString();
		dados[2] = jTableAlunos.getValueAt(linhaSelecionada, 2).toString();
		dados[3] = jTableAlunos.getValueAt(linhaSelecionada, 3).toString();
		dados[4] = ValidadorInput.removerMascara(jTableAlunos.getValueAt(linhaSelecionada, 4).toString());

		new EditarAluno(dados).setVisible(true);
		linhaSelecionada = -1;
	}

	private void deletar() {
		try {
			if (jTableAlunos.getSelectedRow() == -1) {
				throw new Mensagens("Selecione um cadastro para deletar.");
			}

			int id = Integer.parseInt(jTableAlunos.getValueAt(jTableAlunos.getSelectedRow(), 0).toString());

			String[] opcoes = { "Sim", "Não" };
			int resp = JOptionPane.showOptionDialog(this, "Deseja apagar este cadastro?", "Confirmar Exclusão",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[1]);

			if (resp == 0) {
				if (alunoDAO.delete(id)) {
					JOptionPane.showMessageDialog(this, "Aluno deletado com sucesso.");
				} else {
					JOptionPane.showMessageDialog(this, "Erro ao deletar aluno.");
				}
			}

		} catch (Mensagens m) {
			JOptionPane.showMessageDialog(this, m.getMessage());
		} finally {
			carregarTabela();
		}
	}

	private void exportarExcel() {
		try {
			ExcelExporter.exportTableToExcel(jTableAlunos);
			JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage());
		}
	}

	private void abrirProfessores() {
		new GerenciaProfessores().setVisible(true);
		dispose();
	}

	private void jTableMouseClick(java.awt.event.MouseEvent evt) {
		linhaSelecionada = jTableAlunos.getSelectedRow();
	}

	// -------------------------------------------------------------------------
	// CARREGAR TABELA
	// -------------------------------------------------------------------------

	private void carregarTabela() {
		DefaultTableModel modelo = (DefaultTableModel) jTableAlunos.getModel();
		modelo.setNumRows(0);

		List<Aluno> lista = alunoDAO.getMinhaLista();

		for (Aluno a : lista) {
			modelo.addRow(new Object[] { a.getId(), a.getNome(), a.getIdade(), a.getCurso(), a.getFase() + "ª" });
		}
	}

	// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new GerenciaAlunos().setVisible(true));
	}
}
