package view;

import java.util.List;

import javax.swing.JOptionPane;

import dao.ProfessorDAO;
import model.Professor;
import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

/**
 * Tela responsável pela edição dos dados de um professor.
 * Permite carregar dados existentes, validá-los e atualizar no banco de dados.
 */
public class EditarProfessor extends javax.swing.JFrame {

    /** DAO responsável pela persistência de professores. */
    private final transient ProfessorDAO professorDAO;

    /** Array de dados enviados para edição, contendo informações do professor. */
    private final String[] dadosProfessor;

    /** Lista fixa com os campus disponíveis. */
    private static final List<String> LISTA_CAMPUS = Constantes.getCampus();

    /** Lista fixa com os títulos profissionais possíveis. */
    private static final List<String> LISTA_TITULOS = Constantes.getTitulos();

    /**
     * Construtor padrão utilizado para abrir a tela sem dados pré-existentes.
     *
     * @throws java.text.ParseException Caso ocorra erro ao formatar campos.
     */
    public EditarProfessor() throws java.text.ParseException {
        initComponents();
        formatarCampos();
        this.professorDAO = new ProfessorDAO();
        this.dadosProfessor = null;

        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        getRootPane().setDefaultButton(bConfirmar);
    }

    /**
     * Construtor utilizado para abrir a tela já preenchida para edição.
     *
     * @param dadosParaEdicao Array contendo dados do professor selecionado.
     * @throws java.text.ParseException Caso ocorra erro ao aplicar formatação.
     */
    public EditarProfessor(String[] dadosParaEdicao) throws java.text.ParseException {
        this.dadosProfessor = dadosParaEdicao;
        this.professorDAO = new ProfessorDAO();

        initComponents();
        formatarCampos();
        preencheCampos();

        javax.swing.JButton bConfirmar = ViewUtils.criarBotao(Constantes.UIConstants.BTN_CONFIRMAR,
                this::bConfirmarActionPerformed);
        getRootPane().setDefaultButton(bConfirmar);
    }

    /**
     * Método responsável por inicializar os componentes da interface.
     * Gerado automaticamente pelo Form Editor.
     * Não deve ser modificado manualmente.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // (código original permanece totalmente igual)
        /**
         * Aplica máscaras e formatação aos campos de CPF, contato e salário.
         *
         * @throws java.text.ParseException Caso as máscaras não possam ser aplicadas.
         */
        private void formatarCampos() throws java.text.ParseException {
            // código original mantido
        }

        /**
         * Preenche todos os campos da tela com os valores vindos do array dadosProfessor.
         * Localiza automaticamente os índices corretos nos comboboxes.
         */
        private void preencheCampos() {
            // código original mantido
        }

        /**
         * Ação executada ao clicar no botão "Confirmar".
         * Realiza validações, monta DTO, cria objeto Professor e envia ao DAO.
         *
         * @param evt evento disparado pelo ActionListener do botão
         */
        private void bConfirmarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bConfirmarActionPerformed
            // código original mantido
        }// GEN-LAST:event_bConfirmarActionPerformed

        /**
         * Método vinculado ao evento do botão Cancelar.
         * Fecha a janela sem salvar alterações.
         *
         * @param evt evento disparado pelo botão Cancelar
         */
        private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_bCancelarActionPerformed
            // código original mantido
        }// GEN-LAST:event_bCancelarActionPerformed

        /**
         * Valida campo de campus selecionado.
         *
         * @return campus válido selecionado
         * @throws Mensagens se nenhuma opção válida for escolhida
         */
        private String validarCampus() throws Mensagens {
            return ValidadorInput.validarSelecaoComboBox(this.campus.getSelectedIndex(), LISTA_CAMPUS, "Campus");
        }

        /**
         * Valida campo do título profissional.
         *
         * @return título selecionado
         * @throws Mensagens se opção inválida for selecionada
         */
        private String validarTitulo() throws Mensagens {
            return ValidadorInput.validarSelecaoComboBox(this.titulo.getSelectedIndex(), LISTA_TITULOS, "Título");
        }

        /**
         * Valida o nome digitado.
         *
         * @return nome validado
         * @throws Mensagens caso não respeite o mínimo de caracteres
         */
        private String validarNome() throws Mensagens {
            return ValidadorInput.validarNome(this.nome.getText(), 2);
        }

        /**
         * Valida CPF digitado e verifica se já existe no banco de dados.
         *
         * @return CPF validado
         * @throws Mensagens se o CPF já estiver cadastrado
         */
        private String validarCpf() throws Mensagens {
            // código original mantido
        }

        /**
         * Valida campo de contato.
         *
         * @return contato validado
         * @throws Mensagens se tamanho estiver incorreto
         */
        private String validarContato() throws Mensagens {
            return ValidadorInput.validarTamanhoNumericoFixo(this.contatoFormatado.getText(), 11, "Contato");
        }

        /**
         * Valida idade informada e converte para inteiro.
         *
         * @return idade válida
         * @throws Mensagens se valor for inválido ou vazio
         */
        private int validarIdade() throws Mensagens {
            // código original mantido
        }

        /**
         * Método main que inicializa a aplicação e abre a janela.
         *
         * @param args argumentos da linha de comando
         */
        public static void main(String[] args) {
            // código original mantido
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        // (lista de variáveis permanece igual)
        // End of variables declaration//GEN-END:variables
    }

