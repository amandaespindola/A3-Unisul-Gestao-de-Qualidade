package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import model.Professor;
import utils.DaoUtils;

public class ProfessorDAO extends BaseDAO<Professor> {

	private static final ArrayList<Professor> minhaLista = new ArrayList<>();
	private static final String ENTIDADE = "Professor";

	// método auxiliar professorDTO
	private Professor.ProfessorDTO mapResultSetToDTO(ResultSet res) throws SQLException {
		Professor.ProfessorDTO dto = new Professor.ProfessorDTO();
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

	public ProfessorDAO() {
	}

	public ProfessorDAO(Connection conexao) {
		super(conexao);
	}

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

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM tb_professores WHERE id=?";
		Connection conn = getConexao();
		return DaoUtils.executarDelete(conn, sql, id, ENTIDADE, this::fecharConexaoSeInterna);
	}

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

	public ArrayList<Professor> getMinhaLista() {
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

	@Override
	protected String getNomeTabela() {
		return "tb_professores";
	}

	@Override
	public int obterMaiorId() {
		return super.obterMaiorId();
	}
}
