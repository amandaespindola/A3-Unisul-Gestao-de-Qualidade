package utils;

import java.sql.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoUtils {

	private static final Logger logger = Logger.getLogger(DaoUtils.class.getName());

	private DaoUtils() {
		// Evita instanciação
	}

	public static boolean tratarInsercao(PreparedStatement stmt, Object objeto, String tipo,
			java.util.function.IntConsumer setId) throws SQLException {
		int linhasAfetadas = stmt.executeUpdate();
		if (linhasAfetadas > 0) {
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					int novoId = rs.getInt(1);
					setId.accept(novoId); // define o ID no objeto
					logger.info(() -> tipo + " inserido com sucesso: " + objeto.getClass().getSimpleName() + " ID " + novoId);
				}
			}
			return true;
		} else {
			logger.warning(() -> "Nenhuma linha inserida para o " + tipo.toLowerCase());
			return false;
		}
	}

	public static boolean executarDelete(Connection conn, String sql, int id, String tipo,
			Consumer<Connection> fecharConexao) {
		if (conn == null) {
			Logger.getLogger(tipo + "DAO").warning("Conexão nula ao tentar deletar " + tipo.toLowerCase() + ".");
			return false;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
			logger.info(() -> tipo + " deletado: ID " + id);
			return true;
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex, () -> "Erro ao deletar " + tipo.toLowerCase() + " " + id);
			return false;
		} finally {
			fecharConexao.accept(conn);
		}
	}

	public static boolean tratarErroUpdate(String tipo, Object identificador, SQLException ex, Connection conn,
			Consumer<Connection> fecharConexao) {
		logger.log(Level.SEVERE, ex, () -> "Erro ao atualizar " + tipo.toLowerCase() + " " + identificador);
		fecharConexao.accept(conn);
		return false;
	}

	public static void logErro(String acao, String tipo, Object identificador, SQLException ex) {
		Logger.getLogger(tipo + "DAO").log(Level.SEVERE, ex,
				() -> "Erro ao " + acao + " " + tipo.toLowerCase() + " " + identificador);
	}
}
