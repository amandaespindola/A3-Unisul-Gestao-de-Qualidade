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
 * <p>
 * Métodos principais:</p>
 * <ul>
 * <li>{@link #init(String, String)} — inicializa a conexão com usuário e
 * senha.</li>
 * <li>{@link #getConnection()} — retorna a conexão atual (reabre se estiver
 * fechada).</li>
 * <li>{@link #close()} — encerra a conexão ativa.</li>
 * </ul>
 *
 * <p>
 * A classe também suporta carregamento opcional da URL de conexão a partir de
 * um arquivo <code>config.properties</code> incluído no classpath.</p>
 */
public class ConexaoManager {

    private static final Logger logger = Logger.getLogger(ConexaoManager.class.getName());

    /**
     * URL JDBC do banco de dados.
     */
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";

    /**
     * Classe do driver JDBC utilizado.
     */
    private static String driverClass = "com.mysql.cj.jdbc.Driver";

    /**
     * Conexão compartilhada.
     */
    private static Connection conn;

    /**
     * Último usuário utilizado.
     */
    private static String user;

    /**
     * Última senha utilizada.
     */
    private static String password;

    /**
     * Indica se init() já foi chamado.
     */
    private static boolean initialized = false;

    /**
     * Construtor privado para impedir instanciamento externo, pois a classe
     * funciona como Singleton.
     */
    private ConexaoManager() {
    }

    /**
     * Método usado internamente no shutdown da JVM para garantir fechamento
     * seguro.
     */
    private static void executarShutdownSeguro() {
        try {
            close();
        } catch (Exception ignored) {
            // erro ignorado intencionalmente no shutdown
        }
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
     * Define manualmente a classe do driver JDBC.
     *
     * @param driver nome completo da classe do driver JDBC
     */
    public static void setDriverClass(String driver) {
        driverClass = driver;
    }

    /**
     * Inicializa o gerenciador de conexão utilizando usuário e senha
     * informados.
     *
     * @param userDB usuário do banco
     * @param passwordDB senha do banco
     */
    public static synchronized void init(String userDB, String passwordDB) {
        user = userDB;
        password = passwordDB;

        carregarUrlDoProperties();
        fecharSilencioso();
        conn = abrirNovaConexao(user, password);
        initialized = true;

        addShutdownHook();
    }

    /**
     * Retorna a conexão ativa ou cria uma nova se estiver fechada.
     * <p>
     * Se {@link #init(String, String)} não foi chamado anteriormente, retorna
     * {@code null} e registra um erro severo no log.
     * </p>
     *
     * @return Conexão JDBC ativa ou {@code null} se não inicializado ou em caso
     * de erro
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
     * Encerra a conexão atual e marca como não inicializado.
     */
    public static synchronized void close() {
        fecharSilencioso();
        initialized = false;
    }

    // =============================
    // Métodos privados utilitários
    // =============================
    /**
     * Abre uma nova conexão JDBC utilizando as credenciais fornecidas.
     * <p>
     * Carrega o driver JDBC especificado em {@link #driverClass} e tenta
     * estabelecer uma conexão com o banco usando {@link #jdbcUrl}.
     * </p>
     *
     * @param user nome de usuário do banco de dados
     * @param password senha do banco de dados
     * @return Conexão JDBC estabelecida ou {@code null} em caso de erro
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
     * Tenta carregar a URL de conexão a partir do arquivo
     * {@code config.properties}.
     * <p>
     * Se o arquivo existir no classpath e contiver a propriedade
     * {@code db.url}, a URL será atualizada. Caso contrário, mantém a URL
     * padrão. Erros de leitura são silenciosamente ignorados.
     * </p>
     */
    private static void carregarUrlDoProperties() {
        try (InputStream in = ConexaoManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                return;
            }

            Properties p = new Properties();
            p.load(in);

            String url = p.getProperty("db.url");
            if (url != null && !url.isBlank()) {
                jdbcUrl = url;
            }
        } catch (Exception ignored) {
            // silencioso
        }
    }

    /**
     * Fecha a conexão atual de forma silenciosa, ignorando exceções.
     * <p>
     * Este método é usado internamente para garantir que a conexão seja fechada
     * mesmo em cenários de erro. Define {@link #conn} como {@code null} após o
     * fechamento.
     * </p>
     */
    private static void fecharSilencioso() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
                // ignorado
            }
            conn = null;
        }
    }

    /**
     * Registra um shutdown hook na JVM para garantir fechamento adequado da
     * conexão.
     * <p>
     * O hook é executado automaticamente quando a JVM é encerrada, chamando
     * {@link #executarShutdownSeguro()} para fechar a conexão de forma segura.
     * </p>
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(ConexaoManager::executarShutdownSeguro));
    }
}
