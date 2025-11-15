package utils;

import java.sql.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DaoUtils {

    private static final Logger logger = Logger.getLogger(DaoUtils.class.getName());

    private DaoUtils() {
    }

    public static boolean tratarInsercao(
            PreparedStatement stmt,
            Object objeto,
            String tipo,
            java.util.function.IntConsumer setId) throws SQLException {

        int linhasAfetadas = stmt.executeUpdate();

        if (linhasAfetadas > 0) {
            try ( ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int novoId = rs.getInt(1);
                    setId.accept(novoId);
                    logger.info(() -> tipo + " inserido com sucesso: " + objeto.getClass().getSimpleName() + " ID " + novoId);
                }
            }
            return true;
        }

        logger.warning(() -> "Nenhuma linha inserida para " + tipo.toLowerCase());
        return false;
    }

    public static boolean executarDelete(Connection conn, String sql, int id, String tipo,
            Consumer<Connection> fecharConexao) {

        if (conn == null) {
            logger.warning("Conexão nula ao tentar deletar " + tipo.toLowerCase());
            return false;
        }

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info(() -> tipo + " deletado: ID " + id);
            return true;

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao deletar " + tipo.toLowerCase() + " " + id, ex);
            return false;

        } finally {
            fecharConexao.accept(conn);
        }
    }

    public static boolean tratarErroUpdate(String tipo, Object id, SQLException ex,
            Connection conn, Consumer<Connection> fecharConexao) {

        logger.log(Level.SEVERE, "Erro ao atualizar " + tipo.toLowerCase() + " " + id, ex);
        fecharConexao.accept(conn);
        return false;
    }

    public static void logErro(String acao, String tipo, Object identificador, SQLException ex) {
        Logger.getLogger(tipo + "DAO").log(
                Level.SEVERE,
                "Erro ao " + acao + " " + tipo.toLowerCase() + " " + identificador,
                ex
        );
    }
}
