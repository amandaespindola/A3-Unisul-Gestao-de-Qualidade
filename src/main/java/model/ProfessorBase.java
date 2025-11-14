package model;

/**
 * Classe base para representar informações comuns de um professor.
 * <p>
 * Esta classe estende {@link Pessoa} e adiciona atributos específicos relacionados
 * ao vínculo e formação do professor, como campus, CPF, contato, título acadêmico
 * e salário.
 * </p>
 */ 
public class ProfessorBase extends Pessoa {
        
        /** Campus onde o professor atua. */
	protected String campus;
        
        /** CPF do professor. */
	protected String cpf;
        
        /** Informações de contato do professor. */
	protected String contato;
        
        /** Título acadêmico do professor (ex.: Mestre, Doutor). */
	protected String titulo;
        
        /** Salário atual do professor. */
	protected double salario;

        /**
        * Construtor padrão.
        * Inicializa a classe com valores padrão de {@link Pessoa}.
        */
	public ProfessorBase() {
		super();
	}

        /**
        * Construtor que inicializa os dados herdados de {@link Pessoa}.
        *
        * @param id    Identificador único do professor.
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
