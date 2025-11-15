package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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

	private static final Logger LOGGER = Logger.getLogger(GerenciaProfessores.class.getName());

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
		JPanel painelSuperior = ViewUtils.criarPainelSuperiorTitulo("Cadastro de Professores");

		// ---- Botões ----
		JButton bAtualizar = ViewUtils.criarBotao("Atualizar tabela", e -> carregarTabela());
		JButton bCadastrar = ViewUtils.criarBotao("Cadastrar novo", e -> abrirCadastro());
		JButton bEditar = ViewUtils.criarBotao("Editar", e -> editar());
		JButton bDeletar = ViewUtils.criarBotao("Deletar", e -> deletar());
		JButton bExportar = ViewUtils.criarBotao("Exportar para Excel", e -> exportarExcel());

		// Ícone atualizar (igual ao original)
		try {
			ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/View/refresh.png"));
			Image img = refreshIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
			refreshIcon = new ImageIcon(img);
			bAtualizar.setIcon(refreshIcon);
			bAtualizar.setHorizontalTextPosition(SwingConstants.RIGHT);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Ícone não encontrado", e);
		}

		JPanel painelBotoes = ViewUtils.criarPainelBotoesGerencia(bAtualizar, bCadastrar, bEditar, bDeletar, bExportar);

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
		setJMenuBar(ViewUtils.criarMenuGerencia("Gerência de Alunos", this::abrirAlunos,
				() -> new Sobre().setVisible(true)));
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
