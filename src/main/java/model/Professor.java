package model;

import java.util.List;

import dao.ProfessorDAO;

/**
 * Representa um professor, contendo informações herdadas de {@link ProfessorBase}
 * e dados específicos como campus, CPF, contato, título e salário.
 * 
 * Esta classe também funciona como camada intermediária entre a interface
 * e o banco de dados, realizando operações CRUD utilizando o {@link ProfessorDAO}.
 */
public class Professor extends ProfessorBase {

        /** DAO responsável pelas operações de persistência relacionadas a professores. */
	private final ProfessorDAO dao;
	
        /**
        * Construtor padrão. Inicializa o DAO.
        */
        public Professor() {
		this.dao = new ProfessorDAO();
	}

        /**
        * Construtor utilizado para criação de um novo professor sem ID definido.
        *
        * @param campus  Campus onde o professor atua.
        * @param cpf     CPF do professor.
        * @param contato Contato do professor.
        * @param titulo  Título acadêmico do professor.
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
        * Construtor baseado em um {@link ProfessorDTO}. 
        * Utilizado para reconstruir o objeto a partir dos dados vindos do banco de dados.
        *
        * @param dto Objeto contendo os dados do professor.
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

	

        /**
        * Retorna uma representação textual completa do professor,
        * contendo todos os seus atributos.
        *
        * @return String formatada com os dados do professor.
        */
	@Override
	public String toString() {
		return "\n ID: " + this.getId() + "\n Nome: " + this.getNome() + "\n Idade: " + this.getIdade() + "\n Campus: "
				+ this.getCampus() + "\n CPF:" + this.getCpf() + "\n Contato:" + this.getContato() + "\n Título:"
				+ this.getTitulo() + "\n Salário:" + this.getSalario() + "\n -----------";
	}

        /**
        * Retorna a lista de todos os professores cadastrados no banco.
        *
        * @return Lista de professores.
        */
        public List<Professor> getMinhaLista() {
		return dao.getMinhaLista();
	}

        /**
        * Insere um novo professor no banco de dados.
        *
        * @param dto Dados do professor a ser inserido.
        * @return {@code true} sempre, pois o método não verifica o retorno da DAO.
        */
        public boolean inserirProfessorBD(ProfessorDTO dto) {

		int id = this.obterMaiorId() + 1;
		dto.setId(id);

		Professor objeto = new Professor(dto);
		dao.insert(objeto);
		return true;
	}

	/**
        * Remove um professor com base em seu ID.
        *
        * @param id ID do professor a ser removido.
        * @return {@code true} sempre, pois o método não verifica o retorno da DAO.
        */
	public boolean deletarProfessorBD(int id) {
		dao.delete(id);
		return true;
	}

	/**
        * Atualiza os dados de um professor existente.
        *
        * @param dto Objeto contendo os novos dados do professor.
        * @return {@code true} se o professor foi atualizado com sucesso.
        */
	public boolean atualizarAlunoBD(ProfessorDTO dto) {

		Professor objeto = new Professor(dto);
		return dao.update(objeto);
	}

	/**
        * Carrega os dados de um professor específico a partir do ID.
        *
        * @param id ID do professor.
        * @return Objeto {@link Professor} encontrado ou {@code null} caso não exista.
        */
	public Professor carregaProfessor(int id) {
		dao.findById(id);
		return null;
	}

	/**
        * Obtém o maior ID de professor presente na base de dados.
        *
        * @return Maior valor de ID encontrado.
        */
	public int obterMaiorId() {
		return dao.obterMaiorId();
	}


}
