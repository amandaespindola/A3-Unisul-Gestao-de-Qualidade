package view;

import com.formdev.flatlaf.FlatDarkLaf;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelaPrincipal extends javax.swing.JFrame {

	// Logger deve vir AQUI (antes dos métodos e depois dos atributos declarados
	// pelo GUI Builder)
	private static final Logger logger = Logger.getLogger(TelaPrincipal.class.getName());

	public TelaPrincipal() {
		initComponents();
	}

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	@SuppressWarnings("unchecked")
	private void initComponents() {

		bProfessores = new javax.swing.JButton();
		bAlunos = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();
		arquivo = new javax.swing.JMenu();
		menuAlunos = new javax.swing.JMenuItem();
		menuProfessores = new javax.swing.JMenuItem();
		jMenuItem1 = new javax.swing.JMenuItem();
		menuLeave = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Principal");
		setResizable(false);

		bProfessores.setFont(new java.awt.Font("Segoe UI", 0, 18));
		bProfessores.setText("Professores");
		bProfessores.addActionListener(evt -> bProfessoresActionPerformed(evt));

		bAlunos.setFont(new java.awt.Font("Segoe UI", 0, 18));
		bAlunos.setText("Alunos");
		bAlunos.addActionListener(evt -> bAlunosActionPerformed(evt));

		jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("SisUni - Sistema de Gerenciamento Universitário");

		arquivo.setText("Arquivo");

		menuAlunos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuAlunos.setText("Gerenciamento de Alunos");
		menuAlunos.addActionListener(evt -> menuAlunosActionPerformed(evt));
		arquivo.add(menuAlunos);

		menuProfessores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuProfessores.setText("Gerenciamento de Professores");
		menuProfessores.addActionListener(evt -> menuProfessoresActionPerformed(evt));
		arquivo.add(menuProfessores);

		jMenuItem1.setText("Sobre");
		jMenuItem1.addActionListener(evt -> jMenuItem1ActionPerformed(evt));
		arquivo.add(jMenuItem1);

		menuLeave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		menuLeave.setText("Sair");
		menuLeave.addActionListener(evt -> menuLeaveActionPerformed(evt));
		arquivo.add(menuLeave);

		jMenuBar1.add(arquivo);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(95, 95, 95)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(bAlunos, javax.swing.GroupLayout.PREFERRED_SIZE, 208,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(bProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, 208,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(93, Short.MAX_VALUE)));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
								layout.createSequentialGroup().addGap(14, 14, 14)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(44, 44, 44)
										.addComponent(bAlunos, javax.swing.GroupLayout.PREFERRED_SIZE, 55,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(bProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, 55,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(60, Short.MAX_VALUE)));

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	// ------------------------------
	// Métodos Auxiliares (permitidos)
	// ------------------------------
	private void abrirTelaGerenciaAlunos() {
		new GerenciaAlunos().setVisible(true);
		this.dispose();
	}

	private void abrirTelaGeranciaProfessores() {
		new GerenciaProfessores().setVisible(true);
		this.dispose();
	}

	// ------------------------------
	// Actions (GUI Builder)
	// ------------------------------
	private void bAlunosActionPerformed(java.awt.event.ActionEvent evt) {                                       
	    abrirTelaGerenciaAlunos();
	}    

	private void bProfessoresActionPerformed(java.awt.event.ActionEvent evt) {
		abrirTelaGeranciaProfessores();
	}

	private void menuAlunosActionPerformed(java.awt.event.ActionEvent evt) {                                           
	    abrirTelaGerenciaAlunos();
	}  

	private void menuProfessoresActionPerformed(java.awt.event.ActionEvent evt) {
		abrirTelaGeranciaProfessores();
	}

	private void menuLeaveActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		new Sobre().setVisible(true);
	}

	public static void main(String args[]) {
		try {
			FlatDarkLaf.setup();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Falha ao inicializar FlatDarkLaf", e);
		}

		java.awt.EventQueue.invokeLater(() -> new TelaPrincipal().setVisible(true));
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenu arquivo;
	private javax.swing.JButton bAlunos;
	private javax.swing.JButton bProfessores;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JMenuItem menuAlunos;
	private javax.swing.JMenuItem menuLeave;
	private javax.swing.JMenuItem menuProfessores;
	// End of variables declaration//GEN-END:variables
}
