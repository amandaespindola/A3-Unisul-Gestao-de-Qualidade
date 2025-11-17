package model;

import java.util.List;

import dao.AlunoDAO;

/**
 * Representa um aluno dentro do sistema acadêmico.
 * <p>
 * A classe herda de {@link Pessoa} e adiciona informações específicas de alunos,
 * como curso e fase. Também encapsula operações de persistência delegadas ao
 * {@link AlunoDAO}.
 * </p>
 */
public class Aluno extends Pessoa {

	/** Curso em que o aluno está matriculado. */
    private String curso;
    
    /** Fase (semestre/período) atual do aluno. */
    private int fase;
    
    /** DAO responsável pelas operações de banco de dados relacionadas ao aluno. */
    private final AlunoDAO dao;

    /**
     * Construtor padrão. Inicializa o DAO.
     */
    public Aluno() {
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor que inicializa curso e fase.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase (período/semestre) atual.
     */
    public Aluno(String curso, int fase) {
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor completo, utilizado geralmente em operações de leitura do banco.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase atual.
     * @param id    Identificador único do aluno.
     * @param nome  Nome do aluno.
     * @param idade Idade do aluno.
     */
    public Aluno(String curso, int fase, int id, String nome, int idade) {
        super(id, nome, idade);
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    // Métodos GET e SET
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }

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
     * Retorna a lista completa de alunos armazenados no banco de dados.
     *
     * @return Lista de objetos {@link Aluno}.
     */
    public List<Aluno> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Cadastra um novo aluno no banco de dados.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase atual.
     * @param nome  Nome do aluno.
     * @param idade Idade do aluno.
     * @return {@code true} se a operação foi bem-sucedida, {@code false} caso contrário.
     */
    public boolean inserirAlunoBd(String curso, int fase, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, 0, nome, idade);
        return dao.insert(objeto);
    }

    /**
     * Exclui um aluno do banco de dados com base no ID informado.
     *
     * @param id Identificador único do aluno a ser removido.
     * @return {@code true} se a remoção foi realizada, {@code false} caso contrário.
     */
    public boolean deletarAlunoBD(int id) {
        dao.delete(id);
        return true;
    }

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param curso Curso atualizado.
     * @param fase  Fase atualizada.
     * @param id    ID do aluno a ser atualizado.
     * @param nome  Nome atualizado.
     * @param idade Idade atualizada.
     * @return {@code true} se a atualização foi bem-sucedida, {@code false} caso contrário.
     */
    public boolean atualizarAlunoBD(String curso, int fase, int id, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, id, nome, idade);
        dao.update(objeto);
        return true;
    }

    /**
     * Carrega um aluno específico a partir do banco de dados.
     *
     * @param id Identificador único do aluno.
     * @return Objeto {@link Aluno} correspondente ou {@code null} se não encontrado.
     */
    public Aluno carregaAluno(int id) {
        dao.findById(id);
        return null;
    }

    /**
     * Retorna o maior ID presente na tabela de alunos.
     *
     * @return Maior valor de ID encontrado ou 0 caso não existam registros.
     */
    public int obterMaiorId() {
        return dao.obterMaiorId();
    }
}
