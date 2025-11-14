package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;

import java.io.IOException;
import java.util.logging.Logger;

import utils.ExcelExporter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GerenciaAlunos extends javax.swing.JFrame {

    private final transient AlunoDAO alunoDAO;
    private int linhaSelecionada = -1;

    public GerenciaAlunos() {
        initComponents();
        this.alunoDAO = new AlunoDAO();
        this.carregaTabela();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JButton bCadastro = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CADASTRAR, this::bCadastroActionPerformed);
        javax.swing.JButton bEditar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_EDITAR, this::bEditarActionPerformed);
        javax.swing.JButton bDeletar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_DELETAR, this::bDeletarActionPerformed);
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAlunos = new javax.swing.JTable();
        javax.swing.JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_GERENCIA_ALUNOS);
        javax.swing.JButton refresh = ViewUtils.criarBotao(Constantes.UIConstants.BTN_ATUALIZAR, this::refreshActionPerformed);
        javax.swing.JButton export = ViewUtils.criarBotao(Constantes.UIConstants.BTN_EXPORTAR, this::exportActionPerformed);
        javax.swing.JMenuBar jMenuBar1 = ViewUtils.criarMenuBar();
        javax.swing.JMenu menu = new javax.swing.JMenu();
        javax.swing.JMenuItem menuGerenciaProfessores = ViewUtils.criarMenuItem(
            "Gerenciar Professores",
            this::menuGerenciaProfessoresActionPerformed,
            "menuGerenciaProfessores"
        );
        javax.swing.JMenuItem menuExport = ViewUtils.criarMenuItem(
            "Exportar",
            this::menuExportActionPerformed,
            "menuExport"
        );
        javax.swing.JMenuItem menuRefresh = ViewUtils.criarMenuItem(
            "Atualizar",
            this::menuRefreshActionPerformed,
            "menuRefresh"
        );
        javax.swing.JMenuItem menuSobre = ViewUtils.criarMenuItem(
            Constantes.UIConstants.SOBRE,
            this::menuSobreActionPerformed,
            "menuSobre"
        );
        javax.swing.JMenuItem menuLeave = ViewUtils.criarMenuItem(
            "Sair",
            this::menuLeaveActionPerformed,
            "menuLeave"
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gerência de Alunos");
        setBackground(new java.awt.Color(80, 80, 80));
        setResizable(false);

        bCadastro.setText("Cadastrar novo");
        bCadastro.setToolTipText("");

        bEditar.setText("Editar");
        bEditar.setToolTipText("");

        bDeletar.setText("Deletar");
        bDeletar.setToolTipText("");

        jTableAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Idade", "Curso", "Fase"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAlunos.setSelectionForeground(new java.awt.Color(239, 239, 239));
        jTableAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAlunosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableAlunos);
        if (jTableAlunos.getColumnModel().getColumnCount() > 0) {
            jTableAlunos.getColumnModel().getColumn(0).setMinWidth(40);
            jTableAlunos.getColumnModel().getColumn(0).setMaxWidth(40);
            jTableAlunos.getColumnModel().getColumn(1).setMinWidth(400);
            jTableAlunos.getColumnModel().getColumn(1).setMaxWidth(400);
            jTableAlunos.getColumnModel().getColumn(2).setMinWidth(60);
            jTableAlunos.getColumnModel().getColumn(2).setMaxWidth(60);
            jTableAlunos.getColumnModel().getColumn(3).setMinWidth(300);
            jTableAlunos.getColumnModel().getColumn(3).setMaxWidth(300);
        }

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 40)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Cadastro de Alunos");

        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/refresh.png"))); // NOI18N
        refresh.setText("  Atualizar tabela");
        refresh.setToolTipText("CTRL+R");

        export.setText("Exportar para Excel");
        export.setToolTipText("CTRL+E");

        menu.setForeground(new java.awt.Color(239, 239, 239));
        menu.setText("Arquivo");

        menuGerenciaProfessores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuGerenciaProfessores.setText("Gerência de Professores");
        menu.add(menuGerenciaProfessores);

        menuExport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuExport.setText("Exportar para Excel");
        menu.add(menuExport);

        menuRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuRefresh.setText("Atualizar tabela");
        menu.add(menuRefresh);

        menuSobre.setText("Sobre");
        menu.add(menuSobre);

        menuLeave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuLeave.setText("Sair");
        menu.add(menuLeave);

        jMenuBar1.add(menu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(bCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCadastro)
                    .addComponent(bEditar)
                    .addComponent(bDeletar)
                    .addComponent(refresh)
                    .addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exportXls() {
        try {
            ExcelExporter.exportTableToExcel(jTableAlunos);
            JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage(), "Erro de Exportação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void menuGerenciaProfessoresActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuGerenciaProfessoresActionPerformed
        GerenciaProfessores tela = new GerenciaProfessores();
        tela.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_menuGerenciaProfessoresActionPerformed

    private void menuLeaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuLeaveActionPerformed
        System.exit(0);
    }// GEN-LAST:event_menuLeaveActionPerformed

    private void bCadastroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bCadastroActionPerformed
        CadastroAluno tela = new CadastroAluno();
        tela.setVisible(true);

    }// GEN-LAST:event_bCadastroActionPerformed

    private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bEditarActionPerformed
        if (this.linhaSelecionada != -1) {

            String[] dadosParaEdicao = new String[5];

            String id = this.jTableAlunos.getValueAt(this.linhaSelecionada, 0).toString();
            String nome = this.jTableAlunos.getValueAt(this.linhaSelecionada, 1).toString();
            String idade = this.jTableAlunos.getValueAt(this.linhaSelecionada, 2).toString();
            String curso = this.jTableAlunos.getValueAt(this.linhaSelecionada, 3).toString();
            String fase = this.jTableAlunos.getValueAt(this.linhaSelecionada, 4).toString();

            String faseLimpa = ValidadorInput.removerMascara(fase);

            dadosParaEdicao[0] = id;
            dadosParaEdicao[1] = nome;
            dadosParaEdicao[2] = idade;
            dadosParaEdicao[3] = curso;
            dadosParaEdicao[4] = faseLimpa;

            EditarAluno editar = new EditarAluno(dadosParaEdicao);
            editar.setVisible(true);

            this.linhaSelecionada = -1;

        } else {
            JOptionPane.showMessageDialog(null, "Selecione um aluno na tabela para editar.");
        }
    }// GEN-LAST:event_bEditarActionPerformed

    private void jTableAlunosMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTableAlunosMouseClicked
        if (evt.getClickCount() >= 1 && this.jTableAlunos.getSelectedRow() != -1) {
            this.linhaSelecionada = this.jTableAlunos.getSelectedRow();
        }
    }// GEN-LAST:event_jTableAlunosMouseClicked

    private void bDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bDeletarActionPerformed
        try {
            // validando dados da interface gráfica.
            int id = 0;

            if (this.jTableAlunos.getSelectedRow() == -1) {
                throw new Mensagens("Selecione um cadastro para deletar");
            } else {
                id = Integer.parseInt(this.jTableAlunos.getValueAt(this.jTableAlunos.getSelectedRow(), 0).toString());
            }

            String[] options = {"Sim", "Não"};
            int respostaUsuario = JOptionPane.showOptionDialog(null, "Tem certeza que deseja apagar este cadastro?",
                    "Confirmar exclusão", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
                    options[1]);

            if (respostaUsuario == 0) {

                // envia os dados para o AlunoDAO processar e trata a resposta
                if (this.alunoDAO.delete(id)) {
                    JOptionPane.showMessageDialog(rootPane, "Cadastro apagado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao apagar o cadastro no banco de dados.");
                }
            }
        } catch (Mensagens erro) {
            JOptionPane.showMessageDialog(null, erro.getMessage());
        } finally {
            // atualiza a tabela.
            carregaTabela();
        }
    }// GEN-LAST:event_bDeletarActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshActionPerformed
        assert evt != null || evt == null;
        Logger.getLogger(getClass().getName()).fine("Menu refresh triggered");
        this.carregaTabela();
    }// GEN-LAST:event_refreshActionPerformed

    private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuRefreshActionPerformed
        assert evt != null || evt == null;
        this.carregaTabela();
    }// GEN-LAST:event_menuRefreshActionPerformed

    private void menuExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuExportActionPerformed
        assert evt != null || evt == null;
        this.exportXls();

    }// GEN-LAST:event_menuExportActionPerformed

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportActionPerformed
        assert evt != null || evt == null;
        Logger.getLogger(getClass().getName()).fine("Menu export triggered");
        this.exportXls();

    }// GEN-LAST:event_exportActionPerformed

    private void menuSobreActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        Sobre tela = new Sobre();
        tela.setVisible(true);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    public void carregaTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.jTableAlunos.getModel();
        modelo.setNumRows(0);

        List<Aluno> minhalista;
        minhalista = this.alunoDAO.getMinhaLista();

        for (Aluno a : minhalista) {
            modelo.addRow(new Object[]{a.getId(), a.getNome(), a.getIdade(), a.getCurso(), a.getFase() + "ª",});
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GerenciaAlunos.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GerenciaAlunos().setVisible(true));

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableAlunos;
    // End of variables declaration//GEN-END:variables
}
