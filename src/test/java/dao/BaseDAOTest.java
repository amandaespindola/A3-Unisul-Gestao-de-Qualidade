package dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import utils.ConexaoManager;

class BaseDAOTest {

	private BaseDAO<Object> dao;

	@BeforeEach
	void setUp() throws Exception {

		ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");

		ConexaoManager.close();
		ConexaoManager.init("", "");

		Connection conn = ConexaoManager.getConnection();
		System.out.println("URL BaseDAO: " + conn.getMetaData().getURL());
		System.out.println("Driver BaseDAO: " + conn.getMetaData().getDriverName());
		System.out.println("Conexão hash: " + conn.hashCode()); // para ver se é a mesma conexão
		criarTabelasSQLite(conn);

		// criando implementação concreta mínima
		dao = new BaseDAO<Object>(conn) {
			@Override
			protected String getNomeTabela() {
				return "tb_alunos";
			}

			@Override
			public boolean insert(Object objeto) {
				return false;
			}

			@Override
			public boolean update(Object objeto) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};
	}

	private void criarTabelasSQLite(Connection conn) throws SQLException {
		try (Statement st = conn.createStatement()) {
			st.execute(
					"CREATE TABLE IF NOT EXISTS tb_alunos (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "nome TEXT)");

			st.execute("CREATE TABLE IF NOT EXISTS tb_professores (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "nome TEXT)");
		}
	}

	// testar conexao

	@Test
	void testGetConexao() {
		Connection conn = dao.getConexao();
		assertNotNull(conn);
	}

	@Test
	void testAbrirConexao() {
		Connection c = dao.abrirConexao();
		assertNotNull(c);
	}

	@Test
	void testGetConexaoRetornaNull() {
		BaseDAO<Object> daoNulo = new BaseDAO<Object>(null) {
			@Override
			protected String getNomeTabela() {
				return "tb_alunos";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}

			@Override
			protected Connection getConexao() {
				return null;
			}
		};

		Connection c = daoNulo.getConexao();
		assertNull(c);
	}

	@Test
	void testGetConexaoLoggerSevereChamado() {
		// getConexao() vai retornar null diretamente
		BaseDAO<Object> daoFake = new BaseDAO<Object>() {

			@Override
			protected Connection getConexao() {
				return null; // força a linha logger.severe()
			}

			@Override
			protected String getNomeTabela() {
				return "tb_alunos";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		Connection conn = daoFake.getConexao();
		assertNull(conn); // cobre o return null após o logger
	}

	@Test
	void testFecharConexaoSeInternaFechaConexao() throws Exception {
		BaseDAO<Object> daoInterno = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_alunos";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		Connection c = daoInterno.getConexao();
		daoInterno.fecharConexaoSeInterna(c);

		assertTrue(c.isClosed());
	}

	@Test
	void testFecharConexaoSeInterna() throws SQLException {
		Connection conn = dao.getConexao();
		dao.fecharConexaoSeInterna(conn);
		assertFalse(conn.isClosed()); //
	}

	// testar método obterMaiorId

	@Test
	void testObterMaiorIdAlunoTabelaVazia() {
		int maiorId = dao.obterMaiorId();
		assertEquals(0, maiorId);
	}

	@Test
	void testObterMaiorIdAlunoComRegistros() throws Exception {
		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("INSERT INTO tb_alunos (nome) VALUES ('João')");
			st.execute("INSERT INTO tb_alunos (nome) VALUES ('Maria')");
		}

		int maiorId = dao.obterMaiorId();
		assertEquals(2, maiorId);
	}

	@Test
	void testObterMaiorIdProfessorComRegistros() throws Exception {
		BaseDAO<Object> daoProf = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_professores";
			}

			@Override
			public boolean insert(Object objeto) {
				return false;
			}

			@Override
			public boolean update(Object objeto) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("INSERT INTO tb_professores (nome) VALUES ('Prof A')");
			st.execute("INSERT INTO tb_professores (nome) VALUES ('Prof B')");
			st.execute("INSERT INTO tb_professores (nome) VALUES ('Prof C')");
		}

		int maiorId = daoProf.obterMaiorId();
		assertEquals(3, maiorId);
	}

	@Test
	void testObterMaiorIdConexaoFechada() throws Exception {
		Connection conn = ConexaoManager.getConnection();
		conn.close();

		int maiorId = dao.obterMaiorId();
		assertEquals(0, maiorId);
	}

	@Test
	void testObterMaiorIdTabelaNaoPermitida() {

		BaseDAO<Object> daoInvalido = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_invalida";
			}

			@Override
			public boolean insert(Object objeto) {
				return false;
			}

			@Override
			public boolean update(Object objeto) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		assertThrows(IllegalArgumentException.class, daoInvalido::obterMaiorId);
	}

	@Test
	void testGetConexaoUsandoConexaoManager() {
		// Cria DAO sem conexão injetada
		BaseDAO<Object> daoSemConn = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_aluno";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		Connection c = daoSemConn.getConexao();
		assertNotNull(c);
	}

	@Test
	void testFecharConexaoSeInternaLancaSQLException() throws Exception {

		Connection connMock = Mockito.mock(Connection.class);
		Mockito.doThrow(new SQLException("erro")).when(connMock).close();

		BaseDAO<Object> daoErro = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_aluno";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		assertDoesNotThrow(() -> daoErro.fecharConexaoSeInterna(connMock));
	}

	@Test
	void testObterMaiorIdNenhumRegistroComResultSetMock() throws Exception {

		// Forçar rs.next() = false
		ResultSet rsMock = Mockito.mock(ResultSet.class);
		Mockito.when(rsMock.next()).thenReturn(false);

		PreparedStatement stmtMock = Mockito.mock(PreparedStatement.class);
		Mockito.when(stmtMock.executeQuery()).thenReturn(rsMock);

		Connection connMock = Mockito.mock(Connection.class);
		Mockito.when(connMock.isClosed()).thenReturn(false);
		Mockito.when(connMock.prepareStatement(Mockito.anyString())).thenReturn(stmtMock);

		BaseDAO<Object> daoMock = new BaseDAO<Object>(connMock) {
			@Override
			protected String getNomeTabela() {
				return "tb_aluno";
			}

			@Override
			public boolean insert(Object o) {
				return false;
			}

			@Override
			public boolean update(Object o) {
				return false;
			}

			@Override
			public boolean delete(int id) {
				return false;
			}

			@Override
			public Object findById(int id) {
				return null;
			}
		};

		int maior = daoMock.obterMaiorId();

		assertEquals(0, maior); // cobre o ELSE do rs.next()
	}

}
