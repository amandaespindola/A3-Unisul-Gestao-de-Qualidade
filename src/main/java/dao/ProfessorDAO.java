package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.logging.Level;

import model.Professor;
import utils.DaoUtils;
import java.util.List;

/**
 * DAO responsável pelo acesso e manipulação dos registros da entidade
 * {@link Professor} na tabela <code>tb_professores</code>.
 *
 * <p>
 * Implementa operações de CRUD utilizando a infraestrutura fornecida por
 * {@link BaseDAO}, incluindo controle de conexão e logging.
 * </p>
 */
public class ProfessorDAO extends BaseDAO<Professor> {

	private static final ArrayList<Professor> minhaLista = new ArrayList<>();
	private static final String ENTIDADE = "Professor";

	/**
     * Converte um {@link ResultSet} em um objeto {@link model.ProfessorDTO}.
     *
     * @param res ResultSet contendo os dados do professor.
     * @return Um DTO preenchido com os dados do banco.
     * @throws SQLException Caso ocorra erro ao ler os dados do ResultSet.
     */
	private model.ProfessorDTO mapResultSetToDTO(ResultSet res) throws SQLException {
		model.ProfessorDTO dto = new model.ProfessorDTO();
		dto.setCampus(res.getString("campus"));
		dto.setCpf(res.getString("cpf"));
		dto.setContato(res.getString("contato"));
		dto.setTitulo(res.getString("titulo"));
		dto.setSalario(res.getDouble("salario"));
		dto.setId(res.getInt("id"));
		dto.setNome(res.getString("nome"));
		dto.setIdade(res.getInt("idade"));
		return dto;
	}

	/**
     * Construtor padrão que utiliza a conexão obtida internamente pelo
     * {@link BaseDAO}.
     */
	public ProfessorDAO() {
	}

	/**
     * Construtor que recebe uma conexão externa, utilizada principalmente
     * para transações ou operações encadeadas.
     *
     * @param conexao Conexão fornecida externamente.
     */
	public ProfessorDAO(Connection conexao) {
		super(conexao);
	}

	/**
     * Insere um novo professor no banco de dados.
     *
     * @param objeto Instância de {@link Professor} a ser inserida.
     * @return {@code true} se a inserção for bem-sucedida; {@code false} caso contrário.
     */
	@Override
	public boolean insert(Professor objeto) {
		String sql = "INSERT INTO tb_professores (nome, idade, campus, cpf, contato, titulo, salario) VALUES (?, ?, ?, ?, ?, ?, ?)";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar inserir professor.");
			return false;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, objeto.getNome());
			stmt.setInt(2, objeto.getIdade());
			stmt.setString(3, objeto.getCampus());
			stmt.setString(4, objeto.getCpf());
			stmt.setString(5, objeto.getContato());
			stmt.setString(6, objeto.getTitulo());
			stmt.setDouble(7, objeto.getSalario());

			return DaoUtils.tratarInsercao(stmt, objeto, ENTIDADE, objeto::setId);
		} catch (SQLException ex) {
			DaoUtils.logErro("inserir", ENTIDADE, objeto.getNome(), ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return false;
	}

	/**
     * Atualiza os dados de um professor existente no banco.
     *
     * @param objeto Professor contendo os valores atualizados.
     * @return {@code true} caso a atualização seja bem-sucedida; {@code false} caso contrário.
     */
	@Override
	public boolean update(Professor objeto) {
		String sql = "UPDATE tb_professores SET nome=?, idade=?, campus=?, cpf=?, contato=?, titulo=?, salario=? WHERE id=?";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar atualizar professor.");
			return false;
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, objeto.getNome());
			stmt.setInt(2, objeto.getIdade());
			stmt.setString(3, objeto.getCampus());
			stmt.setString(4, objeto.getCpf());
			stmt.setString(5, objeto.getContato());
			stmt.setString(6, objeto.getTitulo());
			stmt.setDouble(7, objeto.getSalario());
			stmt.setInt(8, objeto.getId());

			int linhasAfetadas = stmt.executeUpdate();
			if (linhasAfetadas > 0) {
				logger.info(() -> "Professor atualizado: ID " + objeto.getId());
				return true;
			} else {
				logger.warning(() -> "Nenhuma linha atualizada para professor ID: " + objeto.getId());
				return false;
			}
		} catch (SQLException ex) {
			return DaoUtils.tratarErroUpdate(ENTIDADE, objeto.getId(), ex, conn, this::fecharConexaoSeInterna);
		}
	}

	/**
     * Remove um professor do banco pelo ID.
     *
     * @param id Identificador do professor.
     * @return {@code true} se a exclusão ocorrer com sucesso.
     */
	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM tb_professores WHERE id=?";
		Connection conn = getConexao();
		return DaoUtils.executarDelete(conn, sql, id, ENTIDADE, this::fecharConexaoSeInterna);
	}

	/**
     * Busca um professor pelo ID.
     *
     * @param id Identificador do professor.
     * @return Instância encontrada ou {@code null} caso não exista.
     */
	@Override
	public Professor findById(int id) {
		String sql = "SELECT id, nome, idade, campus, cpf, contato, titulo, salario FROM tb_professores WHERE id=?";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar buscar professor por ID.");
			return null;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet res = stmt.executeQuery()) {
				if (res.next()) {
					return new Professor(mapResultSetToDTO(res));
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex, () -> "Erro ao carregar professor " + id);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return null;
	}

	/**
     * Retorna todos os professores cadastrados.
     *
     * <p>
     * A lista é recriada a cada chamada, garantindo dados sempre atualizados.
     * </p>
     *
     * @return Lista de professores armazenados no banco.
     */
	public List<Professor> getMinhaLista() {
		minhaLista.clear();
		String sql = "SELECT id, nome, idade, campus, cpf, contato, titulo, salario FROM tb_professores";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar carregar lista de professores.");
			return minhaLista;
		}

		try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
			while (res.next()) {
				minhaLista.add(new Professor(mapResultSetToDTO(res)));

			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Erro ao buscar lista de professores", ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return minhaLista;
	}

	/**
     * Retorna o nome da tabela associada a {@link Professor}.
     *
     * @return Nome da tabela.
     */
	@Override
	protected String getNomeTabela() {
		return "tb_professores";
	}

	/**
     * Verifica se um CPF já está cadastrado.
     *
     * @param cpf CPF a ser verificado.
     * @return {@code true} se o CPF já existir na lista.
     */
	public boolean existeCpf(String cpf) {
		return getMinhaLista().stream().anyMatch(p -> p.getCpf().equals(cpf));
	}

	/**
     * Verifica se um CPF já está cadastrado, ignorando um determinado ID.
     * Usado principalmente em atualizações.
     *
     * @param cpf CPF a ser verificado.
     * @param idIgnorado ID que deve ser ignorado na verificação.
     * @return {@code true} se o CPF já existir em outro registro.
     */
	public boolean existeCpf(String cpf, int idIgnorado) {
		return getMinhaLista().stream().anyMatch(p -> p.getCpf().equals(cpf) && p.getId() != idIgnorado);
	}

}
