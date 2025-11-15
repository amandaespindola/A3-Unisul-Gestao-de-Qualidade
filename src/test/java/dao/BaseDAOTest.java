package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.ConexaoManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
				return "tb_aluno";
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
					"CREATE TABLE IF NOT EXISTS tb_aluno (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "nome TEXT)");

			st.execute("CREATE TABLE IF NOT EXISTS tb_professor (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
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
	void testFecharConexaoSeInterna() throws SQLException {
		Connection conn = dao.getConexao();
		dao.fecharConexaoSeInterna(conn);
		assertFalse(conn.isClosed()); //conexao externa
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
			st.execute("INSERT INTO tb_aluno (nome) VALUES ('João')");
			st.execute("INSERT INTO tb_aluno (nome) VALUES ('Maria')");
		}

		int maiorId = dao.obterMaiorId();
		assertEquals(2, maiorId);
	}

	@Test
	void testObterMaiorIdProfessorComRegistros() throws Exception {
		BaseDAO<Object> daoProf = new BaseDAO<Object>() {
			@Override
			protected String getNomeTabela() {
				return "tb_professor";
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
			st.execute("INSERT INTO tb_professor (nome) VALUES ('Prof A')");
			st.execute("INSERT INTO tb_professor (nome) VALUES ('Prof B')");
			st.execute("INSERT INTO tb_professor (nome) VALUES ('Prof C')");
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
}
