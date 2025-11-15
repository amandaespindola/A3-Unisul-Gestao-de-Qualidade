package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.TableUtils;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;

import utils.ExcelExporter;
import utils.LookAndFeelHelper;

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
import java.util.List;
import java.awt.FlowLayout;

public class GerenciaAlunos extends JFrame {

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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// Título
		JLabel lblTitulo = ViewUtils.criarLabelTitulo("Gerência de Alunos");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
		add(lblTitulo, BorderLayout.NORTH);

		// Tabela
		jTableAlunos = new JTable();
		jTableAlunos.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nome", "Idade", "Curso", "Fase" }));
		JScrollPane scroll = new JScrollPane(jTableAlunos);
		add(scroll, BorderLayout.CENTER);

		// Painel de botões
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

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

		// Menu
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
