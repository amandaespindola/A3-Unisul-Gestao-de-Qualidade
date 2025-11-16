package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.ProfessorDAO;
import model.Professor;
import model.ProfessorDTO;
import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

/**
 * Tela de cadastro de professores da instituição.
 * <p>
 * Esta interface permite inserir dados como nome, campus, CPF, contato,
 * título, data de nascimento e salário. Os dados são validados e enviados ao
 * {@link ProfessorDAO} para persistência no banco de dados.
 * </p>
 *
 * <p>
 * A tela utiliza componentes Swing e o layout GridBagLayout para organização
 * dos campos, além de formatadores e validadores utilitários para garantir a
 * consistência das informações inseridas.
 * </p>
 */
public class CadastroProfessor extends JFrame {

	/** Campo de texto para o nome do professor. */
	private JTextField nome;
	
	/** ComboBox que contém a lista de campus disponíveis. */
	private JComboBox<String> campus;
	
	/** ComboBox contendo os títulos possíveis (Mestre, Doutor, etc.). */
	private JComboBox<String> titulo;
	
	/** Campo formatado para entrada do CPF. */
	private JFormattedTextField cpfFormatado;
	
	/** Campo formatado para o número de contato do professor. */
	private JFormattedTextField contatoFormatado;
	
	/** Campo formatado para o salário do professor. */
	private JFormattedTextField salarioFormatado;
	
	/** Componente para seleção de data de nascimento. */
	private com.toedter.calendar.JDateChooser idade;

	/**
     * DAO responsável pela persistência de dados do professor.
     * Usado para inserir novos registros e validar CPF duplicado.
     */
	private final transient ProfessorDAO professorDAO = new ProfessorDAO();

	/**
     * Construtor padrão. Inicializa os componentes da interface gráfica
     * e aplica a formatação aos campos que exigem máscara.
     */
	public CadastroProfessor() {
		initComponents();
		formatarCampos();
	}

	/**
     * Inicializa e configura todos os elementos da interface gráfica,
     * organizando o formulário e os botões principais.
     */
	private void initComponents() {
		setTitle("Cadastro de Professor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Título
		JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_CAD_PROFESSOR);
		painel.add(lblTitulo, BorderLayout.NORTH);

		// Formulário
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Campo Nome
		ViewUtils.addLabel(form, gbc, 0, 0, "Nome:", "lblNome");
		nome = new JTextField(20);
		ViewUtils.addCampo(form, gbc, 1, 0, 3, nome);

		// Campus
		ViewUtils.addLabel(form, gbc, 0, 1, "Campus:", "lblCampus");
		campus = new JComboBox<>(Constantes.getCampus().toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 1, 3, campus);

		// CPF + Contato
		ViewUtils.addLabel(form, gbc, 0, 2, "CPF:", "lblCPF");
		cpfFormatado = new JFormattedTextField();
		cpfFormatado.setColumns(10);
		ViewUtils.addCampo(form, gbc, 1, 2, 1, cpfFormatado);

		ViewUtils.addLabel(form, gbc, 2, 2, "Contato:", "lblContato");
		contatoFormatado = new JFormattedTextField();
		contatoFormatado.setColumns(10);
		ViewUtils.addCampo(form, gbc, 3, 2, 1, contatoFormatado);

		// Nascimento + Salário
		ViewUtils.addLabel(form, gbc, 0, 3, "Nasc.:", "lblNascimento");
		idade = new com.toedter.calendar.JDateChooser();
		ViewUtils.addCampo(form, gbc, 1, 3, 1, idade);

		ViewUtils.addLabel(form, gbc, 2, 3, "Salário:", "lblSalario");
		salarioFormatado = new JFormattedTextField();
		salarioFormatado.setColumns(10);
		ViewUtils.addCampo(form, gbc, 3, 3, 1, salarioFormatado);

		// Título Professor
		ViewUtils.addLabel(form, gbc, 0, 4, "Título:", "lblTituloProfessor");
		titulo = new JComboBox<>(Constantes.getTitulos().toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 4, 3, titulo);

		painel.add(form, BorderLayout.CENTER);

		ViewUtils.adicionarBotoesConfirmarCancelar(painel, this::confirmar, this::cancelar, getRootPane());

		add(painel);
		pack();
		setLocationRelativeTo(null);
	}

// -------------------------
// AÇÕES
// -------------------------

	/**
     * Aplica as formatações necessárias aos campos de CPF, contato e salário,
     * exibindo alertas caso a formatação esteja incorreta.
     */
	private void formatarCampos() {
		ViewUtils.aplicarFormatacaoProfessorComAlerta(this, cpfFormatado, contatoFormatado, salarioFormatado);
	}

	/**
     * Valida o CPF informado no campo correspondente.
     * <p>
     * Realiza validação de tamanho e verifica se o CPF já existe no sistema.
     * </p>
     *
     * @return o CPF validado em formato numérico (somente dígitos)
     * @throws Mensagens caso o CPF seja inválido ou já esteja cadastrado
     */
	private String validarCpf() throws Mensagens {
		String cpf = ValidadorInput.validarTamanhoNumericoFixo(cpfFormatado.getText(), 11, "CPF");

		if (professorDAO.existeCpf(cpf)) {
			throw new Mensagens("CPF já cadastrado no sistema.");
		}
		return cpf;
	}

	/**
     * Realiza a ação de confirmação do cadastro do professor.
     * <p>
     * Este método valida todos os campos do formulário, monta um
     * {@link ProfessorDTO}, converte-o em um objeto {@link Professor} e tenta
     * salvá-lo no banco de dados via {@link ProfessorDAO}.
     * </p>
     * Caso o cadastro seja bem-sucedido, uma mensagem é exibida ao usuário.
     */
	private void confirmar() {
		try {
			String nomeProfessor = ValidadorInput.validarNome(nome.getText(), 2);
			String campusProfessor = ValidadorInput.validarSelecaoComboBox(campus.getSelectedIndex(),
					Constantes.getCampus(), "Campus");

			String cpfProfessor = validarCpf();

			String contatoProfessor = ValidadorInput.validarTamanhoNumericoFixo(contatoFormatado.getText(), 11,
					"Contato");

			int idadeProfessor = ValidadorInput.validarIdadePorData(idade.getDate(), 11);

			double salarioProfessor = ValidadorInput.validarSalario(salarioFormatado, 4);

			String tituloProfessor = ValidadorInput.validarSelecaoComboBox(titulo.getSelectedIndex(),
					Constantes.getTitulos(), "Título");

			ProfessorDTO dto = new ProfessorDTO();
			dto.setCampus(campusProfessor);
			dto.setCpf(cpfProfessor);
			dto.setContato(contatoProfessor);
			dto.setTitulo(tituloProfessor);
			dto.setSalario(salarioProfessor);
			dto.setNome(nomeProfessor);
			dto.setIdade(idadeProfessor);
			dto.setId(0);

			Professor novoProfessor = new Professor(dto);

			if (professorDAO.insert(novoProfessor)) {
				JOptionPane.showMessageDialog(this, "Professor cadastrado com sucesso! ID: " + novoProfessor.getId());
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao cadastrar professor no banco.");
			}

		} catch (Exception ex) {
			ViewUtils.tratarErroCadastro(ex);
		}
	}

	/**
     * Fecha a tela atual sem realizar alterações.
     */
	private void cancelar() {
		dispose();
	}

	/**
     * Método principal utilizado para testes da tela.
     * <p>
     * Aplica o tema Nimbus e exibe a janela de cadastro de professores.
     * </p>
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new CadastroProfessor().setVisible(true));
	}
}
