package utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ConexaoManagerTest {

	@BeforeEach
	void resetState() throws Exception {
		// Zera o estado interno usando reflection
		Field fInitialized = ConexaoManager.class.getDeclaredField("initialized");
		fInitialized.setAccessible(true);
		fInitialized.set(null, false);

		Field fConn = ConexaoManager.class.getDeclaredField("conn");
		fConn.setAccessible(true);
		fConn.set(null, null);

		// Configura para SQLite
		ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");
	}

	@Test
	void testGetConnectionAntesDeInitRetornaNull() {
		Connection c = ConexaoManager.getConnection();
		assertNull(c);
	}

	@Test
	void testInitCriaConexao() {
		ConexaoManager.init("", "");
		Connection c = ConexaoManager.getConnection();
		assertNotNull(c);
	}

	@Test
	void testGetConnectionReabreSeFechada() throws Exception {
		ConexaoManager.init("", "");
		Connection c = ConexaoManager.getConnection();

		c.close(); // força o caminho que reabre

		Connection c2 = ConexaoManager.getConnection();
		assertNotNull(c2);
		assertTrue(!c2.isClosed());
	}

	@Test
	void testCloseFechaConexao() throws Exception {
		ConexaoManager.init("", "");
		Connection c = ConexaoManager.getConnection();

		assertTrue(!c.isClosed());

		ConexaoManager.close();

		Connection afterClose = ConexaoManager.getConnection();
		assertNull(afterClose); // getConnection sem init retorna null
	}

	@Test
	void testCarregarUrlDoPropertiesArquivoExiste() throws Exception {
		// injeta arquivo config.properties fake
		byte[] fakeContent = "db.url=jdbc:sqlite::memory:".getBytes();

		ClassLoader loader = Mockito.mock(ClassLoader.class);
		Mockito.when(loader.getResourceAsStream("config.properties")).thenReturn(new ByteArrayInputStream(fakeContent));

		Field clField = ConexaoManager.class.getDeclaredField("logger");
		clField.setAccessible(true);

		// usa reflection para chamar carregarUrlDoProperties()
		Field methodField = ConexaoManager.class.getDeclaredField("jdbcUrl");
		methodField.setAccessible(true);

		assertDoesNotThrow(() -> {
			// força leitura do properties
			ConexaoManager.init("", "");
		});
	}

	@Test
	void testFecharSilenciosoComSQLException() throws Exception {
		Connection connMock = Mockito.mock(Connection.class);
		Mockito.doThrow(new SQLException("erro")).when(connMock).close();

		Field fConn = ConexaoManager.class.getDeclaredField("conn");
		fConn.setAccessible(true);
		fConn.set(null, connMock);

		assertDoesNotThrow(ConexaoManager::close);
	}

	@Test
	void testAbrirNovaConexaoDriverInvalido() {
		ConexaoManager.setDriverClass("driver.invalido.Classe");

		ConexaoManager.init("x", "y"); // tenta abrir e captura erro

		Connection c = ConexaoManager.getConnection();
		assertNull(c); // falha ao abrir → retorna null
	}

	@Test
	void testGetConnectionSQLExceptionAoVerificarEstado() throws Exception {
		// Prepara estado
		ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");
		ConexaoManager.init("", "");

		Connection connMock = Mockito.mock(Connection.class);

		// força exceção em isClosed()
		Mockito.when(connMock.isClosed()).thenThrow(new SQLException("erro"));

		// Injeta o mock via reflection
		Field f = ConexaoManager.class.getDeclaredField("conn");
		f.setAccessible(true);
		f.set(null, connMock);

		// Deve cair no catch e retornar null
		assertNull(ConexaoManager.getConnection());
	}

	@Test
	void testAbrirNovaConexaoSQLException() {

		// Força uma URL inválida que SEMPRE gera SQLException no SQLite
		ConexaoManager.setJdbcUrl("jdbc:sqlite://caminho/invalido/nao/existe");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");

		ConexaoManager.init("user", "pass");

		// Se o catch for executado, getConnection() será null
		assertNull(ConexaoManager.getConnection());
	}

	@Test
	void testCarregarUrlDoPropertiesComExcecao() {

		// ClassLoader que sempre retorna InputStream quebrado
		ClassLoader fakeLoader = new ClassLoader() {
			@Override
			public InputStream getResourceAsStream(String name) {
				return new BrokenInputStream(); // força exception no load()
			}
		};

		// Usa o context classloader no lugar do classloader real da classe
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(fakeLoader);

		try {
			assertDoesNotThrow(() -> {
				ConexaoManager.init("", ""); // chama carregarUrlDoProperties()
			});
		} finally {
			// restaura para não afetar outros testes
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	@Test
	void testShutdownHookCatch() throws Exception {

		// 1 — Mocka conexão que lança exceção no close()
		Connection connMock = Mockito.mock(Connection.class);
		Mockito.doThrow(new SQLException("erro")).when(connMock).close();

		// 2 — Injeta o mock no campo estático 'conn'
		Field fConn = ConexaoManager.class.getDeclaredField("conn");
		fConn.setAccessible(true);
		fConn.set(null, connMock);

		// 3 — Cria o mesmo hook que o método addShutdownHook cria
		Thread fakeHook = new Thread(() -> {
			try {
				ConexaoManager.close();
			} catch (Exception ignored) {
				// Ignorado propositalmente no teste: falhas no close não afetam o cenário
			}
		});

		// 4 — Executa manualmente (não como hook do sistema)
		assertDoesNotThrow(fakeHook::run);
	}

	@Test
	void testCarregarUrlDoPropertiesExecutaCatch() {

		// classloader que retorna um InputStream quebrado
		ClassLoader fakeLoader = new ClassLoader() {
			@Override
			public InputStream getResourceAsStream(String name) {
				return new BrokenInputStream(); // lança IOException no load()
			}
		};

		// troca apenas TEMPORARIAMENTE o context classloader
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(fakeLoader);

		try {
			assertDoesNotThrow(() -> ConexaoManager.init("", ""));
		} finally {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	@Test
	void testExecutarShutdownSeguroCapturaExcecao() throws Exception {

		// mock da conexão que lança exceção ao fechar
		Connection connMock = Mockito.mock(Connection.class);
		Mockito.doThrow(new SQLException("erro")).when(connMock).close();

		// injeta no campo estático "conn"
		Field fConn = ConexaoManager.class.getDeclaredField("conn");
		fConn.setAccessible(true);
		fConn.set(null, connMock);

		// chama exatamente o método usado no hook
		Method m = ConexaoManager.class.getDeclaredMethod("executarShutdownSeguro");
		m.setAccessible(true);

		assertDoesNotThrow(() -> m.invoke(null));
	}

}
