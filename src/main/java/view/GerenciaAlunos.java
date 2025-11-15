package view;

import dao.AlunoDAO;
import model.Aluno;
import utils.ValidadorInput;
import utils.ViewUtils;
import utils.Constantes;
import utils.TableUtils;

import java.io.IOException;
import java.util.logging.Logger;

import utils.ExcelExporter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Tela de gerenciamento de alunos.
 * Permite cadastrar, editar, excluir, atualizar e exportar alunos para Excel.
 * Também exibe a lista de alunos em uma tabela utilizando JTable.
 */
public class GerenciaAlunos extends javax.swing.JFrame {

    /** DAO responsável pelas operações CRUD de alunos. */
    private final transient AlunoDAO alunoDAO;

    /** Armazena a linha selecionada da tabela. -1 indica nenhuma linha selecionada. */
    private int linhaSelecionada = -1;

    /**
     * Construtor da tela de Gerência de Alunos.
     * Inicializa componentes, carrega a tabela e adiciona listener de clique.
     */
    public GerenciaAlunos() {
        initComponents();
        this.alunoDAO = new AlunoDAO();
        this.carregaTabela();

        TableUtils.addMouseClickListener(jTableAlunos, GerenciaAlunos.this::jTableAlunosMouseClicked);
    }

    /**
     * Método responsável por inicializar a interface gráfica (Swing).
     * Esse código é gerado automaticamente pelo NetBeans.
     */
    private void initComponents() {
        // ... (seu código original permanece exatamente igual)
    }

    /**
     * Exporta os dados da tabela para um arquivo Excel (XLSX).
     * Exibe mensagens de sucesso ou erro para o usuário.
     */
    private void exportXls() {
        try {
            ExcelExporter.exportTableToExcel(jTableAlunos);
            JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + e.getMessage(),
                    "Erro de Exportação", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre a tela de Gerência de Professores.
     *
     * @param evt evento de clique do menu
     */
    private void menuGerenciaProfessoresActionPerformed(java.awt.event.ActionEvent evt) {
        GerenciaProfessores tela = new GerenciaProfessores();
        tela.setVisible(true);
        this.dispose();
    }

    /**
     * Fecha a aplicação quando o usuário seleciona a opção "Sair".
     *
     * @param evt evento de clique
     */
    private void menuLeaveActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    /**
     * Abre a tela de cadastro de aluno.
     *
     * @param evt evento de clique do botão cadastrar
     */
    private void bCadastroActionPerformed(java.awt.event.ActionEvent evt) {
        CadastroAluno tela = new CadastroAluno();
        tela.setVisible(true);
    }

    /**
     * Abre a tela de edição com os dados pré-preenchidos do aluno selecionado.
     *
     * @param evt evento de clique do botão editar
     */
    private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {
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
    }

    /**
     * Captura o clique na tabela e armazena a linha selecionada.
     *
     * @param evt evento de clique do mouse
     */
    private void jTableAlunosMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() >= 1 && this.jTableAlunos.getSelectedRow() != -1) {
            this.linhaSelecionada = this.jTableAlunos.getSelectedRow();
        }
    }

    /**
     * Exclui o aluno selecionado após confirmação do usuário.
     *
     * @param evt evento do botão deletar
     */
    private void bDeletarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int id;

            if (this.jTableAlunos.getSelectedRow() == -1) {
                throw new Mensagens("Selecione um cadastro para deletar.");
            } else {
                id = Integer.parseInt(this.jTableAlunos.getValueAt(this.jTableAlunos.getSelectedRow(), 0).toString());
            }

            String[] options = { "Sim", "Não" };
            int respostaUsuario = JOptionPane.showOptionDialog(null,
                    "Tem certeza que deseja apagar este cadastro?",
                    "Confirmar exclusão",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if (respostaUsuario == 0) {
                if (this.alunoDAO.delete(id)) {
                    JOptionPane.showMessageDialog(rootPane, "Cadastro apagado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao apagar o cadastro no banco de dados.");
                }
            }

        } catch (Mensagens erro) {
            JOptionPane.showMessageDialog(null, erro.getMessage());
        } finally {
            carregaTabela();
        }
    }

    /**
     * Atualiza a tabela de alunos na interface.
     *
     * @param evt evento do botão atualizar
     */
    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {
        this.carregaTabela();
    }

    /**
     * Atualiza a tabela pelo menu "Atualizar".
     *
     * @param evt evento do menu
     */
    private void menuRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        this.carregaTabela();
    }

    /**
     * Exporta os dados da tabela via menu "Exportar".
     *
     * @param evt evento do menu
     */
    private void menuExportActionPerformed(java.awt.event.ActionEvent evt) {
        this.exportXls();
    }

    /**
     * Exporta os dados da tabela pelo botão "Exportar para Excel".
     *
     * @param evt evento do botão exportar
     */
    private void exportActionPerformed(java.awt.event.ActionEvent evt) {
        this.exportXls();
    }

    /**
     * Abre a janela "Sobre" com informações do sistema.
     *
     * @param evt evento do menu
     */
    private void menuSobreActionPerformed(java.awt.event.ActionEvent evt) {
        Sobre tela = new Sobre();
        tela.setVisible(true);
    }

    /**
     * Carrega os alunos do banco e popula a tabela da interface.
     */
    public void carregaTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.jTableAlunos.getModel();
        modelo.setNumRows(0);

        List<Aluno> minhalista = this.alunoDAO.getMinhaLista();

        for (Aluno a : minhalista) {
            modelo.addRow(new Object[]{
                    a.getId(),
                    a.getNome(),
                    a.getIdade(),
                    a.getCurso(),
                    a.getFase() + "ª"
            });
        }
    }

    /**
     * Método principal. Inicia a interface utilizando o LookAndFeel Nimbus.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new GerenciaAlunos().setVisible(true));
    }

    // Variables declaration - do not modify
    /** Tabela que exibe os alunos cadastrados. */
    private javax.swing.JTable jTableAlunos;
    // End of variables declaration
}
