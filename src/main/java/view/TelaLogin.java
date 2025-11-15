package view;

import utils.ConexaoManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class TelaLogin extends javax.swing.JFrame {

	public static String passwordDB = "";
	public static String userDB = "";

	private static final Logger logger = Logger.getLogger(TelaLogin.class.getName());

	public TelaLogin() {
		initComponents();
		carregarCredenciais(); // permitido
		getRootPane().setDefaultButton(login); // permitido
	}

	private void carregarCredenciais() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				Properties props = new Properties();
				props.load(input);
				userDB = props.getProperty("db.user");
				passwordDB = props.getProperty("db.password");
			} else {
				System.out.println("config.properties não encontrado.");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro carregando config.properties", e);
		}
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
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
		setResizable(false);

		login.setFont(new java.awt.Font("Segoe UI", 0, 18));
		login.setText("LOGIN");
		login.setToolTipText("ENTER");
		login.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginActionPerformed(evt);
			}
		});

		jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("SisUni - Sistema de Gerenciamento Universitário");

		password.setFont(new java.awt.Font("Segoe UI", 0, 24));
		password.setHorizontalAlignment(javax.swing.JTextField.CENTER);

		jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 10));
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel2.setText("DIGITE A SENHA (MySQL)");

		user.setFont(new java.awt.Font("Segoe UI", 0, 24));
		user.setHorizontalAlignment(javax.swing.JTextField.CENTER);

		jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 10));
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
								.addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 208,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 208,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 208,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addComponent(jLabel3))
								.addGroup(layout.createSequentialGroup().addGap(46, 46, 46).addComponent(jLabel2)))
						.addGap(92, 92, 92)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(26, 26, 26)
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(jLabel3)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(29, Short.MAX_VALUE)));

		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>

	private void loginActionPerformed(java.awt.event.ActionEvent evt) {
		String senha = String.copyValueOf(this.password.getPassword());
		String usuario = this.user.getText();

		passwordDB = senha;
		userDB = usuario;

		ConexaoManager.init(userDB, passwordDB);

		if (ConexaoManager.getConnection() != null) {
			JOptionPane.showMessageDialog(rootPane, "Conexão efetuada com sucesso!");
			new TelaPrincipal().setVisible(true);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(rootPane, "Conexão falhou!");
		}
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(() -> new TelaLogin().setVisible(true));
	}

	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JButton login;
	private javax.swing.JPasswordField password;
	private javax.swing.JTextField user;
}
