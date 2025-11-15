package view;

import dao.AlunoDAO;
import model.Aluno;

import java.util.List;
import javax.swing.JOptionPane;

import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

public class CadastroAluno extends javax.swing.JFrame {

	private final transient AlunoDAO alunoDAO;

	public CadastroAluno() {
		initComponents(); // ← O GUI Builder precisa disso exatamente assim

		// Mantém sua lógica NOVA fora do initComponents
		this.alunoDAO = new AlunoDAO();

		curso.setModel(new javax.swing.DefaultComboBoxModel<>(Constantes.getCursos().toArray(new String[0])));
		fase.setModel(new javax.swing.DefaultComboBoxModel<>(
				Constantes.getFases().stream().map(f -> f + "ª").toArray(String[]::new)));

		// botão padrão (não mexe no initComponents)
		getRootPane().setDefaultButton(this.bConfirmar);
	}

	// ============================================================
	// NÃO ALTERAR
	// ============================================================

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
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
		idade = new com.toedter.calendar.JDateChooser();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Cadastro de Alunos");
		setResizable(false);

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("Cadastro de Aluno");

		curso.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "-", "Administração", "Análise e Desenvolvimento de Sistemas", "Arquitetura e Urbanismo",
						"Ciências Contábeis", "Ciências da Computação", "Design", "Design de Moda",
						"Relações Internacionais", "Sistemas de Informação" }));
		curso.setName("");

		fase.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "1ª", "2ª", "3ª", "4ª", "5ª", "6ª", "7ª", "8ª", "9ª", "10ª" }));

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel2.setText("Nome:");

		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel3.setText("Curso:");

		jLabel4.setText("Nasc.:");

		jLabel5.setText("Fase:");

		bCancelar.setText("Cancelar");
		bCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bCancelarActionPerformed(evt);
			}
		});

		bConfirmar.setText("Confirmar");
		bConfirmar.setToolTipText("ENTER");
		bConfirmar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bConfirmarActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(33, 33, 33)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(nome)
								.addComponent(curso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel5)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(fase, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup()
												.addComponent(bConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 108,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(66, 66, 66).addComponent(bCancelar,
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
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(fase, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel4).addComponent(jLabel5))
						.addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(bConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
								javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(25, 25, 25)));

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	// ============================================================
	// FIM DO BLOCO PROTEGIDO
	// ============================================================

	private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			List<String> listaCursos = Constantes.getCursos();
			List<Integer> listaFases = Constantes.getFases();

			String nomeAluno = ValidadorInput.validarNome(this.nome.getText(), 2);
			int idadeAluno = ValidadorInput.validarIdadePorData(this.idade.getDate(), 11);
			String cursoAluno = ValidadorInput.validarSelecaoComboBox(this.curso.getSelectedIndex(), listaCursos,
					"Curso");
			int faseAluno = listaFases.get(this.fase.getSelectedIndex());

			Aluno novoAluno = new Aluno(cursoAluno, faseAluno, 0, nomeAluno, idadeAluno);

			if (this.alunoDAO.insert(novoAluno)) {
				JOptionPane.showMessageDialog(rootPane, "Aluno cadastrado com sucesso! ID: " + novoAluno.getId());
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(rootPane, "Erro ao cadastrar aluno no banco de dados.");
			}

		} catch (Exception ex) {
			ViewUtils.tratarErroCadastro(ex);
		}
	}

	private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {
		ViewUtils.fecharJanelaAoCancelar(evt.getSource(), this);
	}

	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		java.awt.EventQueue.invokeLater(() -> new CadastroAluno().setVisible(true));
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton bCancelar;
	private javax.swing.JButton bConfirmar;
	private javax.swing.JComboBox<String> curso;
	private javax.swing.JComboBox<String> fase;
	private com.toedter.calendar.JDateChooser idade;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JTextField nome;
	// End of variables declaration//GEN-END:variables

}
