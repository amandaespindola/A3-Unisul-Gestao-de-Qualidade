package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por gerenciar a conexão JDBC da aplicação utilizando o 
 * padrão Singleton. Centraliza a criação, recuperação e encerramento da 
 * {@link Connection}, garantindo que a aplicação utilize sempre uma única 
 * instância de conexão ativa.
 *
 * <p>Métodos principais:</p>
 * <ul>
 *   <li>{@link #init(String, String)} — inicializa a conexão com usuário e senha.</li>
 *   <li>{@link #getConnection()} — retorna a conexão atual (reabre se estiver fechada).</li>
 *   <li>{@link #close()} — encerra a conexão ativa.</li>
 * </ul>
 *
 * <p>A classe também suporta carregamento opcional da URL de conexão a partir
 * de um arquivo <code>config.properties</code> incluído no classpath.</p>
 */
public class ConexaoManager {
	private static final Logger logger = Logger.getLogger(ConexaoManager.class.getName());

	/** URL JDBC do banco de dados. */
	private static String jdbcUrl = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";
	
	/** Classe do driver JDBC utilizado. */
	private static String driverClass = "com.mysql.cj.jdbc.Driver";

	/** Conexão compartilhada. */
	private static Connection conn; // compartilhada
	
	/** Último usuário usado para conexão. */
	private static String user; // último user usado
	
	/** Última senha usada para conexão. */
	private static String password; // última senha usada
	
	/** Indica se {@link #init(String, String)} já foi executado. */
	private static boolean initialized = false; // se já chamamos init()

	/**
     * Construtor privado para impedir instanciamento externo,
     * pois a classe funciona como Singleton.
     */
	private ConexaoManager() {
	}	

	/**
     * Define manualmente a URL JDBC utilizada para conexão.
     *
     * @param url nova URL de conexão
     */
	public static void setJdbcUrl(String url) {
		jdbcUrl = url;
	}

	/**
     * Define manualmente a classe do driver JDBC a ser utilizada.
     *
     * @param driver nome completo da classe do driver JDBC
     */
	public static void setDriverClass(String driver) {
		driverClass = driver;
	}

	/**
     * Inicializa o gerenciador de conexão utilizando usuário e senha informados.
     * Este método pode ser chamado novamente caso seja necessário trocar 
     * credenciais ou ambiente.
     *
     * <p>Passos realizados:</p>
     * <ul>
     *   <li>Carrega opcionalmente a URL do banco a partir de <code>config.properties</code>.</li>
     *   <li>Fecha a conexão anterior (se existir).</li>
     *   <li>Abre uma nova conexão utilizando as credenciais fornecidas.</li>
     *   <li>Registra um shutdown hook para fechar a conexão quando a JVM encerrar.</li>
     * </ul>
     *
     * @param userDB usuário do banco de dados
     * @param passwordDB senha do banco de dados
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
     * Retorna a conexão ativa. Caso esteja nula ou fechada, uma nova conexão é
     * automaticamente aberta, desde que {@link #init(String, String)} já tenha sido chamado.
     *
     * @return a conexão ativa ou {@code null} em caso de falha
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
     * Encerra a conexão atual e marca o gerenciador como não inicializado.
     */
	public static synchronized void close() {
		fecharSilencioso();
		initialized = false;
	}

	// ============================
    // Métodos privados utilitários
    // ============================

	/**
     * Abre uma nova conexão JDBC com usuário e senha informados.
     *
     * @param user usuário do banco
     * @param password senha do banco
     * @return conexão aberta ou {@code null} se ocorrer erro
     */
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

	/**
     * Carrega a URL JDBC de um arquivo <code>config.properties</code> caso exista.
     * O método ignora silenciosamente qualquer erro ou ausência do arquivo.
     */
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

	/**
     * Fecha silenciosamente a conexão atual, ignorando eventuais erros.
     */
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

	/**
     * Registra um shutdown hook que garante o fechamento da conexão quando a JVM
     * for encerrada.
     */
	private static void addShutdownHook() {
		// registra apenas 1 vez
		// (não é crítico registrar mais de uma, mas economiza)
		// você pode guardar um flag se quiser evitar múltiplos hooks
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				close();
			} catch (Exception ignored) {
				// ignorado intencionalmente: erro ao fechar conexão no shutdown não causa
			}
		}));
	}
}