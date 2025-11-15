package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.AlunoDAO;
import model.Aluno;
import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

public class CadastroAluno extends JFrame {

	private JTextField nome;
	private JComboBox<String> curso;
	private JComboBox<Integer> fase;
	private com.toedter.calendar.JDateChooser idade;
	private final transient AlunoDAO alunoDAO = new AlunoDAO();

	public CadastroAluno() {
		initComponents();
	}

	private void initComponents() {

		setTitle("Cadastro de Aluno");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Título
		JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_CAD_ALUNO);
		painel.add(lblTitulo, BorderLayout.NORTH);

		// Painel de formulário
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Campo Nome
		ViewUtils.addLabel(form, gbc, 0, 0, "Nome:", "lblNome");
		nome = new JTextField(20);
		ViewUtils.addCampo(form, gbc, 1, 0, 3, nome);

		// Campo Curso
		ViewUtils.addLabel(form, gbc, 0, 1, Constantes.UIConstants.CURSO, "lblCurso");
		curso = new JComboBox<>(Constantes.getCursos().toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 1, 3, curso);

		// Idade
		ViewUtils.addLabel(form, gbc, 0, 2, Constantes.UIConstants.NASCIMENTO, "lblNascimento");
		idade = new com.toedter.calendar.JDateChooser();
		ViewUtils.addCampo(form, gbc, 1, 2, 1, idade);

		// Fase
		ViewUtils.addLabel(form, gbc, 2, 2, Constantes.UIConstants.FASE, "lblFase");
		fase = new JComboBox<>(Constantes.getFases().toArray(new Integer[0]));
		ViewUtils.addCampo(form, gbc, 3, 2, 1, fase);

		painel.add(form, BorderLayout.CENTER);

		ViewUtils.adicionarBotoesConfirmarCancelar(painel, this::confirmar, this::cancelar, getRootPane());

		add(painel);
		pack();
		setLocationRelativeTo(null);
	}

	// AÇÃO CONFIRMAR
	private void confirmar() {
		try {
			String nomeAluno = ValidadorInput.validarNome(nome.getText(), 2);
			int idadeAluno = ValidadorInput.validarIdadePorData(idade.getDate(), 11);

			List<String> cursos = Constantes.getCursos();
			List<Integer> fases = Constantes.getFases();

			String cursoAluno = ValidadorInput.validarSelecaoComboBox(curso.getSelectedIndex(), cursos, "Curso");

			int faseAluno = fases.get(fase.getSelectedIndex());

			Aluno novoAluno = new Aluno(cursoAluno, faseAluno, 0, nomeAluno, idadeAluno);

			if (alunoDAO.insert(novoAluno)) {
				JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso! ID: " + novoAluno.getId());
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao cadastrar aluno no banco.");
			}

		} catch (Exception e) {
			ViewUtils.tratarErroCadastro(e);
		}
	}

	// AÇÃO CANCELAR
	private void cancelar() {
		dispose();
	}

	// MAIN PARA TESTAR A TELA
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new CadastroAluno().setVisible(true));
	}
}
