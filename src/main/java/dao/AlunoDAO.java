package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import model.Aluno;
import utils.DaoUtils;

public class AlunoDAO extends BaseDAO<Aluno> {

	private static final ArrayList<Aluno> MinhaLista = new ArrayList<>();
	private static final String ENTIDADE = "Aluno";

	public AlunoDAO() {
	}

	public AlunoDAO(Connection conexao) {
		super(conexao);
	}

	@Override
	public boolean insert(Aluno objeto) {
		String sql = "INSERT INTO tb_alunos (nome, idade, curso, fase) VALUES (?, ?, ?, ?)";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar inserir aluno.");
			return false;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, objeto.getNome());
			stmt.setInt(2, objeto.getIdade());
			stmt.setString(3, objeto.getCurso());
			stmt.setInt(4, objeto.getFase());

			return DaoUtils.tratarInsercao(stmt, objeto, ENTIDADE, objeto::setId);
		} catch (SQLException ex) {
			DaoUtils.logErro("inserir", ENTIDADE, objeto.getNome(), ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return false;
	}

	@Override
	public boolean update(Aluno objeto) {
		String sql = "UPDATE tb_alunos SET nome=?, idade=?, curso=?, fase=? WHERE id=?";
		Connection conn = getConexao();
		if (conn == null) {
			return false;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, objeto.getNome());
			stmt.setInt(2, objeto.getIdade());
			stmt.setString(3, objeto.getCurso());
			stmt.setInt(4, objeto.getFase());
			stmt.setInt(5, objeto.getId());
			stmt.executeUpdate();
			logger.info(() -> "Aluno atualizado: ID " + objeto.getId());
			return true;
		} catch (SQLException ex) {
			return DaoUtils.tratarErroUpdate(ENTIDADE, objeto.getId(), ex, conn, this::fecharConexaoSeInterna);
		}
	}

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM tb_alunos WHERE id=?";
		Connection conn = getConexao();
		return DaoUtils.executarDelete(conn, sql, id, ENTIDADE, this::fecharConexaoSeInterna);
	}

	@Override
	public Aluno findById(int id) {
		String sql = "SELECT id, nome, idade, curso, fase FROM tb_alunos WHERE id=?";
		Connection conn = getConexao();
		if (conn == null) {
			return null;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet res = stmt.executeQuery()) {
				if (res.next()) {
					return new Aluno(res.getString("curso"), res.getInt("fase"), res.getInt("id"),
							res.getString("nome"), res.getInt("idade"));
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Erro ao carregar aluno " + id, ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return null;
	}

	public ArrayList<Aluno> getMinhaLista() {
		MinhaLista.clear();
		String sql = "SELECT id, nome, idade, curso, fase FROM tb_alunos";
		Connection conn = getConexao();
		if (conn == null) {
			return MinhaLista;
		}

		try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
			while (res.next()) {
				MinhaLista.add(new Aluno(res.getString("curso"), res.getInt("fase"), res.getInt("id"),
						res.getString("nome"), res.getInt("idade")));
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Erro ao buscar lista de alunos", ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return MinhaLista;
	}

	@Override
	protected String getNomeTabela() {
		return "tb_alunos";
	}

	@Override
	public int obterMaiorId() {
		return super.obterMaiorId();
	}

}
