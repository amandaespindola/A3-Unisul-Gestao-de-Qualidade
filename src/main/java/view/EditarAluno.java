package view;

import java.awt.BorderLayout;
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

/**
 * Tela de edição de dados de um aluno já cadastrado.
 * <p>
 * Esta interface permite alterar informações como nome, idade, curso e fase.
 * Os dados são pré-carregados quando o construtor recebe o array
 * {@code dadosAluno}, geralmente vindo de uma tabela de listagem.
 * </p>
 *
 * <p>
 * A classe utiliza componentes Swing organizados em um GridBagLayout, 
 * além de validações fornecidas pela classe {@link ValidadorInput} 
 * e persistência utilizando {@link AlunoDAO}.
 * </p>
 */
public class EditarAluno extends JFrame {

	/** Campo de texto para o nome do aluno. */
	private JTextField nome;
	
	/** ComboBox contendo a lista de cursos disponíveis. */
	private JComboBox<String> curso;
	
	/** ComboBox contendo as fases disponíveis para seleção. */
	private JComboBox<Integer> fase;
	
	/** Campo de texto para a idade do aluno. */
	private JTextField idade;

	/** DAO responsável pela persistência e recuperação de dados de alunos. */
	private final transient AlunoDAO alunoDAO = new AlunoDAO();
	
	/**
     * Array contendo os dados do aluno selecionado.
     * <p>
     * A estrutura esperada é:
     * <ul>
     *   <li>[0] - ID</li>
     *   <li>[1] - Nome</li>
     *   <li>[2] - Idade</li>
     *   <li>[3] - Curso</li>
     *   <li>[4] - Fase</li>
     * </ul>
     * Caso seja {@code null}, a tela é aberta sem preencher os campos.
     */
	private final String[] dadosAluno;

	/**
     * Construtor padrão. Abre a tela sem dados preenchidos.
     */
	public EditarAluno() {
		this.dadosAluno = null;
		initComponents();
	}

	/**
     * Construtor que recebe os dados do aluno a ser editado.
     * Popula os campos do formulário com as informações fornecidas.
     *
     * @param dados array contendo os dados completos do aluno
     */
	public EditarAluno(String[] dados) {
		this.dadosAluno = dados;
		initComponents();
		preencherCampos();
	}

	/**
     * Inicializa e configura a interface gráfica, criando os campos,
     * labels, botões e organizando o layout.
     */
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
		ViewUtils.addLabel(form, gbc, 0, 0, "Nome:", "lblNome");
		nome = new JTextField(20);
		ViewUtils.addCampo(form, gbc, 1, 0, 3, nome);

		// Curso
		ViewUtils.addLabel(form, gbc, 0, 1, "Curso:", "lblCurso");
		curso = new JComboBox<>(Constantes.getCursos().toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 1, 3, curso);

		// Idade + Fase
		ViewUtils.addLabel(form, gbc, 0, 2, "Idade:", "lblIdade");
		idade = new JTextField(10);
		ViewUtils.addCampo(form, gbc, 1, 2, 1, idade);

		ViewUtils.addLabel(form, gbc, 2, 2, "Fase:", "lblFase");
		fase = new JComboBox<>(Constantes.getFases().toArray(new Integer[0]));
		ViewUtils.addCampo(form, gbc, 3, 2, 1, fase);

		painel.add(form, BorderLayout.CENTER);

		ViewUtils.adicionarBotoesConfirmarCancelar(painel, this::confirmar, this::cancelar, getRootPane());

		add(painel);
		pack();
		setLocationRelativeTo(null);
	}

	/**
     * Preenche os campos da interface com os dados do aluno selecionado.
     * <p>
     * Apenas é executado se {@link #dadosAluno} não for {@code null}.
     * </p>
     */
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

	/**
     * Realiza a validação dos campos do formulário, atualiza o objeto
     * {@link Aluno} e solicita ao {@link AlunoDAO} que persista as alterações.
     * <p>
     * Em caso de sucesso, exibe uma mensagem ao usuário e fecha a janela.
     * Caso contrário, apresenta uma mensagem de erro.
     * </p>
     */
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

	/**
     * Fecha a janela sem realizar alterações.
     */
	private void cancelar() {
		dispose();
	}

	/**
     * Método principal utilizado apenas para testes da interface.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		java.awt.EventQueue.invokeLater(() -> new EditarAluno().setVisible(true));
	}
}
