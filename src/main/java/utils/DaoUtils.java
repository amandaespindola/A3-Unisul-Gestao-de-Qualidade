package utils;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoUtils {

    private static final Logger logger = Logger.getLogger(DaoUtils.class.getName());

    private DaoUtils() {
        // Evita instanciação
    }

    public static boolean tratarInsercao(PreparedStatement stmt, Object objeto, String tipo, java.util.function.IntConsumer setId) throws SQLException {
        int linhasAfetadas = stmt.executeUpdate();
        if (linhasAfetadas > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int novoId = rs.getInt(1);
                    setId.accept(novoId); // define o ID no objeto
                    logger.info(() -> tipo + " inserido: ID " + novoId);
                }
            }
            return true;
        } else {
            logger.warning(() -> "Nenhuma linha inserida para o " + tipo.toLowerCase());
            return false;
        }
    }

    public static void logErro(String acao, String tipo, Object identificador, SQLException ex) {
        Logger.getLogger(tipo + "DAO").log(Level.SEVERE,
                "Erro ao " + acao + " " + tipo.toLowerCase() + " " + identificador, ex);
    }
}
