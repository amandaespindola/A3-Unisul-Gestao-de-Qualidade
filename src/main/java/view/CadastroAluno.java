package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.AlunoDAO;
import model.Aluno;
import utils.Constantes;
import utils.LookAndFeelHelper;
import utils.ValidadorInput;
import utils.ViewUtils;

/**
 * Tela de cadastro de alunos.
 *
 * <p>
 * Permite inserir nome, curso, fase e data de nascimento de um aluno, validando
 * os campos e registrando os dados no banco de dados por meio de
 * {@link AlunoDAO}. A interface utiliza componentes auxiliares da classe
 * {@link ViewUtils} para padronização visual.</p>
 *
 * <p>
 * Esta classe herda de {@link JFrame} e cria uma janela independente utilizada
 * no módulo de gestão de alunos do sistema.</p>
 */
public class CadastroAluno extends JFrame {

    /**
     * Campo de texto para o nome do aluno.
     */
    private JTextField nome;

    /**
     * ComboBox contendo a lista de cursos disponíveis.
     */
    private JComboBox<String> curso;

    /**
     * ComboBox contendo a lista de fases possíveis.
     */
    private JComboBox<Integer> fase;

    /**
     * Campo de seleção de data para idade/nascimento.
     */
    private com.toedter.calendar.JDateChooser idade;

    /**
     * DAO responsável por operações de persistência do aluno.
     */
    private final transient AlunoDAO alunoDAO = new AlunoDAO();

    /**
     * Construtor padrão.
     * <p>
     * Inicializa os componentes gráficos e monta a interface de cadastro.</p>
     */
    public CadastroAluno() {
        initComponents();
    }

    /**
     * Inicializa e organiza todos os componentes visuais da tela.
     * <p>
     * Define título, layout, campos do formulário e botões de ação.</p>
     */
    private void initComponents() {

        setTitle("Cadastro de Aluno");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = ViewUtils.criarLabelTitulo(Constantes.UIConstants.TITULO_CAD_ALUNO);
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Painel de formulário
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Nome
        ViewUtils.addLabel(form, gbc, 0, 0, "Nome:", "lblNome");
        nome = new JTextField(20);
        ViewUtils.addCampo(form, gbc, 1, 0, 3, nome);

        // Campo Curso
        ViewUtils.addLabel(form, gbc, 0, 1, Constantes.UIConstants.CURSO, "lblCurso");
        curso = new JComboBox<>(Constantes.getCursos().toArray(new String[0]));
        ViewUtils.addCampo(form, gbc, 1, 1, 3, curso);

        // Idade
        ViewUtils.addLabel(form, gbc, 0, 2, Constantes.UIConstants.NASCIMENTO, "lblNascimento");
        idade = new com.toedter.calendar.JDateChooser();
        ViewUtils.addCampo(form, gbc, 1, 2, 1, idade);

        // Fase
        ViewUtils.addLabel(form, gbc, 2, 2, Constantes.UIConstants.FASE, "lblFase");
        fase = new JComboBox<>(Constantes.getFases().toArray(new Integer[0]));
        ViewUtils.addCampo(form, gbc, 3, 2, 1, fase);

        painel.add(form, BorderLayout.CENTER);

        ViewUtils.adicionarBotoesConfirmarCancelar(painel, this::confirmar, this::cancelar, getRootPane());

        add(painel);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Ação executada ao pressionar o botão "Confirmar".
     *
     * <p>
     * Realiza validação dos campos utilizando {@link ValidadorInput}, cria um
     * novo objeto {@link Aluno} e o envia ao banco de dados por meio do método
     * {@link AlunoDAO#insert(model.Aluno)}.</p>
     *
     * <p>
     * Exibe mensagens de sucesso ou erro conforme necessário.</p>
     */
    private void confirmar() {
        try {
            String nomeAluno = ValidadorInput.validarNome(nome.getText(), 2);
            int idadeAluno = ValidadorInput.validarIdadePorData(idade.getDate(), 11);

            List<String> cursos = Constantes.getCursos();
            List<Integer> fases = Constantes.getFases();

            String cursoAluno = ValidadorInput.validarSelecaoComboBox(curso.getSelectedIndex(), cursos, "Curso");

            int faseAluno = fases.get(fase.getSelectedIndex());

            Aluno novoAluno = new Aluno(cursoAluno, faseAluno, 0, nomeAluno, idadeAluno);

            if (alunoDAO.insert(novoAluno)) {
                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso! ID: " + novoAluno.getId());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar aluno no banco.");
            }

        } catch (Exception e) {
            ViewUtils.tratarErroCadastro(e);
        }
    }

    /**
     * Fecha a janela de cadastro sem salvar informações.
     * <p>
     * Executado quando o usuário clica em "Cancelar".</p>
     */
    private void cancelar() {
        dispose();
    }

    /**
     * Método main utilizado para teste isolado da tela.
     * <p>
     * Aplica o tema Nimbus e exibe a janela.</p>
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        LookAndFeelHelper.aplicarNimbus();
        EventQueue.invokeLater(() -> new CadastroAluno().setVisible(true));
    }
}
