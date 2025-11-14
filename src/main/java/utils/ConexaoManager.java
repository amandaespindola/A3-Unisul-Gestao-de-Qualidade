package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para gerenciamento centralizado de conexões JDBC.
 * Funciona como um singleton estático, mantendo uma única instância
 * de {@link Connection} reutilizável ao longo do ciclo da aplicação.
 * 
 * <p>Responsabilidades principais:</p>
 * <ul>
 *   <li>Inicializar conexão com credenciais do usuário ({@link #init}).</li>
 *   <li>Fornecer acesso seguro à conexão ativa ({@link #getConnection}).</li>
 *   <li>Fechar a conexão de forma controlada ({@link #close}).</li>
 *   <li>Reabrir a conexão automaticamente caso esteja fechada.</li>
 * </ul>
 *
 * <p>A URL JDBC pode ser carregada de um arquivo <code>config.properties</code>,
 * caso exista no classpath.</p>
 */
public class ConexaoManager {
	private static final Logger logger = Logger.getLogger(ConexaoManager.class.getName());

	/** URL JDBC utilizada para conexão com o banco. */
	private static String jdbcUrl = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";

        /** Instância única da conexão mantida pelo sistema. */
	private static Connection conn; // compartilhada
        
        /** Usuário utilizado para autenticação. */
	private static String user; // último user usado
        
        /** Senha utilizada para autenticação. */
	private static String password; // última senha usada
        
        /** Indica se o inicializador já foi chamado. */
	private static boolean initialized = false; // se já chamamos init()

        /** Construtor privado para impedir instanciação externa. */
	private ConexaoManager() {
	}

	/**
        * Inicializa o gerenciador com credenciais de acesso ao banco de dados.
        * Pode ser chamado novamente para trocar usuário, senha ou ambiente.
        *
        * @param userDB     Usuário do banco.
        * @param passwordDB Senha do banco.
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
        * Retorna a conexão ativa. Caso a conexão esteja nula ou fechada,
        * uma nova conexão será aberta automaticamente.
        *
        * @return Conexão ativa ou {@code null} caso não tenha sido inicializada.
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
        * Fecha a conexão atual e marca o gerenciador como não inicializado.
        */
	public static synchronized void close() {
		fecharSilencioso();
		initialized = false;
	}

	// =============================================================
        // Métodos privados utilitários
        // =============================================================

        /**
        * Abre uma nova conexão JDBC utilizando usuário e senha informados.
        *
        * @param user     Usuário do banco.
        * @param password Senha do banco.
        * @return Nova instância de {@link Connection}, ou {@code null} em caso de erro.
        */
	private static Connection abrirNovaConexao(String user, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection c = DriverManager.getConnection(jdbcUrl, user, password);
			logger.info("Conexão MySQL aberta via ConexaoManager.");
			return c;
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Driver JDBC não encontrado.", e);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Erro ao abrir conexão JDBC.", e);
		}
		return null;
	}

        /**
        * Tenta carregar a URL JDBC a partir de um arquivo <code>config.properties</code>.
        * Caso o arquivo não exista ou ocorra erro, a URL padrão é mantida.
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
        * Fecha a conexão atual sem lançar exceções.
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
        * Registra um hook para garantir que a conexão seja fechada
        * automaticamente ao encerrar a JVM.
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