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
 * DAO responsável por realizar operações CRUD relacionadas à entidade
 * {@link Professor}. Esta classe se comunica com a tabela
 * <code>tb_professores</code> no banco de dados e utiliza a infraestrutura
 * fornecida por {@link BaseDAO}.
 */
public class ProfessorDAO extends BaseDAO<Professor> {

        /**
        * Lista auxiliar utilizada para armazenar resultados de consultas.
        */
	private static final ArrayList<Professor> minhaLista = new ArrayList<>();
        
        /**
        * Nome da entidade utilizado para logs e mensagens de erro.
        */
	private static final String ENTIDADE = "Professor";

        /**
        * Mapeia um {@link ResultSet} para um objeto {@link model.ProfessorDTO}.
        *
        * @param res ResultSet contendo os dados do professor.
        * @return Um objeto DTO preenchido com os dados do banco.
        * @throws SQLException caso ocorra erro ao ler o ResultSet.
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
        * Construtor padrão.
        */
	public ProfessorDAO() {
	}

        /**
        * Construtor que permite injetar uma conexão externa.
        *
        * @param conexao Conexão a ser utilizada pelo DAO.
        */
	public ProfessorDAO(Connection conexao) {
		super(conexao);
	}

        /**
        * Insere um novo professor na tabela <code>tb_professores</code>.
        *
        * @param objeto Objeto do tipo {@link Professor} a ser inserido.
        * @return {@code true} se a inserção foi bem-sucedida, {@code false} caso contrário.
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
        * Atualiza um professor existente na tabela <code>tb_professores</code>.
        *
        * @param objeto Objeto contendo os dados atualizados do professor.
        * @return {@code true} se o registro foi atualizado, {@code false} caso nenhuma linha seja afetada.
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
        * Remove um professor pelo seu ID.
        *
        * @param id ID do professor a ser removido.
        * @return {@code true} se o registro foi excluído, {@code false} caso contrário.
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
        * @param id ID do professor desejado.
        * @return Instância de {@link Professor} caso encontrada, ou {@code null} caso não exista.
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
        * Retorna uma lista contendo todos os professores cadastrados.
        *
        * @return Lista de {@link Professor}.
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
        * Retorna o nome da tabela associada ao DAO.
        *
        * @return Nome da tabela <code>tb_professores</code>.
        */
	@Override
	protected String getNomeTabela() {
		return "tb_professores";
	}

        /**
        * Verifica se existe um professor cadastrado com o CPF informado.
        *
        * @param cpf CPF a ser verificado.
        * @return {@code true} se já existir um professor com esse CPF, {@code false} caso contrário.
        */
	public boolean existeCpf(String cpf) {
		return getMinhaLista().stream().anyMatch(p -> p.getCpf().equals(cpf));
	}

        /**
        * Verifica se existe um professor com o CPF informado, ignorando um ID específico
        * (útil em operações de edição).
        *
        * @param cpf CPF a ser verificado.
        * @param idIgnorado ID que deve ser desconsiderado na validação.
        * @return {@code true} se existir outro professor com o mesmo CPF, {@code false} caso contrário.
        */
	public boolean existeCpf(String cpf, int idIgnorado) {
		return getMinhaLista().stream().anyMatch(p -> p.getCpf().equals(cpf) && p.getId() != idIgnorado);
	}

}
