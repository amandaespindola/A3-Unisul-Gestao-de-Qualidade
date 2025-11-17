package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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
 * Janela responsável pela edição dos dados de um professor já cadastrado.
 * Permite alterar informações pessoais, contato, campus, idade, salário e título acadêmico.
 *
 * A classe utiliza componentes Swing, validações utilitárias e integra com o
 * {@link dao.ProfessorDAO} para atualizar os dados no banco de dados.
 *
 * Campos podem ser pré-preenchidos quando o construtor recebe um vetor de dados.
 *
 * @author Seu Nome
 */
public class EditarProfessor extends JFrame {

	/** Campo de texto para o nome do professor. */
	private JTextField nome;
	
	/** ComboBox de campus disponíveis. */
	private JComboBox<String> campus;
	
	/** ComboBox de títulos acadêmicos disponíveis. */
	private JComboBox<String> titulo;
	
	/** Campo formatado para CPF. */
	private JFormattedTextField cpfFormatado;
	
	/** Campo formatado para contato telefônico. */
	private JFormattedTextField contatoFormatado;
	
	/** Campo formatado para salário. */
	private JFormattedTextField salarioFormatado;
	
	/** Campo de texto para idade. */
	private JTextField idade;

	/** DAO responsável por operações de banco de dados para Professor. */	
	private final transient ProfessorDAO professorDAO = new ProfessorDAO();
	
	/**
     * Vetor contendo os dados do professor a ser editado.
     * A ordem esperada é:
     * [nome, idade, campus, cpf, contato, titulo, salario, id]
     */
	private final String[] dadosProfessor;

	/** Lista estática de campus cadastrados nas constantes. */
	private static final List<String> LISTA_CAMPUS = Constantes.getCampus();
	
	/** Lista estática de títulos acadêmicos. */
	private static final List<String> LISTA_TITULOS = Constantes.getTitulos();

	/**
     * Construtor padrão. Utilizado quando não há dados pré-existentes para preencher.
     * Abre a janela vazia para edição.
     */
	public EditarProfessor() {
		this.dadosProfessor = null;
		initComponents();
		formatarCampos();
	}

	/**
     * Construtor utilizado ao editar um professor já existente.
     *
     * @param dados Vetor contendo os dados do professor conforme ordenação definida,
     *              utilizado para preencher os campos automaticamente.
     */
	public EditarProfessor(String[] dados) {
		this.dadosProfessor = dados;
		initComponents();
		formatarCampos();
		preencherCampos();
	}

<<<<<<< HEAD
	/**
     * Inicializa e organiza todos os componentes da interface gráfica
     * utilizando BorderLayout e GridBagLayout.
     */
=======
	private static class CampoConfig {
		int x;
		int y;
		int width;
		String label; 
		String labelName;
		javax.swing.JComponent componente;

		CampoConfig(int x, int y, int width, String label, String labelName, javax.swing.JComponent componente) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.label = label;
			this.labelName = labelName;
			this.componente = componente;
		}
	}

	private void addField(JPanel form, GridBagConstraints gbc, CampoConfig cfg) {
		ViewUtils.addLabel(form, gbc, cfg.x, cfg.y, cfg.label, cfg.labelName);
		ViewUtils.addCampo(form, gbc, cfg.x + 1, cfg.y, cfg.width, cfg.componente);
	}

