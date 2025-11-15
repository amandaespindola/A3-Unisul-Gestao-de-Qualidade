package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.ConexaoManager;
import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ViewUtils;

public class TelaLogin extends JFrame {

	private static final Logger logger = Logger.getLogger(TelaLogin.class.getName());
	private String passwordDB;
	private String userDB;

	private JTextField campoUsuario;
	private JPasswordField campoSenha;

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
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setSize(380, 300);
	        setLocationRelativeTo(null);
	        setResizable(false);
	        setLayout(new BorderLayout(10, 10));

	        // ===========================
	        // TÍTULO
	        // ===========================
	        JLabel lblTitulo = new JLabel(Constantes.UIConstants.TITULO_SISTEMA, JLabel.CENTER);
	        lblTitulo.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.BOLD, 15));
	        add(lblTitulo, BorderLayout.NORTH);

	        // ===========================
	        // PAINEL CENTRAL COM CAMPOS
	        // ===========================
	        JPanel painelCampos = new JPanel(new GridLayout(6, 1, 5, 5));

	        JLabel lblUser = new JLabel(Constantes.UIConstants.LABEL_USUARIO, JLabel.CENTER);
	        lblUser.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 11));

	        campoUsuario = new JTextField();
	        campoUsuario.setHorizontalAlignment(JTextField.CENTER);
	        campoUsuario.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 20));

	        JLabel lblSenha = new JLabel(Constantes.UIConstants.LABEL_SENHA, JLabel.CENTER);
	        lblSenha.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 11));

	        campoSenha = new JPasswordField();
	        campoSenha.setHorizontalAlignment(JPasswordField.CENTER);
	        campoSenha.setFont(new Font(Constantes.UIConstants.DEFAULT_FONT, Font.PLAIN, 20));

	        painelCampos.add(lblUser);
	        painelCampos.add(campoUsuario);
	        painelCampos.add(lblSenha);
	        painelCampos.add(campoSenha);

	        add(painelCampos, BorderLayout.CENTER);

	        // ===========================
	        // BOTÃO LOGIN
	        // ===========================
	        JButton bLogin = ViewUtils.criarBotao(Constantes.UIConstants.BTN_LOGIN, e -> login());
	        bLogin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	        add(bLogin, BorderLayout.SOUTH);

	        getRootPane().setDefaultButton(bLogin);
	    }

	    private void login() {

	        String usuarioDigitado = campoUsuario.getText();
	        String senhaDigitada = String.valueOf(campoSenha.getPassword());

	        if (!usuarioDigitado.isBlank()) userDB = usuarioDigitado;
	        if (!senhaDigitada.isBlank()) passwordDB = senhaDigitada;

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
