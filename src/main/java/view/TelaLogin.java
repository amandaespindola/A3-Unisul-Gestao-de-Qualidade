package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;

import utils.Constantes;
import utils.ConexaoManager;
import utils.LookAndFeelHelper;

public class TelaLogin extends JFrame {

	private static final Logger logger = Logger.getLogger(TelaLogin.class.getName());
	private String passwordDB;
	private String userDB;

	private JTextField campoUsuario;
	private JPasswordField campoSenha;
        
        private String defaultFont = Constantes.UIConstants.DEFAULT_FONT;

	public TelaLogin() {
		initComponents();
		carregarCredenciais();
	}

	private void carregarCredenciais() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				Properties props = new Properties();
				props.load(input);
				userDB = props.getProperty("db.user");
				passwordDB = props.getProperty("db.password");
			} else {
				logger.warning("Arquivo config.properties não encontrado — usuário deverá digitar manualmente.");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro ao carregar config.properties", e);
		}
	}

	private void initComponents() {

		setTitle("Login");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		// Fundo somente para modo claro.
		// (No modo escuro o FlatDarkLaf cuida de tudo)
		if (!UIManager.getLookAndFeel().getName().toLowerCase().contains("dark")) {
			getContentPane().setBackground(new Color(245, 245, 245));
		}

		// Borda azul igual ao vídeo
		Color blueBorder = new Color(120, 155, 255);
		Border border = BorderFactory.createLineBorder(blueBorder, 2);

		// ======================
		// TÍTULO
		// ======================
		JLabel lblSistema = new JLabel("SisUni - Sistema de Gerenciamento Universitário", SwingConstants.CENTER);
		lblSistema.setFont(new Font(defaultFont, Font.BOLD, 18));
		lblSistema.setForeground(UIManager.getColor("Label.foreground"));

		// ======================
		// LABEL USUÁRIO
		// ======================
		JLabel lblUser = new JLabel("DIGITE O USUÁRIO (MySQL)", JLabel.CENTER);
		lblUser.setFont(new Font(defaultFont, Font.PLAIN, 15));

		// ======================
		// CAMPO USUÁRIO
		// ======================
		campoUsuario = new JTextField();
		campoUsuario.setFont(new Font(defaultFont, Font.PLAIN, 25));
		campoUsuario.setHorizontalAlignment(JTextField.CENTER);
		campoUsuario.setPreferredSize(new Dimension(320, 50));
		campoUsuario.setBorder(border);

		// NÃO definir background/foreground → deixa o tema cuidar

		// ======================
		// LABEL SENHA
		// ======================
		JLabel lblSenha = new JLabel("DIGITE A SENHA (MySQL)", JLabel.CENTER);
		lblSenha.setFont(new Font(defaultFont, Font.PLAIN, 15));

		// ======================
		// CAMPO SENHA
		// ======================
		campoSenha = new JPasswordField();
		campoSenha.setFont(new Font(defaultFont, Font.PLAIN, 25));
		campoSenha.setHorizontalAlignment(JTextField.CENTER);
		campoSenha.setPreferredSize(new Dimension(320, 50));
		campoSenha.setBorder(border);

		// NÃO definir background/foreground → deixa o tema cuidar

		// ======================
		// BOTÃO LOGIN
		// ======================
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font(defaultFont, Font.BOLD, 20));
		btnLogin.setPreferredSize(new Dimension(320, 55));
		btnLogin.setBorder(border);
		btnLogin.setFocusPainted(false);
		btnLogin.addActionListener(e -> login());

		getRootPane().setDefaultButton(btnLogin);

		// ======================
		// GROUPLAYOUT (centralizado verticalmente)
		// ======================
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(lblSistema, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(lblUser)
				.addComponent(campoUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addComponent(lblSenha)
				.addComponent(campoSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE));

		layout.setVerticalGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE) // Centraliza verticalmente
				.addComponent(lblSistema).addGap(25).addComponent(lblUser)
				.addComponent(campoUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addGap(20).addComponent(lblSenha)
				.addComponent(campoSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addGap(25).addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE)
				.addGap(0, 0, Short.MAX_VALUE));

		// ======================
		// TAMANHO DA JANELA
		// ======================
		setSize(520, 500);
		setMinimumSize(new Dimension(520, 500));
		setLocationRelativeTo(null);
	}

	private void login() {

		String usuarioDigitado = campoUsuario.getText();
		String senhaDigitada = String.valueOf(campoSenha.getPassword());

		if (!usuarioDigitado.isBlank())
			userDB = usuarioDigitado;
		if (!senhaDigitada.isBlank())
			passwordDB = senhaDigitada;

		// Inicializa conexão global
		ConexaoManager.init(userDB, passwordDB);

		if (ConexaoManager.getConnection() != null) {
			JOptionPane.showMessageDialog(this, "Conexão efetuada com sucesso!");

			TelaPrincipal tela = new TelaPrincipal();
			tela.setVisible(true);

			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Conexão falhou!");
		}
	}

	// MAIN
	public static void main(String[] args) {
		LookAndFeelHelper.aplicarNimbus();
		EventQueue.invokeLater(() -> new TelaLogin().setVisible(true));
	}
}
