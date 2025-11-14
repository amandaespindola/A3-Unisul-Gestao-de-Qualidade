package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para operações comuns em DAOs, como inserção,
 * deleção, tratamento de erros e logging padronizado.
 * 
 * <p>Fornece métodos genéricos que reduzem repetição de código nas 
 * implementações concretas de DAOs.</p>
 */
public class DaoUtils {

	private static final Logger logger = Logger.getLogger(DaoUtils.class.getName());

	private DaoUtils() {
		// Evita instanciação
	}

        /**
        * Executa uma instrução de inserção e trata a captura do ID gerado
        * automaticamente pelo banco de dados.
        *
        * @param stmt      PreparedStatement já configurado para inserção.
        * @param objeto    Objeto sendo inserido (usado apenas para log).
        * @param tipo      Nome do tipo de entidade, usado nas mensagens.
        * @param setId     Função que recebe o ID gerado e o define no objeto.
        * @return {@code true} se a inserção afetou ao menos uma linha, {@code false} caso contrário.
        * @throws SQLException caso ocorra erro ao executar a query.
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
        * Executa uma operação de deleção com base no ID da entidade.
        *
        * @param conn            Conexão ativa com o banco.
        * @param sql             SQL contendo o comando DELETE com placeholder para ID.
        * @param id              Identificador da entidade a ser removida.
        * @param tipo            Tipo de entidade (para logs).
        * @param fecharConexao   Função responsável por fechar/limpar a conexão.
        * @return {@code true} se o DELETE foi bem-sucedido; {@code false} caso contrário.
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
        * Trata erros ocorridos durante operações de atualização (UPDATE),
        * garantindo fechamento seguro da conexão.
        *
        * @param tipo            Tipo da entidade modificada.
        * @param identificador   Identificador da entidade (ID, CPF, etc.)
        * @param ex              Exceção SQL capturada.
        * @param conn            Conexão utilizada na operação.
        * @param fecharConexao   Função para fechamento seguro da conexão.
        * @return sempre {@code false}, permitindo uso direto em retornos de métodos DAO.
        */
	public static boolean tratarErroUpdate(String tipo, Object identificador, SQLException ex, Connection conn,
			Consumer<Connection> fecharConexao) {
		logger.log(Level.SEVERE, ex, () -> "Erro ao atualizar " + tipo.toLowerCase() + " " + identificador);
		fecharConexao.accept(conn);
		return false;
	}

        /**
        * Registra um erro genérico de operação DAO no logger correspondente,
        * sem encerrar conexões.
        *
        * @param acao           Ação executada (ex.: "buscar", "inserir", "excluir").
        * @param tipo           Tipo da entidade.
        * @param identificador  Identificador utilizado na operação.
        * @param ex             Exceção capturada.
        */
	public static void logErro(String acao, String tipo, Object identificador, SQLException ex) {
		Logger.getLogger(tipo + "DAO").log(Level.SEVERE, ex,
				() -> "Erro ao " + acao + " " + tipo.toLowerCase() + " " + identificador);
	}
}
