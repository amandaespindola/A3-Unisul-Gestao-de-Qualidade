package view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.ViewUtils;
import utils.ConexaoManager;
import utils.Constantes;

public class TelaLogin extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(TelaLogin.class.getName());
    private String passwordDB;
    private String userDB;

    public TelaLogin() {
        initComponents();
        carregarCredenciais();

        javax.swing.JButton login = ViewUtils.criarBotao(Constantes.UIConstants.BTN_LOGIN, this::loginActionPerformed);
        getRootPane().setDefaultButton(login);
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

    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JButton login = ViewUtils.criarBotao(Constantes.UIConstants.BTN_LOGIN, this::loginActionPerformed);
        javax.swing.JLabel lblSistema = ViewUtils.criarLabel(Constantes.UIConstants.TITULO_SISTEMA, "lblSistema");
        password = new javax.swing.JPasswordField();
        javax.swing.JLabel lblSenha = ViewUtils.criarLabel(Constantes.UIConstants.LABEL_SENHA, "lblSenha");
        user = new javax.swing.JTextField();
        javax.swing.JLabel lblUser = ViewUtils.criarLabel(Constantes.UIConstants.LABEL_USUARIO, "lblUser");
        javax.swing.JMenuBar jMenuBar1 = ViewUtils.criarMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(51, 255, 51));
        setResizable(false);

        login.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        login.setText("LOGIN");
        login.setToolTipText("ENTER");
        login.setAlignmentX(0.5F);

        lblSistema.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, java.awt.Font.BOLD, 13));
        lblSistema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSistema.setText("SisUni - Sistema de Gerenciamento Universitário");

        password.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, 0, 24));
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblSenha.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, 0, 10));
        lblSenha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSenha.setText("DIGITE A SENHA (MySQL)");

        user.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, 0, 24));
        user.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblUser.setFont(new java.awt.Font(Constantes.UIConstants.DEFAULT_FONT, 0, 10));
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUser.setText("DIGITE O USUÁRIO (MySQL)");
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSistema, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(password)
                        .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(lblUser))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(lblSenha)))
                .addGap(92, 92, 92))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(lblSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        login.getAccessibleContext().setAccessibleDescription("");

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
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new TelaLogin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}
