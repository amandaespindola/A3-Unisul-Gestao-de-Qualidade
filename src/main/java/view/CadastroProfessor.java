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

	private void formatarCampos() {
		ViewUtils.aplicarFormatacaoProfessorComAlerta(this, cpfFormatado, contatoFormatado, salarioFormatado);
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
