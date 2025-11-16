package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton simples para gerenciar a Connection do app. - init(user, pass):
 * abre e guarda a conexão - getConnection(): retorna a conexão (reabre se caiu)
 * - close(): fecha com segurança
 */

public class ConexaoManager {
	private static final Logger logger = Logger.getLogger(ConexaoManager.class.getName());

	// URL pode vir do properties se quiser (deixa aqui por enquanto)
	private static String jdbcUrl = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";
	private static String driverClass = "com.mysql.cj.jdbc.Driver";

	private static Connection conn; // compartilhada
	private static String user; // último user usado
	private static String password; // última senha usada
	private static boolean initialized = false; // se já chamamos init()

	private static void executarShutdownSeguro() {
		try {
			close();
		} catch (Exception ignored) {
			// erro ignorado intencionalmente no shutdown
		}
	}

	private ConexaoManager() {
	}

	public static void setJdbcUrl(String url) {
		jdbcUrl = url;
	}

	public static void setDriverClass(String driver) {
		driverClass = driver;
	}

	/**
	 * Inicializa com credenciais do usuário (login). Pode ser chamado novamente
	 * (ex: trocar user/ambiente).
	 */
	public static synchronized void init(String userDB, String passwordDB) {
		user = userDB;
		password = passwordDB;

		// (Opcional) Carregar URL do config.properties, se quiser externalizar a URL
		carregarUrlDoProperties(); // seguro, ignora se não achar

		fecharSilencioso(); // fecha conexão antiga, se existir
		conn = abrirNovaConexao(user, password);
		initialized = true;

		// fecha quando a JVM encerrar
		addShutdownHook();
	}

	/**
	 * Retorna a conexão ativa. Reabre se estiver nula/fechada (desde que já tenha
	 * sido inicializado).
	 */
	public static synchronized Connection getConnection() {
		try {
			if (!initialized) {
				logger.severe("ConexaoManager.getConnection() chamado antes de init().");
				return null;
			}
			if (conn == null || conn.isClosed()) {
				conn = abrirNovaConexao(user, password);
			}
			return conn;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Falha ao verificar estado da conexão.", e);
			return null;
		}
	}

	/**
	 * Fecha a conexão atual.
	 */
	public static synchronized void close() {
		fecharSilencioso();
		initialized = false;
	}

	// ---------- privados utilitários ----------

	private static Connection abrirNovaConexao(String user, String password) {
		try {
			Class.forName(driverClass);
			Connection c = DriverManager.getConnection(jdbcUrl, user, password);
			logger.info("Conexão aberta via ConexaoManager.");
			return c;
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Driver JDBC não encontrado.", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Erro ao abrir conexão JDBC.", e);
		}
		return null;
	}

	private static void carregarUrlDoProperties() {
		try (InputStream in = ConexaoManager.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (in == null)
				return;
			Properties p = new Properties();
			p.load(in);
			String url = p.getProperty("db.url"); // opcional: adicione no seu properties
			if (url != null && !url.isBlank()) {
				jdbcUrl = url;
			}
		} catch (Exception e) {
			// silencioso: se falhar, segue com default
		}
	}

	private static void fecharSilencioso() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignored) {
				// ignorado intencionalmente: falha ao fechar conexão não é crítica no
			}
			conn = null;
		}
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(ConexaoManager::executarShutdownSeguro));
	}

}
