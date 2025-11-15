package model;

import java.util.List;

import dao.AlunoDAO;

/**
 * Classe que representa um Aluno no sistema, herdando de Pessoa.
 * Contém atributos específicos (curso, fase) e métodos de interação com o banco.
 */
public class Aluno extends Pessoa {

    // Atributos

    /**
     * Curso que o aluno está cursando.
     */
    private String curso;

    /**
     * Fase (período/semestre) atual do aluno.
     */
    private int fase;

    /**
     * Objeto responsável pela comunicação com o banco de dados.
     */
    private final AlunoDAO dao;

    /**
     * Construtor padrão que inicializa o DAO.
     */
    public Aluno() {
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor que recebe curso e fase.
     *
     * @param curso curso do aluno
     * @param fase fase do aluno
     */
    public Aluno(String curso, int fase) {
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor completo que recebe também os atributos da classe Pessoa.
     *
     * @param curso curso do aluno
     * @param fase fase atual do aluno
     * @param id id do aluno
     * @param nome nome do aluno
     * @param idade idade do aluno
     */
    public Aluno(String curso, int fase, int id, String nome, int idade) {
        super(id, nome, idade);
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    // Métodos GET e SET

    /**
     * Retorna o curso do aluno.
     *
     * @return curso do aluno
     */
    public String getCurso() {
        return curso;
    }

    /**
     * Define o curso do aluno.
     *
     * @param curso novo curso
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Retorna a fase do aluno.
     *
     * @return fase do aluno
     */
    public int getFase() {
        return fase;
    }

    /**
     * Altera a fase do aluno.
     *
     * @param fase nova fase
     */
    public void setFase(int fase) {
        this.fase = fase;
    }

    /**
     * Retorna uma representação em texto do aluno.
     *
     * @return dados formatados do aluno
     */
    @Override
    public String toString() {
        return "\n ID: " + this.getId()
                + "\n Nome: " + this.getNome()
                + "\n Idade: " + this.getIdade()
                + "\n Curso: " + this.getCurso()
                + "\n Fase:" + this.getFase()
                + "\n -----------";
    }

    /**
     * Retorna a lista de todos os alunos cadastrados.
     *
     * @return lista de alunos
     */
    public List<Aluno> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Insere um novo aluno no banco de dados.
     *
     * @param curso curso do aluno
     * @param fase fase do aluno
     * @param nome nome do aluno
     * @param idade idade do aluno
     * @return true se inserido com sucesso
     */
    public boolean inserirAlunoBd(String curso, int fase, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, 0, nome, idade);
        return dao.insert(objeto);
    }

    /**
     * Deleta um aluno do banco através do ID.
     *
     * @param id identificador do aluno
     * @return true após realizar a operação
     */
    public boolean deletarAlunoBD(int id) {
        dao.delete(id);
        return true;
    }

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param curso novo curso
     * @param fase nova fase
     * @param id id do aluno
     * @param nome novo nome
     * @param idade nova idade
     * @return true após realizar a operação
     */
    public boolean atualizarAlunoBD(String curso, int fase, int id, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, id, nome, idade);
        dao.update(objeto);
        return true;
    }

    /**
     * Carrega os dados de um aluno específico pelo ID.
     *
     * @param id identificador do aluno
     * @return objeto Aluno encontrado ou null
     */
    public Aluno carregaAluno(int id) {
        dao.findById(id);
        return null;
    }

    /**
     * Retorna o maior ID registrado no banco de dados.
     *
     * @return maior ID encontrado
     */
    public int obterMaiorId() {
        return dao.obterMaiorId();
    }
}
