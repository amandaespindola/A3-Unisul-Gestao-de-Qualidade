package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária contendo métodos auxiliares para operações comuns em DAOs,
 * como tratamento de inserções, exclusões, atualizações e registro de erros.
 * 
 * <p>
 * Esta classe não deve ser instanciada.
 * </p>
 */
public class DaoUtils {

	private static final Logger logger = Logger.getLogger(DaoUtils.class.getName());

	/**
     * Construtor privado para impedir instanciação.
     */
	private DaoUtils() {
		// Evita instanciação
	}

	/**
     * Executa uma instrução de inserção (INSERT) e tenta recuperar o ID gerado
     * automaticamente pelo banco de dados, caso exista. Em seguida, esse ID é
     * definido no objeto correspondente por meio do callback fornecido.
     *
     * @param stmt    PreparedStatement já configurado com os parâmetros da
     *                inserção. Deve estar preparado para retornar as generated
     *                keys.
     * @param objeto  objeto que está sendo inserido, utilizado apenas para logs.
     * @param tipo    nome genérico da entidade (ex.: "Aluno", "Professor"), usado
     *                nas mensagens de log.
     * @param setId   função que recebe o ID gerado (IntConsumer) e o define no
     *                objeto.
     * @return {@code true} se pelo menos uma linha foi afetada; {@code false} caso
     *         contrário.
     * @throws SQLException se ocorrer algum erro ao executar o comando SQL.
     */
	public static boolean tratarInsercao(PreparedStatement stmt, Object objeto, String tipo,
			java.util.function.IntConsumer setId) throws SQLException {
		int linhasAfetadas = stmt.executeUpdate();
		if (linhasAfetadas > 0) {
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					int novoId = rs.getInt(1);
					setId.accept(novoId); // define o ID no objeto
					logger.info(() -> tipo + " inserido com sucesso: " + objeto.getClass().getSimpleName() + " ID "
							+ novoId);
				}
			}
			return true;
		} else {
			logger.warning(() -> "Nenhuma linha inserida para o " + tipo.toLowerCase());
			return false;
		}
	}

	/**
     * Executa uma operação de exclusão (DELETE) com base em um ID e registra a
     * operação. Após a execução, a conexão é fechada utilizando o {@code Consumer}
     * fornecido.
     *
     * @param conn            conexão ativa com o banco de dados.
     * @param sql             comando SQL contendo um parâmetro para o ID.
     * @param id              identificador do registro a ser removido.
     * @param tipo            nome da entidade (ex.: "Aluno", "Professor"), usado
     *                        para logs.
     * @param fecharConexao   função que recebe a conexão e realiza o fechamento.
     * @return {@code true} se o delete foi executado com sucesso; {@code false} em
     *         caso de erro ou conexão nula.
     */
	public static boolean executarDelete(Connection conn, String sql, int id, String tipo,
			Consumer<Connection> fecharConexao) {
		if (conn == null) {
			Logger.getLogger(tipo + "DAO").log(Level.WARNING, "Conexão nula ao tentar deletar {0}.",
					tipo.toLowerCase());
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

	/**
     * Realiza o tratamento e log de erros ocorridos durante uma operação de
     * atualização (UPDATE), garantindo que a conexão seja fechada.
     *
     * @param tipo            nome da entidade afetada.
     * @param identificador   identificador do registro que estava sendo atualizado.
     * @param ex              exceção SQL capturada.
     * @param conn            conexão ativa que deve ser fechada.
     * @param fecharConexao   função responsável pelo fechamento da conexão.
     * @return sempre retorna {@code false}, indicando falha na operação.
     */
	public static boolean tratarErroUpdate(String tipo, Object identificador, SQLException ex, Connection conn,
			Consumer<Connection> fecharConexao) {
		logger.log(Level.SEVERE, ex, () -> "Erro ao atualizar " + tipo.toLowerCase() + " " + identificador);
		fecharConexao.accept(conn);
		return false;
	}

	/**
     * Registra um erro genérico relacionado a uma operação específica de DAO,
     * incluindo o tipo da entidade e o identificador do registro.
     *
     * @param acao            descrição da operação (ex.: "inserir", "buscar",
     *                        "atualizar").
     * @param tipo            nome da entidade sendo manipulada.
     * @param identificador   identificação do registro envolvido na operação.
     * @param ex              exceção que foi lançada.
     */
	public static void logErro(String acao, String tipo, Object identificador, SQLException ex) {
		Logger.getLogger(tipo + "DAO").log(Level.SEVERE, ex,
				() -> "Erro ao " + acao + " " + tipo.toLowerCase() + " " + identificador);
	}
}
