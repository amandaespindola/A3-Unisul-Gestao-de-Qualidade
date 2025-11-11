package view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.ConexaoManager;
import utils.Constantes;

public class TelaLogin extends javax.swing.JFrame {
	private static final Logger logger = Logger.getLogger(TelaLogin.class.getName());
	private String passwordDB;
	private String userDB;

	public TelaLogin() {
		initComponents();
		carregarCredenciais();
		getRootPane().setDefaultButton(this.login);
	}

	private void carregarCredenciais() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				Properties props = new Properties();
				props.load(input);
				userDB = props.getProperty("db.user");
				passwordDB = props.getProperty("db.password");
			} else {
				logger.warning("Arquivo config.properties não encontrado. O usuário deverá inserir manualmente");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro ao carregar arquivo de configuração (config.properties)", e);
		}
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		login = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		password = new javax.swing.JPasswordField();
		jLabel2 = new javax.swing.JLabel();
		user = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Login");
		setAlwaysOnTop(true);
		setBackground(new java.awt.Color(51, 255, 51));
		setResizable(false);

		login.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 18)); // NOI18N
		login.setText("LOGIN");
		login.setToolTipText("ENTER");
		login.setAlignmentX(0.5F);
		login.addActionListener(this::loginActionPerformed);

		jLabel1.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 14)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("SisUni - Sistema de Gerenciamento Universitário");

		password.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 24)); // NOI18N
		password.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		jLabel2.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 10)); // NOI18N
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel2.setText("DIGITE A SENHA (MySQL)");

		user.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 24)); // NOI18N
		user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		jLabel3.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.PLAIN, 10)); // NOI18N
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel3.setText("DIGITE O USUÁRIO (MySQL)");
		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(password).addComponent(user,
												javax.swing.GroupLayout.PREFERRED_SIZE, 208,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addComponent(jLabel3))
								.addGroup(layout.createSequentialGroup().addGap(46, 46, 46).addComponent(jLabel2)))
						.addGap(92, 92, 92)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap(26, Short.MAX_VALUE)
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(jLabel3)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(29, 29, 29)));

		login.getAccessibleContext().setAccessibleDescription("Cadastro de Professores");

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	private void loginActionPerformed(java.awt.event.ActionEvent evt) {
		String senhaDigitada = String.copyValueOf(this.password.getPassword());
		String usuarioDigitado = this.user.getText();

		if (!senhaDigitada.isBlank()) {
			this.passwordDB = senhaDigitada; // se manteve campos de instância
		}
		if (!usuarioDigitado.isBlank()) {
			this.userDB = usuarioDigitado;
		}

		// 1) Inicializa a conexão global
		ConexaoManager.init(this.userDB, this.passwordDB);

		// 2) Testa conexão global
		if (ConexaoManager.getConnection() != null) {
			JOptionPane.showMessageDialog(rootPane, "Conexão efetuada com sucesso!");
			TelaPrincipal tela = new TelaPrincipal(); // não precisa mais passar Connection
			tela.setVisible(true);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(rootPane, "Conexão falhou!");
		}
	}
	// GEN-LAST:event_loginActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(() -> new TelaLogin().setVisible(true));
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JButton login;
	private javax.swing.JPasswordField password;
	private javax.swing.JTextField user;
	// End of variables declaration//GEN-END:variables
}
