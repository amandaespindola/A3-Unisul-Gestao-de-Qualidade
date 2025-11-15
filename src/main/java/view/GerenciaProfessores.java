package view;

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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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

import dao.ProfessorDAO;
import model.Professor;
import utils.ExcelExporter;
import utils.LookAndFeelHelper;
import utils.TableUtils;
import utils.ValidadorInput;
import utils.ViewUtils;

public class GerenciaProfessores extends JFrame {

	private JTable tabelaProf;
	private final transient ProfessorDAO professorDAO = new ProfessorDAO();
	private int linhaSelecionada = -1;

	public GerenciaProfessores() {
		initComponents();
		carregarTabela();

		TableUtils.addMouseClickListener(tabelaProf, this::mouseClickTabela);
	}

	private void initComponents() {

		setTitle("Gerência de Professores");
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
		JLabel lblTitulo = new JLabel("Cadastro de Professores", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		painelSuperior.add(lblTitulo);

		// ======================================================
		// BOTÕES — ALINHADOS COMO NO VÍDEO
		// ======================================================
		JPanel painelBotoes = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(0, 10, 0, 10);
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;

		// Larguras
		Dimension btnLateral = new Dimension(160, 30); // Atualizar / Exportar
		Dimension btnCentral = new Dimension(160, 30); // Cadastrar / Editar / Deletar

		// Botões
		ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/View/refresh.png"));
		Image img = refreshIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		refreshIcon = new ImageIcon(img);

		JButton bAtualizar = ViewUtils.criarBotao("Atualizar tabela", e -> carregarTabela());
		bAtualizar.setIcon(refreshIcon);
		bAtualizar.setHorizontalTextPosition(SwingConstants.RIGHT);

		JButton bCadastrar = ViewUtils.criarBotao("Cadastrar novo", e -> abrirCadastro());
		bCadastrar.setPreferredSize(btnCentral);

		JButton bEditar = ViewUtils.criarBotao("Editar", e -> editar());
		bEditar.setPreferredSize(btnCentral);

		JButton bDeletar = ViewUtils.criarBotao("Deletar", e -> deletar());
		bDeletar.setPreferredSize(btnCentral);

		JButton bExportar = ViewUtils.criarBotao("Exportar para Excel", e -> exportarExcel());
		bExportar.setPreferredSize(btnLateral);

		// ====== POSIÇÕES ======

		// --- Botão Atualizar (ESQUERDA)
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		painelBotoes.add(bAtualizar, gbc);

		// Botões centrais — juntos (insets menores)
		gbc.insets = new Insets(0, 5, 0, 5); 

		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridx = 1;
		painelBotoes.add(bCadastrar, gbc);

		gbc.gridx = 2;
		painelBotoes.add(bEditar, gbc);

		gbc.gridx = 3;
		painelBotoes.add(bDeletar, gbc);

		// --- Botão Exportar (DIREITA) — margem maior
		gbc.insets = new Insets(0, 15, 0, 15);
		gbc.gridx = 4;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		painelBotoes.add(bExportar, gbc);

		painelSuperior.add(painelBotoes);
		painel.add(painelSuperior, BorderLayout.NORTH);

		// ======================================================
		// TABELA
		// ======================================================
		tabelaProf = new JTable();
		tabelaProf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tabelaProf.setRowHeight(28);

		tabelaProf.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nome", "Idade", "Campus", "CPF", "Contato", "Título", "Salário" }));

		JScrollPane scroll = new JScrollPane(tabelaProf);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

		scroll.setPreferredSize(new Dimension(getWidth(), getHeight() - 170 // reduz o espaçamento para subir a tabela
		));
		painel.add(scroll, BorderLayout.CENTER);

		// ======================================================
		// MENU SUPERIOR
		// ======================================================
		JMenuBar menuBar = new JMenuBar();
		JMenu menuArquivo = new JMenu("Arquivo");

		JMenuItem menuAlunos = new JMenuItem("Gerência de Alunos");
		menuAlunos.addActionListener(e -> abrirAlunos());

		JMenuItem menuSobre = new JMenuItem("Sobre");
		menuSobre.addActionListener(e -> new Sobre().setVisible(true));

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(e -> System.exit(0));

		menuArquivo.add(menuAlunos);
		menuArquivo.add(menuSobre);
		menuArquivo.add(menuSair);

		menuBar.add(menuArquivo);
		setJMenuBar(menuBar);
	}

	// ============================================================
	// AÇÕES
	// ============================================================

	private void abrirCadastro() {
		try {
			new CadastroProfessor().setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao abrir cadastro: " + e.getMessage());
		}
	}

	private void editar() {
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(this, "Selecione um professor para editar.");
			return;
		}

		String[] dados = new String[8];

		dados[0] = tabelaProf.getValueAt(linhaSelecionada, 1).toString(); // nome
		dados[1] = tabelaProf.getValueAt(linhaSelecionada, 2).toString(); // idade
		dados[2] = tabelaProf.getValueAt(linhaSelecionada, 3).toString(); // campus
		dados[3] = tabelaProf.getValueAt(linhaSelecionada, 4).toString(); // cpf
		dados[4] = tabelaProf.getValueAt(linhaSelecionada, 5).toString(); // contato
		dados[5] = tabelaProf.getValueAt(linhaSelecionada, 6).toString(); // título
		dados[6] = ValidadorInput.removerMascara(tabelaProf.getValueAt(linhaSelecionada, 7).toString()); // salário
		dados[7] = tabelaProf.getValueAt(linhaSelecionada, 0).toString(); // id

		try {
			new EditarProfessor(dados).setVisible(true);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Erro ao abrir edição: " + ex.getMessage());
		}

		linhaSelecionada = -1;
	}

	private void deletar() {
		try {
			if (tabelaProf.getSelectedRow() == -1) {
				throw new Mensagens("Selecione um cadastro para deletar.");
			}

			int id = Integer.parseInt(tabelaProf.getValueAt(tabelaProf.getSelectedRow(), 0).toString());

			String[] opcoes = { "Sim", "Não" };
			int resp = JOptionPane.showOptionDialog(this, "Deseja realmente apagar?", "Confirmar Exclusão",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[1]);

			if (resp == 0) {
				if (professorDAO.delete(id)) {
					JOptionPane.showMessageDialog(this, "Professor deletado com sucesso.");
				} else {
					JOptionPane.showMessageDialog(this, "Erro ao deletar professor.");
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
			ExcelExporter.exportTableToExcel(tabelaProf);
			JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage());
		}
	}

	private void abrirAlunos() {
		new GerenciaAlunos().setVisible(true);
		dispose();
	}

	private void mouseClickTabela(java.awt.event.MouseEvent evt) {
		linhaSelecionada = tabelaProf.getSelectedRow();
	}

	// ============================================================
	// CARREGA TABELA
	// ============================================================

	private void carregarTabela() {
		DefaultTableModel modelo = (DefaultTableModel) tabelaProf.getModel();
		modelo.setNumRows(0);

		List<Professor> lista = professorDAO.getMinhaLista();
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		for (Professor p : lista) {
			modelo.addRow(new Object[] { p.getId(), p.getNome(), p.getIdade(), p.getCampus(), p.getCpf(),
					p.getContato(), p.getTitulo(), nf.format(p.getSalario()) });
		}
	}

	// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new GerenciaProfessores().setVisible(true));
	}
}
