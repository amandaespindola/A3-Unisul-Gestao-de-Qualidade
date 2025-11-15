package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;
import utils.LookAndFeelHelper;

import java.util.List;

import javax.swing.JOptionPane;

/**
 * Tela gráfica responsável pela edição dos dados de um aluno.
 * Permite carregar dados existentes, validá-los e atualizar o registro no banco.
 */
public class EditarAluno extends javax.swing.JFrame {

    /** DAO responsável pelas operações de persistência do aluno. */
    private final transient AlunoDAO alunoDAO;

    /** Array contendo os dados do aluno para edição. */
    private final String[] dadosAluno;

    /**
     * Construtor padrão. Usado quando a tela será aberta sem dados pré-existentes.
     * Inicializa os componentes e prepara o botão padrão.
     */
    public EditarAluno() {
        initComponents();
        this.alunoDAO = new AlunoDAO();
        this.dadosAluno = null;

        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        getRootPane().setDefaultButton(bConfirmar);
    }

    /**
     * Construtor utilizado quando os dados de um aluno já existem e devem ser editados.
     *
     * @param dados Array contendo os dados do aluno (id, nome, idade, curso, fase).
     */
    public EditarAluno(String[] dados) {
        initComponents();
        this.alunoDAO = new AlunoDAO();
        this.dadosAluno = dados;
        preencheCampos();

        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        getRootPane().setDefaultButton(bConfirmar);
    }

    /**
     * Método gerado automaticamente pelo Form Editor.
     * Responsável por configurar e inicializar todos os componentes da interface.
     * Não deve ser modificado manualmente.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // (código original permanece igual)
        /**
         * Preenche os campos da tela com os dados fornecidos no array dadosAluno.
         * Faz a seleção automática de curso e fase.
         */
        private void preencheCampos() {
            // (código original permanece igual)
        }

        /**
         * Ação executada ao clicar no botão "Confirmar".
         * Realiza validações, monta um objeto Aluno e envia a atualização ao DAO.
         *
         * @param evt Evento acionado pelo botão Confirmar.
         */
        private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bConfirmarActionPerformed
            // (código original permanece igual)
        }// GEN-LAST:event_bConfirmarActionPerformed

        /**
         * Ação executada ao clicar no botão "Cancelar".
         * Fecha a janela sem salvar alterações.
         *
         * @param evt Evento acionado pelo botão Cancelar.
         */
        private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bCancelarActionPerformed
            ViewUtils.fecharJanelaAoCancelar(evt.getSource(), this);
        }// GEN-LAST:event_bCancelarActionPerformed

        /**
         * Método principal para iniciar a aplicação isoladamente.
         *
         * @param args argumentos da linha de comando.
         */
        public static void main(String[] args) {
            LookAndFeelHelper.aplicarNimbus();
            java.awt.EventQueue.invokeLater(() -> new EditarAluno().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JComboBox<String> curso;
        private javax.swing.JComboBox<String> fase;
        private javax.swing.JTextField idade;
        private javax.swing.JTextField nome;
        // End of variables declaration//GEN-END:variables
    }

