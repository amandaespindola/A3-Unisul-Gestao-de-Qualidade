package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class EditarProfessor extends JFrame {

	private JTextField nome;
	private JComboBox<String> campus;
	private JComboBox<String> titulo;
	private JFormattedTextField cpfFormatado;
	private JFormattedTextField contatoFormatado;
	private JFormattedTextField salarioFormatado;
	private JTextField idade;

	private final transient ProfessorDAO professorDAO = new ProfessorDAO();
	private final String[] dadosProfessor;

	private static final List<String> LISTA_CAMPUS = Constantes.getCampus();
	private static final List<String> LISTA_TITULOS = Constantes.getTitulos();

	public EditarProfessor() {
		this.dadosProfessor = null;
		initComponents();
		formatarCampos();
	}

	public EditarProfessor(String[] dados) {
		this.dadosProfessor = dados;
		initComponents();
		formatarCampos();
		preencherCampos();
	}

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
		ViewUtils.addLabel(form, gbc, 0, 0, "Nome:", "lblNome");
		nome = new JTextField(20);
		ViewUtils.addCampo(form, gbc, 1, 0, 3, nome);

		// Campus
		ViewUtils.addLabel(form, gbc, 0, 1, "Campus:", "lblCampus");
		campus = new JComboBox<>(LISTA_CAMPUS.toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 1, 3, campus);

		// CPF + Contato
		ViewUtils.addLabel(form, gbc, 0, 2, "CPF:", "lblCPF");
		cpfFormatado = new JFormattedTextField();
		cpfFormatado.setColumns(10);
		ViewUtils.addCampo(form, gbc, 1, 2, 1, cpfFormatado);

		// Idade + Salário
		ViewUtils.addLabel(form, gbc, 0, 3, "Idade:", "lblIdade");
		idade = new JTextField(10);
		ViewUtils.addCampo(form, gbc, 1, 3, 1, idade);

		ViewUtils.addLabel(form, gbc, 2, 3, "Salário:", "lblSalario");
		salarioFormatado = new JFormattedTextField();
		salarioFormatado.setColumns(10);
		ViewUtils.addCampo(form, gbc, 3, 3, 1, salarioFormatado);

		// Título acadêmico
		ViewUtils.addLabel(form, gbc, 0, 4, "Título:", "lblTituloProfessor");
		titulo = new JComboBox<>(LISTA_TITULOS.toArray(new String[0]));
		ViewUtils.addCampo(form, gbc, 1, 4, 3, titulo);

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

	// Formatador de CPF, Salário, Contato
	private void formatarCampos() {
		try {
			ValidadorInput.aplicarFormatacaoProfessor(cpfFormatado, contatoFormatado, salarioFormatado);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Erro ao formatar campos.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Preenche campos com dados já existentes
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

	private void cancelar() {
		dispose();
	}

	// VALIDADORES
	private String validarCampus() throws Mensagens {
		return ValidadorInput.validarSelecaoComboBox(campus.getSelectedIndex(), LISTA_CAMPUS, "Campus");
	}

	private String validarTitulo() throws Mensagens {
		return ValidadorInput.validarSelecaoComboBox(titulo.getSelectedIndex(), LISTA_TITULOS, "Título");
	}

	private String validarNome() throws Mensagens {
		return ValidadorInput.validarNome(nome.getText(), 2);
	}

	private String validarCpf() throws Mensagens {
		String cpf = ValidadorInput.validarTamanhoNumericoFixo(cpfFormatado.getText(), 11, "CPF");
		int idAtual = Integer.parseInt(dadosProfessor[7]);

		if (professorDAO.existeCpf(cpf, idAtual)) {
			throw new Mensagens("CPF já cadastrado no sistema.");
		}
		return cpf;
	}

	private String validarContato() throws Mensagens {
		return ValidadorInput.validarTamanhoNumericoFixo(contatoFormatado.getText(), 11, "Contato");
	}

	private int validarIdade() throws Mensagens {
		if (idade.getText().isEmpty()) {
			throw new Mensagens("Idade não pode ser vazia");
		}
		return ValidadorInput.validarTamanhoMinimoNumerico(idade.getText(), 11);
	}

	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new EditarProfessor().setVisible(true));
	}
}
