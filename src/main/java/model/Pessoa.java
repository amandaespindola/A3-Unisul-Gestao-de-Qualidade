package model;

/**
 * Representa uma pessoa genérica dentro do sistema.
 * <p>
 * Esta classe serve como base para entidades mais específicas, como {@link Aluno}
 * ou {@link Professor}, fornecendo atributos comuns como id, nome e idade.
 * </p>
 */
public abstract class Pessoa {

	/** Identificador único da pessoa. */
    private int id;
    
    /** Nome completo da pessoa. */
    private String nome;
    
    /** Idade da pessoa. */
    private int idade;

    /**
     * Construtor padrão.
     */
    protected Pessoa() {
    }

    /**
     * Construtor completo.
     *
     * @param id    Identificador único.
     * @param nome  Nome da pessoa.
     * @param idade Idade da pessoa.
     */
    protected Pessoa(int id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

 // Métodos GET e SET
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

}
