package model;

import java.util.List;

import dao.AlunoDAO;

/**
 * Representa um aluno, armazenando informações como curso e fase,
 * além de herdar os atributos básicos da classe {@link Pessoa}.
 * 
 * Esta classe também encapsula operações de CRUD por meio de um
 * {@link AlunoDAO}, servindo como uma camada intermediária entre 
 * a interface e o banco de dados.
 */
public class Aluno extends Pessoa {

    /** Curso no qual o aluno está matriculado. */
    private String curso;    private String curso;
    
    /** Fase (semestre) atual do aluno. */
    private int fase;
    
    /** DAO responsável pelas operações de persistência de alunos. */
    private final AlunoDAO dao;

    /**
     * Construtor padrão. Inicializa o DAO.
     */
    public Aluno() {
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor para criação de novo aluno sem ID definido.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase (semestre) do aluno.
     */
    public Aluno(String curso, int fase) {
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    /**
     * Construtor completo utilizado quando o aluno já existe no banco.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase do aluno.
     * @param id    ID do aluno.
     * @param nome  Nome do aluno.
     * @param idade Idade do aluno.
     */
    public Aluno(String curso, int fase, int id, String nome, int idade) {
        super(id, nome, idade);
        this.curso = curso;
        this.fase = fase;
        this.dao = new AlunoDAO();
    }

    /** @return Curso do aluno. */
    public String getCurso() {
        return curso;
    }

    /**
     * Define o curso do aluno.
     *
     * @param curso Nome do curso.
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /** @return Fase atual do aluno. */
    public int getFase() {
        return fase;
    }

    /**
     * Define a fase atual do aluno.
     *
     * @param fase Número da fase (semestre).
     */
    public void setFase(int fase) {
        this.fase = fase;
    }

    /**
     * Retorna uma representação textual do aluno, incluindo seus atributos
     * herdados e específicos.
     *
     * @return String formatada com os dados do aluno.
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
     * @return Lista de alunos.
     */
    public List<Aluno> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Insere um novo aluno no banco de dados.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase do aluno.
     * @param nome  Nome do aluno.
     * @param idade Idade do aluno.
     * @return {@code true} se o aluno foi inserido com sucesso.
     */
    public boolean inserirAlunoBd(String curso, int fase, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, 0, nome, idade);
        return dao.insert(objeto);
    }

    /**
     * Deleta um aluno do banco de dados com base em seu ID.
     *
     * @param id ID do aluno.
     * @return {@code true} sempre, pois o método não verifica resultado.
     */
    public boolean deletarAlunoBD(int id) {
        dao.delete(id);
        return true;
    }

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param curso Curso do aluno.
     * @param fase  Fase do aluno.
     * @param id    ID do aluno.
     * @param nome  Nome do aluno.
     * @param idade Idade do aluno.
     * @return {@code true} sempre, pois o método não verifica resultado.
     */
    public boolean atualizarAlunoBD(String curso, int fase, int id, String nome, int idade) {
        Aluno objeto = new Aluno(curso, fase, id, nome, idade);
        dao.update(objeto);
        return true;
    }

    /**
     * Busca um aluno no banco de dados pelo ID.
     *
     * @param id ID do aluno.
     * @return Objeto {@link Aluno} encontrado ou {@code null} caso não exista.
     */
    public Aluno carregaAluno(int id) {
        dao.findById(id);
        return null;
    }

    /**
     * Obtém o maior ID de aluno existente na base de dados.
     *
     * @return Maior valor de ID encontrado.
     */
    public int obterMaiorId() {
        return dao.obterMaiorId();
    }
}
