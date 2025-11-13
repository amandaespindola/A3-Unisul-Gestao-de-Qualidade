package model;

import java.sql.SQLException;
import java.util.*;

import dao.ProfessorDAO;

public class Professor extends ProfessorBase {

	private final ProfessorDAO dao;
	
	// Construtores
	public Professor() {
		this.dao = new ProfessorDAO();
	}

	public Professor(String campus, String cpf, String contato, String titulo, double salario) {
		this.campus = campus;
		this.cpf = cpf;
		this.contato = contato;
		this.titulo = titulo;
		this.salario = salario;
		this.dao = new ProfessorDAO();
	}

	public Professor(ProfessorDTO dto) {
		super(dto.getId(), dto.getNome(), dto.getIdade());
		this.campus = dto.getCampus();
		this.cpf = dto.getCpf();
		this.contato = dto.getContato();
		this.titulo = dto.getTitulo();
		this.salario = dto.getSalario();
		this.dao = new ProfessorDAO();
	}

	

	@Override
	public String toString() {
		return "\n ID: " + this.getId() + "\n Nome: " + this.getNome() + "\n Idade: " + this.getIdade() + "\n Campus: "
				+ this.getCampus() + "\n CPF:" + this.getCpf() + "\n Contato:" + this.getContato() + "\n Título:"
				+ this.getTitulo() + "\n Salário:" + this.getSalario() + "\n -----------";
	}

	// Retorna a Lista de Professores (objetos)
	public List<Professor> getMinhaLista() {
		return dao.getMinhaLista();
	}

	// Cadastra novo professor
	public boolean inserirProfessorBD(ProfessorDTO dto) throws SQLException {

		int id = this.obterMaiorId() + 1;
		dto.setId(id);

		Professor objeto = new Professor(dto);
		dao.insert(objeto);
		return true;
	}

	// Deleta um professor específico pelo seu campo ID
	public boolean deletarProfessorBD(int id) {
		dao.delete(id);
		return true;
	}

	// Edita um professor específico pelo seu campo ID
	public boolean atualizarAlunoBD(ProfessorDTO dto) {

		Professor objeto = new Professor(dto);
		return dao.update(objeto);
	}

	// carrega dados de um professor específico pelo seu ID
	public Professor carregaProfessor(int id) {
		dao.findById(id);
		return null;
	}

	// retorna o maior ID da nossa base de dados
	public int obterMaiorId() throws SQLException {
		return dao.obterMaiorId();
	}


}
