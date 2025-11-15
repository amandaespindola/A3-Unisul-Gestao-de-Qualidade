package model;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Serve como base para classes como Aluno, Professor, etc.
 * Contém atributos gerais como id, nome e idade.
 */
public abstract class Pessoa {

    // Atributos

    /**
     * Identificador único da pessoa.
     */
    private int id;

    /**
     * Nome da pessoa.
     */
    private String nome;

    /**
     * Idade da pessoa.
     */
    private int idade;

    /**
     * Construtor protegido padrão.
     * Usado por subclasses.
     */
    protected Pessoa() {
    }

    /**
     * Construtor protegido com parâmetros.
     *
     * @param id identificador da pessoa
     * @param nome nome da pessoa
     * @param idade idade da pessoa
     */
    protected Pessoa(int id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    // Métodos GET e SET

    /**
     * Retorna o ID da pessoa.
     *
     * @return id da pessoa
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID da pessoa.
     *
     * @param id novo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome da pessoa.
     *
     * @return nome da pessoa
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da pessoa.
     *
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a idade da pessoa.
     *
     * @return idade da pessoa
     */
    public int getIdade() {
        return idade;
    }

    /**
     * Define a idade da pessoa.
     *
     * @param idade nova idade
     */
    public void setIdade(int idade) {
        this.idade = idade;
    }

}
