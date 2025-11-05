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
		connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
		Statement stmt = connection.createStatement();
		stmt.execute(
				"CREATE TABLE tb_alunos (id INT PRIMARY KEY, nome VARCHAR(255), idade INT, curso VARCHAR(255), fase INT)");
		stmt.close();
	}

	@BeforeEach
	void setup() {
		alunoDAO = new AlunoDAO() {
			@Override
			public Connection getConexao() {
				return connection;
			}
		};
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