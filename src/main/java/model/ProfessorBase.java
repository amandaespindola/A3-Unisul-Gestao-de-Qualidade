package model;

/**
 * Classe base para professores do sistema acadêmico.
 * <p>
 * Fornece atributos comuns de professores, como campus, CPF, contato, título e salário.
 * Serve como superclasse para {@link Professor}.
 * </p>
 */
public class ProfessorBase extends Pessoa {
	
	/** Campus em que o professor atua. */
	protected String campus;
	
	/** CPF do professor. */
	protected String cpf;
	
	/** Contato do professor (telefone ou e-mail). */
	protected String contato;
	
	/** Título acadêmico do professor. */
	protected String titulo;
	
	/** Salário do professor. */
	protected double salario;

	/**
     * Construtor padrão.
     */
	public ProfessorBase() {
		super();
	}

	/**
     * Construtor completo com atributos da classe {@link Pessoa}.
     *
     * @param id    Identificador único.
     * @param nome  Nome do professor.
     * @param idade Idade do professor.
     */
	public ProfessorBase(int id, String nome, int idade) {
		super(id, nome, idade);
	}

	// Getters e Setters comuns
	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}
}
