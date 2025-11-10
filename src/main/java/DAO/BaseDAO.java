package DAO;

import View.TelaLogin;
import java.sql.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConexaoManager;

public abstract class BaseDAO<T> {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected Connection conexao;
	private boolean conexaoExterna = false;

	private static final Set<String> TABELAS_PERMITIDAS = Set.of("tb_professor", "tb_aluno");

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

		if (!TABELAS_PERMITIDAS.contains(nomeTabela)) {
			logger.log(Level.SEVERE, "Tabela não permitida: {0}", nomeTabela);
			throw new IllegalArgumentException("Tabela não permitida: " + nomeTabela);
		}

		String sql = String.format("SELECT MAX(id) AS max_id FROM %s", nomeTabela);

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
