package view;

import javax.swing.JOptionPane;

import dao.ProfessorDAO;
import model.Professor;

import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

public class CadastroProfessor extends javax.swing.JFrame {

    /**
     * DAO responsável pelas operações de persistência relacionadas ao Professor.
     * É marcado como transient para evitar problemas de serialização.
     */
    private final transient ProfessorDAO professorDAO;

    /**
     * Construtor da tela de Cadastro de Professor.
     * Inicializa os componentes da interface gráfica, formata campos e define
     * o botão padrão da janela.
     * 
     * @throws java.text.ParseException caso ocorra erro ao formatar campos.
     */
    public CadastroProfessor() throws java.text.ParseException {
        initComponents();
        formatarCampos();
        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        getRootPane().setDefaultButton(bConfirmar);
        this.professorDAO = new ProfessorDAO();
    }

    /**
     * Método gerado automaticamente pelo Editor de Formulários. 
     * Inicializa e organiza os componentes da interface gráfica.
     * 
     * <p><b>Aviso:</b> Este método é regenerado sempre que o editor gráfico é usado,
     * portanto não deve ser modificado manualmente.</p>
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        javax.swing.JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_CAD_PROFESSOR);
        nome = new javax.swing.JTextField();
        campus = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblNome = new javax.swing.JLabel();
        javax.swing.JLabel lblCampus = ViewUtils.criarLabel(Constantes.UIConstants.CAMPUS, "lblCampus");
        javax.swing.JButton bCancelar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CANCELAR,
                this::bCancelarActionPerformed);
        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        titulo = new javax.swing.JComboBox<>();
        javax.swing.JLabel lblTituloProfessor = ViewUtils.criarLabel(Constantes.UIConstants.TITULO,
                "lblTituloProfessor");
        idade = new com.toedter.calendar.JDateChooser();
        javax.swing.JLabel lblNascimento = ViewUtils.criarLabel(Constantes.UIConstants.NASCIMENTO, "lblNascimento");
        cpfFormatado = new javax.swing.JFormattedTextField();
        javax.swing.JLabel lblCPF = ViewUtils.criarLabel(Constantes.UIConstants.CPF, "lblCPF");
        javax.swing.JLabel lblContato = ViewUtils.criarLabel(Constantes.UIConstants.CONTATO, "lblContato");
        salarioFormatado = new javax.swing.JFormattedTextField();
        javax.swing.JLabel lblSalario = ViewUtils.criarLabel(Constantes.UIConstants.SALARIO, "lblSalario");
        contatoFormatado = new javax.swing.JFormattedTextField();

        ViewUtils.configurarTelaCadastroPadrao(this, lblTitulo, "Cadastro de Professor", bCancelar, bConfirmar);

        idade.setBackground(new java.awt.Color(153, 153, 153));
        idade.setForeground(new java.awt.Color(51, 51, 51));

        lblNascimento.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNascimento.setText("Nasc.:");

        lblCPF.setText("CPF:");

        lblContato.setText("Contato:");

        lblSalario.setText("Salário:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblTitulo,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup().addGap(29, 29, 29).addGroup(layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblTituloProfessor).addComponent(lblNascimento))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(bConfirmar,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 108,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(66, 66, 66).addComponent(bCancelar,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 108,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(idade,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 108,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(lblSalario)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(salarioFormatado,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))))

                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblNome).addComponent(lblCampus).addComponent(lblCPF))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(nome).addComponent(campus, 0, 282, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                        .createSequentialGroup().addComponent(cpfFormatado)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(lblContato)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(contatoFormatado,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addContainerGap()
                .addComponent(
                        lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(campus, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCampus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cpfFormatado, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCPF).addComponent(lblContato).addComponent(contatoFormatado,
                                javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(idade, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNascimento)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(salarioFormatado, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSalario)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTituloProfessor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)));

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Aplica máscaras e formatações nos campos de entrada do formulário.
     * Exibe mensagem de erro caso alguma formatação falhe.
     * 
     * @throws java.text.ParseException caso ocorra falha na aplicação de máscara.
     */
    private void formatarCampos() throws java.text.ParseException {
        try {
            ValidadorInput.aplicarFormatacaoProfessor(cpfFormatado, contatoFormatado, salarioFormatado);
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao formatar campos", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida o CPF informado pelo usuário, removendo formatação,
     * verificando o tamanho e consultando no banco se já existe cadastrado.
     * 
     * @return CPF validado e limpo (apenas números).
     * @throws Mensagens caso o CPF seja inválido ou já existente.
     */
    private String validarCpf() throws Mensagens {
        String cpfLimpo = ValidadorInput.validarTamanhoNumericoFixo(this.cpfFormatado.getText(), 11, "CPF");

        if (this.professorDAO.existeCpf(cpfLimpo)) {
            throw new Mensagens("CPF já cadastrado no sistema");
        }
        return cpfLimpo;
    }

    /**
     * Ação executada quando o botão Confirmar é pressionado.
     * Valida os campos do formulário, cria um ProfessorDTO,
     * utiliza o DAO para inserir no banco e exibe mensagens apropriadas.
     * 
     * @param evt evento de clique do botão.
     */
    private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {

        try {

            String nomeProfessor = ValidadorInput.validarNome(this.nome.getText(), 2);
            String campusProfessor = ValidadorInput.validarSelecaoComboBox(this.campus.getSelectedIndex(),
                    Constantes.getCampus(), "Campus");
            String cpfProfessor = validarCpf();
            String contatoProfessor = ValidadorInput.validarTamanhoNumericoFixo(this.contatoFormatado.getText(), 11,
                    "Contato");
            int idadeProfessor = ValidadorInput.validarIdadePorData(this.idade.getDate(), 11);
            double salarioProfessor = ValidadorInput.validarSalario(this.salarioFormatado, 4);
            String tituloProfessor = ValidadorInput.validarSelecaoComboBox(this.titulo.getSelectedIndex(),
                    Constantes.getTitulos(), "Título");

            model.ProfessorDTO dto = new model.ProfessorDTO();
            dto.setCampus(campusProfessor);
            dto.setCpf(cpfProfessor);
            dto.setContato(contatoProfessor);
            dto.setTitulo(tituloProfessor);
            dto.setSalario(salarioProfessor);
            dto.setNome(nomeProfessor);
            dto.setIdade(idadeProfessor);
            dto.setId(0);
            Professor novoProfessor = new Professor(dto);

            // Adicionando dados validados no database usando o DAO
            if (this.professorDAO.insert(novoProfessor)) {
                JOptionPane.showMessageDialog(rootPane,
                        "Professor cadastrado com sucesso! ID: " + novoProfessor.getId());

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Erro ao cadastrar professor no banco de dados.");
            }

        } catch (Exception ex) {
            ViewUtils.tratarErroCadastro(ex);
        }

    }

    /**
     * Ação executada quando o botão Cancelar é pressionado.
     * Fecha a janela atual utilizando o utilitário ViewUtils.
     * 
     * @param evt evento de clique do botão.
     */
    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        ViewUtils.fecharJanelaAoCancelar(evt.getSource(), this);
    }

    /**
     * Inicializa o Look and Feel da aplicação e abre a janela de cadastro.
     * 
     * @param args argumentos da linha de comando.
     */
    public static void main(String[] args) {
        LookAndFeelHelper.aplicarNimbus();
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new CadastroProfessor().setVisible(true);
            } catch (java.text.ParseException ex) {
                java.util.logging.Logger.getLogger(CadastroProfessor.class.getName())
                        .log(java.util.logging.Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> campus;
    private javax.swing.JFormattedTextField contatoFormatado;
    private javax.swing.JFormattedTextField cpfFormatado;
    private com.toedter.calendar.JDateChooser idade;
    private javax.swing.JTextField nome;
    private javax.swing.JFormattedTextField salarioFormatado;
    private javax.swing.JComboBox<String> titulo;
    // End of variables declaration//GEN-END:variables
}
