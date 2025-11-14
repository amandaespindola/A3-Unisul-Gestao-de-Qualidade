package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConexaoManager;

public abstract class BaseDAO<T> {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected Connection conexao;
	private boolean conexaoExterna = false;

	protected BaseDAO() {
	}

	public Connection abrirConexao() {
		return getConexao();
	}

	protected BaseDAO(Connection conexao) {
		this.conexao = conexao;
		this.conexaoExterna = true;
	}

	protected Connection getConexao() {
		if (this.conexao != null) {
			return this.conexao; // conexão injetada (ex.: testes)
		}
		// Pega do manager global
		Connection c = ConexaoManager.getConnection();
		if (c == null) {
			logger.severe("getConexao() não encontrou conexão global (ConexaoManager não inicializado?).");
		}
		return c;
	}

	protected void fecharConexaoSeInterna(Connection conn) {
		if (!conexaoExterna && conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Erro ao fechar conexão interna", e);
			}
		}
	}

	public int obterMaiorId() {
		String nomeTabela = getNomeTabela();

		String sql;

		switch (nomeTabela) {
		case "tb_professor":
			sql = "SELECT MAX(id) AS max_id FROM tb_professor";
			break;
		case "tb_aluno":
			sql = "SELECT MAX(id) AS max_id FROM tb_aluno";
			break;
		default:
			logger.log(Level.SEVERE, "Tabela não permitida: {0}", nomeTabela);
			throw new IllegalArgumentException("Tabela não permitida: " + nomeTabela);
		}

		try (Connection conn = getConexao()) {
			if (conn == null || conn.isClosed()) {
				logger.log(Level.SEVERE, "Conexão nula ou fechada ao buscar maior ID de {0}.", nomeTabela);
				return 0;
			}

			try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {
					int maxId = rs.getInt("max_id");
					logger.log(Level.FINE, "Maior ID encontrado na tabela {0}: {1}",
							new Object[] { nomeTabela, maxId });
					return maxId;
				} else {
					logger.log(Level.INFO, "Nenhum registro encontrado na tabela {0}. Retornando 0.", nomeTabela);
					return 0;
				}
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, String.format("Erro ao buscar maior ID da tabela %s.", nomeTabela), e);
			return 0;
		}
	}

	protected abstract String getNomeTabela();

	public abstract boolean insert(T objeto);

	public abstract boolean update(T objeto);

	public abstract boolean delete(int id);

	public abstract T findById(int id);
}
