package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConexaoManager;

/**
 * Classe base para implementação de DAOs (Data Access Objects) que fornece
 * funcionalidades comuns de acesso a banco de dados, como abrir/fechar conexão
 * e operações básicas de CRUD.
 *
 * @param <T> Tipo da entidade que o DAO irá manipular.
 */
public abstract class BaseDAO<T> {

    /**
     * Logger para registrar mensagens de log da classe.
     */
    protected final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Conexão com o banco de dados.
     */
    protected Connection conexao;

    /**
     * Indica se a conexão foi fornecida externamente.
     */
    private boolean conexaoExterna = false;

    /**
     * Construtor padrão.
     */
    protected BaseDAO() {
    }

    /**
     * Abre e retorna uma conexão com o banco de dados.
     *
     * @return Conexão ativa.
     */
    public Connection abrirConexao() {
        return getConexao();
    }

    /**
     * Construtor que recebe uma conexão externa.
     *
     * @param conexao Conexão a ser utilizada pelo DAO.
     */
    protected BaseDAO(Connection conexao) {
        this.conexao = conexao;
        this.conexaoExterna = true;
    }

    /**
     * Obtém a conexão atual. Se uma conexão externa tiver sido informada,
     * retorna ela; caso contrário, obtém uma conexão do {@link ConexaoManager}.
     *
     * @return Conexão ativa ou {@code null} caso não seja possível obter.
     */
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

    /**
     * Fecha a conexão fornecida caso ela tenha sido criada internamente (ou
     * seja, não tenha sido injetada externamente).
     *
     * @param conn Conexão a ser fechada.
     */
    protected void fecharConexaoSeInterna(Connection conn) {
        if (!conexaoExterna && conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Erro ao fechar conexão interna", e);
            }
        }
    }

    /**
     * Obtém o maior ID da tabela associada ao DAO.
     *
     * @return O maior valor de ID encontrado. Retorna 0 caso não haja registros
     * ou em caso de erro.
     * @throws IllegalArgumentException caso o nome da tabela não seja
     * permitido.
     */
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
                            new Object[]{nomeTabela, maxId});
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

    /**
     * Retorna o nome da tabela no banco de dados associada ao DAO.
     *
     * @return Nome da tabela como {@link String}.
     */
    protected abstract String getNomeTabela();

    /**
     * Insere um novo registro no banco de dados.
     *
     * @param objeto Objeto a ser inserido.
     * @return {@code true} se a operação foi bem-sucedida, {@code false} caso
     * contrário.
     */
    public abstract boolean insert(T objeto);

    /**
     * Atualiza um registro existente no banco de dados.
     *
     * @param objeto Objeto contendo os dados atualizados.
     * @return {@code true} se a operação foi bem-sucedida, {@code false} caso
     * contrário.
     */
    public abstract boolean update(T objeto);

    /**
     * Exclui um registro do banco de dados com base no seu ID.
     *
     * @param id ID do registro a ser removido.
     * @return {@code true} se a operação foi bem-sucedida, {@code false} caso
     * contrário.
     */
    public abstract boolean delete(int id);

    /**
     * Busca um registro no banco de dados pelo ID.
     *
     * @param id ID do registro desejado.
     * @return Objeto encontrado ou {@code null} se não existir.
     */
    public abstract T findById(int id);
}