>>>>>>> 79bc5a8fc879fe38bf31a14db192a92c78a93141
	private void initComponents() {

		setTitle("Editar Professor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_EDITAR_PROFESSOR);
		painel.add(lblTitulo, BorderLayout.NORTH);

		// Formulário
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Nome
		nome = new JTextField(20);
		addField(form, gbc, new CampoConfig(
				0, 0, 3,
				"Nome:", "lblNome",
				nome
		));

		// Campus
		campus = new JComboBox<>(LISTA_CAMPUS.toArray(new String[0]));
		addField(form, gbc, new CampoConfig(
				0, 1, 3,
				"Campus:", "lblCampus",
				campus
		));

		// CPF
		cpfFormatado = new JFormattedTextField();
		cpfFormatado.setColumns(10);
		addField(form, gbc, new CampoConfig(
				0, 2, 1,
				"CPF:", "lblCPF",
				cpfFormatado
		));

		// Contato
		contatoFormatado = new JFormattedTextField();
		contatoFormatado.setColumns(10);
		addField(form, gbc, new CampoConfig(
				2, 2, 1,
				"Contato:", "lblContato",
				contatoFormatado
		));

		// Idade + Salário
		idade = new JTextField(10);
		addField(form, gbc, new CampoConfig(
				0, 3, 1,
				"Idade:", "lblIdade",
				idade
		));

		salarioFormatado = new JFormattedTextField();
		salarioFormatado.setColumns(10);
		addField(form, gbc, new CampoConfig(
				2, 3, 1,
				"Salário:", "lblSalario",
				salarioFormatado
		));

		// Título acadêmico
		titulo = new JComboBox<>(LISTA_TITULOS.toArray(new String[0]));
		addField(form, gbc, new CampoConfig(
				0, 4, 3,
				"Título:", "lblTituloProfessor",
				titulo
		));

		painel.add(form, BorderLayout.CENTER);

		ViewUtils.adicionarBotoesConfirmarCancelar(painel, this::confirmar, this::cancelar, getRootPane());

		add(painel);
		pack();
		setLocationRelativeTo(null);

	}

	/**
     * Aplica máscaras e formatações para CPF, contato e salário.
     * Utiliza formatadores definidos em {@link ViewUtils}.
     */
	private void formatarCampos() {
		ViewUtils.aplicarFormatacaoProfessorComAlerta(this, cpfFormatado, contatoFormatado, salarioFormatado);
	}

	/**
     * Preenche os campos da interface com os valores recebidos no vetor dadosProfessor.
     * Só executa se dadosProfessor não for nulo.
     */
	private void preencherCampos() {
		if (dadosProfessor == null)
			return;

		nome.setText(dadosProfessor[0]);
		idade.setText(dadosProfessor[1]);

		campus.setSelectedIndex(LISTA_CAMPUS.indexOf(dadosProfessor[2]));
		cpfFormatado.setText(dadosProfessor[3]);
		contatoFormatado.setText(dadosProfessor[4]);
		titulo.setSelectedIndex(LISTA_TITULOS.indexOf(dadosProfessor[5]));

		try {
			salarioFormatado.setValue(Double.parseDouble(dadosProfessor[6]));
		} catch (Exception e) {
			salarioFormatado.setValue(0.0);
		}
	}

	/**
     * Ação executada ao confirmar a edição.
     * Valida todos os campos, cria um {@link ProfessorDTO},
     * converte para {@link Professor} e solicita atualização ao banco via DAO.
     *
     * Exibe mensagens de erro ou sucesso ao usuário.
     */
	private void confirmar() {
		try {
			int id = Integer.parseInt(dadosProfessor[7]);

			String campusProfessor = validarCampus();
			String nomeProfessor = validarNome();
			String cpfProfessor = validarCpf();
			String contatoProfessor = validarContato();
			int idadeProfessor = validarIdade();
			double salario = ValidadorInput.validarSalario(salarioFormatado, 4);
			String tituloProfessor = validarTitulo();

			ProfessorDTO dto = new ProfessorDTO();
			dto.setId(id);
			dto.setCampus(campusProfessor);
			dto.setCpf(cpfProfessor);
			dto.setContato(contatoProfessor);
			dto.setIdade(idadeProfessor);
			dto.setNome(nomeProfessor);
			dto.setSalario(salario);
			dto.setTitulo(tituloProfessor);

			Professor professorAtualizado = new Professor(dto);

			if (professorDAO.update(professorAtualizado)) {
				JOptionPane.showMessageDialog(this, "Professor alterado com sucesso!");
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Erro ao alterar professor.");
			}

		} catch (Mensagens m) {
			JOptionPane.showMessageDialog(this, m.getMessage());
		} catch (NumberFormatException n) {
			JOptionPane.showMessageDialog(this, "Informe um número válido.");
		}
	}

	/**
     * Ação executada ao cancelar a operação.
     * Apenas fecha a janela.
     */
	private void cancelar() {
		dispose();
	}

	/**
     * Valida o campus selecionado.
     *
     * @return O campus selecionado.
     * @throws Mensagens caso nenhum campus válido seja selecionado.
     */
	private String validarCampus() throws Mensagens {
		return ValidadorInput.validarSelecaoComboBox(campus.getSelectedIndex(), LISTA_CAMPUS, "Campus");
	}

	/**
     * Valida o título acadêmico selecionado.
     *
     * @return O título selecionado.
     * @throws Mensagens caso nenhum título válido seja selecionado.
     */
	private String validarTitulo() throws Mensagens {
		return ValidadorInput.validarSelecaoComboBox(titulo.getSelectedIndex(), LISTA_TITULOS, "Título");
	}

	/**
     * Valida o nome do professor.
     *
     * @return O nome validado.
     * @throws Mensagens caso o nome seja muito curto ou inválido.
     */
	private String validarNome() throws Mensagens {
		return ValidadorInput.validarNome(nome.getText(), 2);
	}

	/**
     * Valida o CPF informado e verifica duplicidade no banco.
     *
     * @return O CPF validado.
     * @throws Mensagens caso esteja em formato inválido ou já exista cadastrado.
     */
	private String validarCpf() throws Mensagens {
		String cpf = ValidadorInput.validarTamanhoNumericoFixo(cpfFormatado.getText(), 11, "CPF");
		int idAtual = Integer.parseInt(dadosProfessor[7]);

		if (professorDAO.existeCpf(cpf, idAtual)) {
			throw new Mensagens("CPF já cadastrado no sistema.");
		}
		return cpf;
	}

	/**
     * Valida o número de contato.
     *
     * @return O número validado.
     * @throws Mensagens caso esteja em formato inválido.
     */
	private String validarContato() throws Mensagens {
		return ValidadorInput.validarTamanhoNumericoFixo(contatoFormatado.getText(), 11, "Contato");
	}

	/**
     * Valida a idade informada.
     *
     * @return A idade validada.
     * @throws Mensagens caso o campo esteja vazio ou inválido.
     */
	private int validarIdade() throws Mensagens {
		if (idade.getText().isEmpty()) {
			throw new Mensagens("Idade não pode ser vazia");
		}
		return ValidadorInput.validarTamanhoMinimoNumerico(idade.getText(), 3);
	}

	/**
     * Método principal para abrir a janela individualmente.
     *
     * @param args Argumentos da linha de comando.
     */
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new EditarProfessor().setVisible(true));
	}
}
