package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

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

public class CadastroProfessor extends JFrame {

	private JTextField nome;
	private JComboBox<String> campus;
	private JComboBox<String> titulo;
	private JFormattedTextField cpfFormatado;
	private JFormattedTextField contatoFormatado;
	private JFormattedTextField salarioFormatado;
	private com.toedter.calendar.JDateChooser idade;

	private final transient ProfessorDAO professorDAO = new ProfessorDAO();

	public CadastroProfessor() {
		initComponents();
		formatarCampos();
	}

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
		gbc.gridx = 0;
		gbc.gridy = 0;
		form.add(ViewUtils.criarLabel("Nome:", "lblNome"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		nome = new JTextField(20);
		form.add(nome, gbc);

		gbc.gridwidth = 1;

		// Campus
		gbc.gridx = 0;
		gbc.gridy = 1;
		form.add(ViewUtils.criarLabel("Campus:", "lblCampus"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		campus = new JComboBox<>(Constantes.getCampus().toArray(new String[0]));
		form.add(campus, gbc);

		gbc.gridwidth = 1;

		// CPF + Contato
		gbc.gridx = 0;
		gbc.gridy = 2;
		form.add(ViewUtils.criarLabel("CPF:", "lblCPF"), gbc);

		gbc.gridx = 1;
		cpfFormatado = new JFormattedTextField();
		cpfFormatado.setColumns(10);
		form.add(cpfFormatado, gbc);

		gbc.gridx = 2;
		form.add(ViewUtils.criarLabel("Contato:", "lblContato"), gbc);

		gbc.gridx = 3;
		contatoFormatado = new JFormattedTextField();
		contatoFormatado.setColumns(10);
		form.add(contatoFormatado, gbc);

		// Nascimento + Salário
		gbc.gridx = 0;
		gbc.gridy = 3;
		form.add(ViewUtils.criarLabel("Nasc.:", "lblNascimento"), gbc);

		gbc.gridx = 1;
		idade = new com.toedter.calendar.JDateChooser();
		form.add(idade, gbc);

		gbc.gridx = 2;
		form.add(ViewUtils.criarLabel("Salário:", "lblSalario"), gbc);

		gbc.gridx = 3;
		salarioFormatado = new JFormattedTextField();
		salarioFormatado.setColumns(10);
		form.add(salarioFormatado, gbc);

		// Título Professor
		gbc.gridx = 0;
		gbc.gridy = 4;
		form.add(ViewUtils.criarLabel("Título:", "lblTituloProfessor"), gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 3;
		titulo = new JComboBox<>(Constantes.getTitulos().toArray(new String[0]));
		form.add(titulo, gbc);

		gbc.gridwidth = 1;

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

// -------------------------
// AÇÕES
// -------------------------

	private void formatarCampos() {
		try {
			ValidadorInput.aplicarFormatacaoProfessor(cpfFormatado, contatoFormatado, salarioFormatado);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Erro ao formatar campos.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String validarCpf() throws Mensagens {
		String cpf = ValidadorInput.validarTamanhoNumericoFixo(cpfFormatado.getText(), 11, "CPF");

		if (professorDAO.existeCpf(cpf)) {
			throw new Mensagens("CPF já cadastrado no sistema.");
		}
		return cpf;
	}

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

	private void cancelar() {
		dispose();
	}

// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new CadastroProfessor().setVisible(true));
	}
}
