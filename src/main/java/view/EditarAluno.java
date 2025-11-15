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
 * Janela responsável pela edição dos dados de um aluno já cadastrado.
 * Permite ao usuário alterar nome, idade, curso e fase.
 * 
 * Esta classe utiliza componentes Swing e interage com {@link AlunoDAO}
 * para atualizar as informações no banco de dados.
 */
public class EditarAluno extends javax.swing.JFrame {

    /** DAO responsável por operações relacionadas ao aluno. */
    private final transient AlunoDAO alunoDAO;

    /** Dados originais do aluno selecionado para edição. */
    private final String[] dadosAluno;

    /**
     * Construtor padrão utilizado quando nenhum aluno é informado.
     * Inicializa os componentes da interface gráfica e prepara o botão padrão.
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
     * Construtor utilizado quando os dados de um aluno existente são informados.
     * Preenche automaticamente os campos da interface com os dados recebidos.
     *
     * @param dados vetor contendo informações do aluno selecionado
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
     * Método gerado automaticamente responsável por inicializar
     * todos os componentes gráficos da interface.
     * <p>
     * <b>Não deve ser modificado manualmente.</b>
     */
    private void initComponents() {
        // (código original permanece igual)
