package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;
import utils.LookAndFeelHelper;

import java.util.List;
import javax.swing.JOptionPane;

public class EditarAluno extends javax.swing.JFrame {

	private final transient AlunoDAO alunoDAO;
	private final String[] dadosAluno;

	// ======== CONSTRUTOR ÚNICO (CERTO) ========
	public EditarAluno(String[] dados) {
		initComponents();
		this.alunoDAO = new AlunoDAO();
		this.dadosAluno = dados;

		preencheCampos();
		getRootPane().setDefaultButton(bConfirmar);
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		jLabel1 = new javax.swing.JLabel();
		nome = new javax.swing.JTextField();
		curso = new javax.swing.JComboBox<>();
		fase = new javax.swing.JComboBox<>();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		bCancelar = new javax.swing.JButton();
		bConfirmar = new javax.swing.JButton();
		idade = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Editar Aluno");
		setResizable(false);

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("Editar Aluno");

		curso.setModel(new javax.swing.DefaultComboBoxModel<>(Constantes.getCursos().toArray(new String[0])));
		fase.setModel(new javax.swing.DefaultComboBoxModel<>(Constantes.getFasesFormatadas().toArray(new String[0])));

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel2.setText("Nome:");

		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel3.setText("Curso:");

		jLabel4.setText("Idade:");
		jLabel5.setText("Fase:");

		bCancelar.setText("Cancelar");
		bCancelar.addActionListener(evt -> bCancelarActionPerformed(evt));

		bConfirmar.setText("Confirmar");
		bConfirmar.addActionListener(evt -> bConfirmarActionPerformed(evt));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(33, 33, 33)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(nome).addComponent(curso, 0, 250, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18).addComponent(jLabel5)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(fase, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup()
												.addComponent(bConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 108,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(40, 40, 40).addComponent(bCancelar,
														javax.swing.GroupLayout.PREFERRED_SIZE, 108,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(37, Short.MAX_VALUE)));

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addComponent(
						jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(curso, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel3))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(fase, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel4).addComponent(jLabel5)
						.addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(30, 30, 30)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(bConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
								javax.swing.GroupLayout.PREFERRED_SIZE))
				.addContainerGap(25, Short.MAX_VALUE)));

		pack();
		setLocationRelativeTo(null);
	}

	private void preencheCampos() {
		nome.setText(dadosAluno[1]);
		idade.setText(dadosAluno[2]);

		curso.setSelectedItem(dadosAluno[3]);
		fase.setSelectedItem(dadosAluno[4] + "ª");
	}

	private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			String nomeAluno = ValidadorInput.validarNome(nome.getText(), 2);

			int idadeAluno = Integer.parseInt(idade.getText());
			if (idadeAluno < 11)
				throw new Mensagens("Idade inválida");

			String cursoAluno = (String) curso.getSelectedItem();
			if (cursoAluno.equals("-"))
				throw new Mensagens("Escolha um curso.");

			int faseAluno = Integer.parseInt(fase.getSelectedItem().toString().replace("ª", ""));

			int idAluno = Integer.parseInt(dadosAluno[0]);

			Aluno alunoAtualizado = new Aluno(cursoAluno, faseAluno, idAluno, nomeAluno, idadeAluno);

			if (alunoDAO.update(alunoAtualizado)) {
				JOptionPane.showMessageDialog(rootPane, "Aluno alterado com sucesso!");
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(rootPane, "Erro ao alterar aluno no banco.");
			}

		} catch (Exception ex) {
			ViewUtils.tratarErroCadastro(ex);
		}
	}

	private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		java.awt.EventQueue.invokeLater(() -> {
			// NÃO EXISTE MAIS CONSTRUTOR SEM PARÂMETROS (CORRETO)
		});
	}

	private javax.swing.JButton bCancelar;
	private javax.swing.JButton bConfirmar;
	private javax.swing.JComboBox<String> curso;
	private javax.swing.JComboBox<String> fase;
	private javax.swing.JTextField idade;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JTextField nome;
}
