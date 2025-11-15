package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;
import utils.LookAndFeelHelper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditarAluno extends JFrame {

	private JTextField nome;
	private JComboBox<String> curso;
	private JComboBox<Integer> fase;
	private JTextField idade;

	private final transient AlunoDAO alunoDAO = new AlunoDAO();
	private final String[] dadosAluno;

	public EditarAluno() {
		this.dadosAluno = null;
		initComponents();
	}

	public EditarAluno(String[] dados) {
		this.dadosAluno = dados;
		initComponents();
		preencherCampos();
	}

	private void initComponents() {

		setTitle("Editar Aluno");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Título
		JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_EDITAR_ALUNO);
		painel.add(lblTitulo, BorderLayout.NORTH);

		// Formulário
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Nome
		gbc.gridx = 0;
		gbc.gridy = 0;
		form.add(ViewUtils.criarLabel("Nome:", "lblNome"), gbc);

		gbc.gridx = 1;
		nome = new JTextField(20);
		form.add(nome, gbc);

		// Curso
		gbc.gridx = 0;
		gbc.gridy = 1;
		form.add(ViewUtils.criarLabel("Curso:", "lblCurso"), gbc);

		gbc.gridx = 1;
		curso = new JComboBox<>(Constantes.getCursos().toArray(new String[0]));
		form.add(curso, gbc);

		// Idade + Fase
		gbc.gridx = 0;
		gbc.gridy = 2;
		form.add(ViewUtils.criarLabel("Idade:", "lblIdade"), gbc);

		gbc.gridx = 1;
		idade = new JTextField(10);
		form.add(idade, gbc);

		gbc.gridx = 2;
		form.add(ViewUtils.criarLabel("Fase:", "lblFase"), gbc);

		gbc.gridx = 3;
		fase = new JComboBox<>(Constantes.getFases().toArray(new Integer[0]));
		form.add(fase, gbc);

		painel.add(form, BorderLayout.CENTER);

		// Botões
		JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR, e -> confirmar());
		JButton bCancelar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CANCELAR, e -> cancelar());

		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		botoes.add(bConfirmar);
		botoes.add(bCancelar);

		painel.add(botoes, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(bConfirmar);

		add(painel);
		pack();
		setLocationRelativeTo(null);
	}

	// Preenche a tela com os dados existentes
	private void preencherCampos() {
		if (dadosAluno == null)
			return;

		List<String> listaCursos = Constantes.getCursos();
		List<Integer> listaFases = Constantes.getFases();

		String cursoAluno = dadosAluno[3];
		int faseAluno = Integer.parseInt(dadosAluno[4]);

		nome.setText(dadosAluno[1]);
		idade.setText(dadosAluno[2]);

		curso.setSelectedIndex(listaCursos.indexOf(cursoAluno));
		fase.setSelectedIndex(listaFases.indexOf(faseAluno));
	}

	private void confirmar() {
		try {
			List<String> listaCursos = Constantes.getCursos();
			List<Integer> listaFases = Constantes.getFases();

			// Validações
			String nomeAluno = ValidadorInput.validarNome(nome.getText(), 2);
			int idadeAluno = ValidadorInput.validarTamanhoMinimoNumerico(idade.getText(), 11);
			String cursoAluno = ValidadorInput.validarSelecaoComboBox(curso.getSelectedIndex(), listaCursos, "Curso");
			int faseAluno = listaFases.get(fase.getSelectedIndex());

			int idAluno = Integer.parseInt(dadosAluno[0]);

			Aluno alunoAtualizado = new Aluno(cursoAluno, faseAluno, idAluno, nomeAluno, idadeAluno);

			if (alunoDAO.update(alunoAtualizado)) {
				JOptionPane.showMessageDialog(this, "Aluno alterado com sucesso!");
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao alterar aluno.");
			}

		} catch (Exception ex) {
			ViewUtils.tratarErroCadastro(ex);
		}
	}

	private void cancelar() {
		dispose();
	}

	// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		java.awt.EventQueue.invokeLater(() -> new EditarAluno().setVisible(true));
	}
}
