package model;

import java.util.List;

import dao.ProfessorDAO;

/**
 * Representa um professor dentro do sistema acadêmico.
 * <p>
 * A classe herda de {@link ProfessorBase} e adiciona operações de persistência
 * delegadas ao {@link ProfessorDAO}.
 * </p>
 */
public class Professor extends ProfessorBase {

	/** DAO responsável pelas operações de banco de dados relacionadas ao professor. */
	private final ProfessorDAO dao;
	
	/**
     * Construtor padrão. Inicializa o DAO.
     */
	public Professor() {
		this.dao = new ProfessorDAO();
	}

	/**
     * Construtor que inicializa os principais atributos do professor.
     *
     * @param campus  Campus em que o professor atua.
     * @param cpf     CPF do professor.
     * @param contato Contato do professor.
     * @param titulo  Título acadêmico.
     * @param salario Salário do professor.
     */
	public Professor(String campus, String cpf, String contato, String titulo, double salario) {
		this.campus = campus;
		this.cpf = cpf;
		this.contato = contato;
		this.titulo = titulo;
		this.salario = salario;
		this.dao = new ProfessorDAO();
	}

	/**
     * Construtor que inicializa um professor a partir de um DTO.
     *
     * @param dto Objeto {@link ProfessorDTO} contendo os dados do professor.
     */
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

	/**
     * Retorna a lista completa de professores armazenados no banco de dados.
     *
     * @return Lista de objetos {@link Professor}.
     */
	public List<Professor> getMinhaLista() {
		return dao.getMinhaLista();
	}

	/**
     * Cadastra um novo professor no banco de dados.
     *
     * @param dto Objeto {@link ProfessorDTO} contendo os dados do professor.
     * @return {@code true} se a operação foi realizada com sucesso.
     */
	public boolean inserirProfessorBD(ProfessorDTO dto) {

		int id = this.obterMaiorId() + 1;
		dto.setId(id);

		Professor objeto = new Professor(dto);
		dao.insert(objeto);
		return true;
	}

	/**
     * Exclui um professor do banco de dados com base no ID informado.
     *
     * @param id Identificador único do professor a ser removido.
     * @return {@code true} se a remoção foi realizada.
     */
	public boolean deletarProfessorBD(int id) {
		dao.delete(id);
		return true;
	}

	/**
     * Atualiza os dados de um professor existente.
     *
     * @param dto Objeto {@link ProfessorDTO} contendo os dados atualizados.
     * @return {@code true} se a atualização foi bem-sucedida.
     */
	public boolean atualizarAlunoBD(ProfessorDTO dto) {

		Professor objeto = new Professor(dto);
		return dao.update(objeto);
	}

	/**
     * Carrega um professor específico a partir do banco de dados.
     *
     * @param id Identificador único do professor.
     * @return Objeto {@link Professor} correspondente ou {@code null} se não encontrado.
     */
	public Professor carregaProfessor(int id) {
		dao.findById(id);
		return null;
	}

	/**
     * Retorna o maior ID presente na tabela de professores.
     *
     * @return Maior valor de ID encontrado ou 0 caso não existam registros.
     */
	public int obterMaiorId() {
		return dao.obterMaiorId();
	}


}
