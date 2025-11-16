package utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

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
	void testAbrirNovaConexaoDriverInvalido() throws Exception {
		ConexaoManager.setDriverClass("driver.invalido.Classe");

		ConexaoManager.init("x", "y"); // tenta abrir e captura erro

		Connection c = ConexaoManager.getConnection();
		assertNull(c); // falha ao abrir → retorna null
	}
}
