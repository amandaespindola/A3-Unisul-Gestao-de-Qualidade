package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

		JPanel painel = ViewUtils.criarPainelBase(this);

		JButton[] btns = ViewUtils.criarBotoesGerencia(this::carregarTabela, this::abrirCadastro, this::editar,
				this::deletar, this::exportarExcel, LOGGER);
		JPanel painelSuperior = ViewUtils.criarPainelGerenciaTopo("Cadastro de Alunos", btns[0], btns[1], btns[2],
				btns[3], btns[4]);
		painel.add(painelSuperior, BorderLayout.NORTH);

		jTableAlunos = new JTable();
		jTableAlunos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jTableAlunos.setRowHeight(28);
		jTableAlunos.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Nome", "Idade", "Curso", "Fase" }));

		JScrollPane scroll = new JScrollPane(jTableAlunos);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		scroll.setPreferredSize(new Dimension(getWidth(), getHeight() - 170));

		painel.add(scroll, BorderLayout.CENTER);

		setJMenuBar(ViewUtils.criarMenuGerencia("Gerência de Professores", this::abrirProfessores,
				() -> new Sobre().setVisible(true)));

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
