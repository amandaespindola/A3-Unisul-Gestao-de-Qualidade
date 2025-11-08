package DAO;

import View.TelaLogin;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public abstract class BaseDAO<T> {

    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

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
            return this.conexao; // usa conexão injetada nos testes
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";
            String user = TelaLogin.userDB;
            String password = TelaLogin.passwordDB;
            Connection connection = DriverManager.getConnection(url, user, password);

            logger.log(Level.INFO, "Conexão com o banco estabelecida (MySQL).");
            return connection;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver JDBC não encontrado.", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
        }
        return null;
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

    public int maiorID() {
        String nomeTabela = getNomeTabela();
        String sql = String.format("SELECT MAX(id) AS max_id FROM %s", nomeTabela);

        try (Connection conn = getConexao()) {
            if (conn == null || conn.isClosed()) {
                logger.log(Level.SEVERE, "Conexão nula ou fechada ao buscar maior ID de {0}.", nomeTabela);
                return 0;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    logger.log(Level.FINE, "Maior ID encontrado na tabela {0}: {1}", new Object[]{nomeTabela, maxId});
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
