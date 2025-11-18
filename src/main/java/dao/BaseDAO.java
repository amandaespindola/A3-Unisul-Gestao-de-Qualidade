package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConexaoManager;

/**
 * Classe base para DAOs responsáveis por operações de persistência.
 *
 * <p>
 * Fornece mecanismos comuns como controle de conexão, logging e métodos
 * utilitários para fechamento de conexões. As subclasses devem implementar
 * métodos CRUD e informar o nome da tabela correspondente.
 * </p>
 *
 * @param <T> Tipo da entidade manipulada pelo DAO.
 */
public abstract class BaseDAO<T> {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected Connection conexao;
	private boolean conexaoExterna = false;

	/**
     * Construtor padrão que permite ao DAO utilizar uma conexão interna
     * obtida via {@link ConexaoManager}.
     */
	protected BaseDAO() {
	}

	/**
     * Retorna uma conexão ativa para uso do DAO.
     *
     * @return Uma instância de {@link Connection}, interna ou externa.
     */
	public Connection abrirConexao() {
		return getConexao();
	}

	/**
     * Construtor utilizado quando uma conexão externa é injetada.
     * Esta conexão não será fechada automaticamente.
     *
     * @param conexao Conexão externa fornecida para o DAO.
     */
	protected BaseDAO(Connection conexao) {
		this.conexao = conexao;
		this.conexaoExterna = true;
	}

	/**
     * Obtém a conexão utilizada pelo DAO.
     *
     * <p>
     * Caso uma conexão externa tenha sido fornecida, ela será retornada.
     * Caso contrário, uma conexão interna será obtida através de
     * {@link ConexaoManager}.
     * </p>
     *
     * @return Conexão ativa ou {@code null} se não for possível obtê-la.
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
     * Fecha a conexão caso ela seja interna.
     *
     * <p>
     * Conexões externas não devem ser fechadas pelo DAO, portanto são ignoradas.
     * </p>
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
     * Obtém o maior ID presente na tabela associada ao DAO.
     *
     * <p>
     * Este método consulta a tabela correspondente, definida por
     * {@link #getNomeTabela()}, retornando o valor máximo encontrado
     * na coluna <code>id</code>.
     * </p>
     *
     * <p>
     * Em caso de erro, tabela não permitida ou ausência de registros,
     * retorna {@code 0}.
     * </p>
     *
     * @return O maior ID encontrado ou {@code 0} se não houver registros
     *         ou ocorrer algum erro.
     */
	public int obterMaiorId() {
		String nomeTabela = getNomeTabela();

		String sql;

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

	/**
     * Deve retornar o nome da tabela associada à entidade manipulada pelo DAO.
     *
     * @return Nome da tabela correspondente.
     */
	protected abstract String getNomeTabela();

	/**
     * Insere uma nova entidade no banco de dados.
     *
     * @param objeto Entidade a ser inserida.
     * @return {@code true} se a inserção ocorrer corretamente.
     */
	public abstract boolean insert(T objeto);

	/**
     * Atualiza uma entidade existente no banco de dados.
     *
     * @param objeto Entidade contendo os valores atualizados.
     * @return {@code true} se a atualização ocorrer corretamente.
     */
	public abstract boolean update(T objeto);

	/**
     * Exclui um registro pelo ID.
     *
     * @param id Identificador da entidade.
     * @return {@code true} se a exclusão for bem-sucedida.
     */
	public abstract boolean delete(int id);

	/**
     * Busca uma entidade pelo ID.
     *
     * @param id Identificador da entidade.
     * @return Instância encontrada ou {@code null} se não existir.
     */
	public abstract T findById(int id);
}
