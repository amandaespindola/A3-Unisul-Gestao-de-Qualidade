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

/**
 * Tela de gerenciamento de alunos do sistema.
 * Permite visualizar, cadastrar, editar, excluir e exportar dados de alunos para Excel.
 *
 * A classe utiliza uma tabela Swing (JTable) e integra com {@link dao.AlunoDAO}
 * para carregar e manipular os registros armazenados no banco.
 *
 * A interface oferece ações rápidas como:
 * <ul>
 *   <li>Abrir formulário de cadastro</li>
 *   <li>Editar aluno selecionado</li>
 *   <li>Excluir aluno</li>
 *   <li>Exportar para Excel</li>
 *   <li>Navegar para a janela de gerenciamento de professores</li>
 * </ul>
 *
 * Esta janela é criada com dimensões fixas e layout baseado em BorderLayout.
 */
public class GerenciaAlunos extends JFrame {

	/** Logger utilizado para registrar ações e erros da interface. */
	private static final Logger LOGGER = Logger.getLogger(GerenciaAlunos.class.getName());

	/** Tabela onde os alunos são exibidos. */
	private JTable jTableAlunos;
	
	/** DAO responsável pela comunicação com o banco de dados para alunos. */
	private final transient AlunoDAO alunoDAO = new AlunoDAO();
	
	/** Índice da linha selecionada na JTable. -1 significa nenhuma seleção. */
	private int linhaSelecionada = -1;

	/**
     * Construtor padrão.
     * Configura os componentes da UI, carrega a tabela e adiciona listener de clique.
     */
	public GerenciaAlunos() {
		initComponents();
		carregarTabela();

		TableUtils.addMouseClickListener(jTableAlunos, this::jTableMouseClick);
	}

	/**
     * Inicializa todos os componentes gráficos da janela.
     * Define o título, tamanho, painéis superiores, tabela e menu.
     */
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

	/**
     * Abre a janela de cadastro de novo aluno.
     */
	private void abrirCadastro() {
		new CadastroAluno().setVisible(true);
	}

	/**
     * Abre a tela de edição do aluno selecionado na tabela.
     * Caso nenhuma linha esteja selecionada, exibe mensagem de aviso.
     */
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

	/**
     * Exclui o aluno selecionado, após confirmar com o usuário.
     * Em caso de erro ou ausência de seleção, exibe a mensagem apropriada.
     */
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

	/**
     * Exporta os dados exibidos na tabela para um arquivo Excel.
     * Utiliza {@link utils.ExcelExporter}.
     */
	private void exportarExcel() {
		try {
			ExcelExporter.exportTableToExcel(jTableAlunos);
			JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage());
		}
	}

	/**
     * Abre a janela de gerenciamento de professores e fecha a atual.
     */
	private void abrirProfessores() {
		new GerenciaProfessores().setVisible(true);
		dispose();
	}

	/**
     * Executado quando uma linha da tabela é clicada.
     * Atualiza a variável que guarda a linha selecionada.
     *
     * @param evt Evento do clique do mouse.
     */
	private void jTableMouseClick(java.awt.event.MouseEvent evt) {
		linhaSelecionada = jTableAlunos.getSelectedRow();
	}

	// -------------------------------------------------------------------------
	// CARREGAR TABELA
	// -------------------------------------------------------------------------

	/**
     * Recarrega os dados da tabela com a lista atual de alunos.
     * Consulta o banco via {@link dao.AlunoDAO#getMinhaLista()}.
     */
	private void carregarTabela() {
		DefaultTableModel modelo = (DefaultTableModel) jTableAlunos.getModel();
		modelo.setNumRows(0);

		List<Aluno> lista = alunoDAO.getMinhaLista();

		for (Aluno a : lista) {
			modelo.addRow(new Object[] { a.getId(), a.getNome(), a.getIdade(), a.getCurso(), a.getFase() + "ª" });
		}
	}

	/**
     * Método principal para executar a janela de gerenciamento de alunos.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new GerenciaAlunos().setVisible(true));
	}
}
