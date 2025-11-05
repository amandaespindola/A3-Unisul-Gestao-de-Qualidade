package dao;

import DAO.AlunoDAO;
import Model.Aluno;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

public class AlunoDAOTest {
	private static Connection connection;
	private AlunoDAO alunoDAO;

	@BeforeAll
	static void setupDatabase() throws Exception {
		// Carrega o driver SQLite explicitamente
		Class.forName("org.sqlite.JDBC");

		// Conexão com banco SQLite em memória
		connection = DriverManager.getConnection("jdbc:sqlite::memory:");

		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE tb_alunos (" + "id INTEGER PRIMARY KEY, " + "nome TEXT, " + "idade INTEGER, "
				+ "curso TEXT, " + "fase INTEGER)");
		stmt.close();
	}

	@BeforeEach
	void setup() throws SQLException {
		// Limpa a tabela antes de cada teste
		Statement stmt = connection.createStatement();
		stmt.execute("DELETE FROM tb_alunos");
		stmt.close();

		alunoDAO = new AlunoDAO() {
			@Override
			public Connection getConexao() {
				return connection;
			}
		};
	}

	@AfterAll
	static void tearDown() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	@Test
	void testInsertAlunoBD() {
		Aluno aluno = new Aluno("Engenharia", 3, 1, "João", 20);
		Assertions.assertTrue(alunoDAO.InsertAlunoBD(aluno));
	}

	@Test
	void testGetMinhaLista() {
		alunoDAO.InsertAlunoBD(new Aluno("Engenharia", 3, 1, "João", 20));
		alunoDAO.InsertAlunoBD(new Aluno("Medicina", 2, 2, "Maria", 22));

		List<Aluno> lista = alunoDAO.getMinhaLista();
		Assertions.assertEquals(2, lista.size());
	}
}