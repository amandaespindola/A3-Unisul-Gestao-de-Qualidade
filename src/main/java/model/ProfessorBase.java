package model;

public class ProfessorBase extends Pessoa {
	protected String campus;
	protected String cpf;
	protected String contato;
	protected String titulo;
	protected double salario;

	public ProfessorBase() {
		super();
	}

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
