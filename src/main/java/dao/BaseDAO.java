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

    protected BaseDAO(Connection conexao) {
        this.conexao = conexao;
        this.conexaoExterna = true;
    }

    protected Connection getConexao() {
        if (this.conexao != null) {
            return this.conexao; // conexão injetada (testes)
        }

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

        // CORRIGIDO: nomes das tabelas agora estão corretos
        switch (nomeTabela) {
            case "tb_professores":
                sql = "SELECT MAX(id) AS max_id FROM tb_professores";
                break;
            case "tb_alunos":
                sql = "SELECT MAX(id) AS max_id FROM tb_alunos";
                break;
            default:
                logger.log(Level.SEVERE, "Tabela não permitida: {0}", nomeTabela);
                throw new IllegalArgumentException("Tabela não permitida: " + nomeTabela);
        }

        try ( Connection conn = getConexao()) {
            if (conn == null || conn.isClosed()) {
                logger.log(Level.SEVERE, "Conexão nula ou fechada ao buscar maior ID de {0}.", nomeTabela);
                return 0;
            }

            try ( PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("max_id");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao buscar maior ID na tabela " + nomeTabela, e);
        }

        return 0;
    }

    protected abstract String getNomeTabela();

    public abstract boolean insert(T objeto);

    public abstract boolean update(T objeto);

    public abstract boolean delete(int id);

    public abstract T findById(int id);
}
