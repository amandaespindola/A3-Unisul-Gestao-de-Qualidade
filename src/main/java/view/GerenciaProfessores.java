package view;

import dao.ProfessorDAO;
import model.Professor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.table.DefaultTableModel;
import utils.ExcelExporter;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;
import utils.TableUtils;
import java.util.List;

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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// ====== Título ======
		JLabel lblTitulo = ViewUtils.criarLabelTitulo("Gerência de Professores");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
		add(lblTitulo, BorderLayout.NORTH);

		// ====== Tabela ======
		tabelaProf = new JTable();
		tabelaProf.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nome", "Idade", "Campus", "CPF", "Contato", "Título", "Salário" }));

		JScrollPane scroll = new JScrollPane(tabelaProf);
		add(scroll, BorderLayout.CENTER);

		// ====== Painel de Botões ======
		JPanel botoes = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));

		JButton bCadastrar = ViewUtils.criarBotao("Cadastrar", e -> abrirCadastro());
		JButton bEditar = ViewUtils.criarBotao("Editar", e -> editar());
		JButton bDeletar = ViewUtils.criarBotao("Deletar", e -> deletar());
		JButton bRefresh = ViewUtils.criarBotao("Atualizar", e -> carregarTabela());
		JButton bExportar = ViewUtils.criarBotao("Exportar Excel", e -> exportarExcel());

		botoes.add(bCadastrar);
		botoes.add(bEditar);
		botoes.add(bDeletar);
		botoes.add(bRefresh);
		botoes.add(bExportar);

		add(botoes, BorderLayout.SOUTH);

		// ====== Menu ======
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
