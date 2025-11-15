package dao;

import model.Aluno;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.ConexaoManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlunoDAOTest {

	private AlunoDAO dao;

	@BeforeEach
	void setUp() throws Exception {
		ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");

		ConexaoManager.close();
		ConexaoManager.init("", "");

		Connection conn = ConexaoManager.getConnection();	
		criarTabelaSQLite(conn);

		dao = new AlunoDAO(conn);
	}

	private void criarTabelaSQLite(Connection conn) throws SQLException {
		try (Statement stm = conn.createStatement()) {
			stm.execute("CREATE TABLE IF NOT EXISTS tb_alunos (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "nome TEXT," + "idade INTEGER," + "curso TEXT," + "fase INTEGER)");
		}
	}

	// insert
	@Test
	void testInsert() {

		Aluno a = new Aluno("Análise e Desenvolvimento de Sistemas", 4, 1, "Amanda", 22);
		boolean inserido = dao.insert(a);

		assertTrue(inserido);
		assertTrue(a.getId() > 0);
	}

	// FindById
	@Test
	void testFindById() {
		Aluno a = new Aluno("Análise e Desenvolvimento de Sistemas", 4, 1, "Laura", 25);
		dao.insert(a);

		Aluno encontrado = dao.findById(a.getId());
		assertNotNull(encontrado);
		assertEquals(a.getNome(), encontrado.getNome());
		assertEquals(a.getCurso(), encontrado.getCurso());
		assertEquals(a.getFase(), encontrado.getFase());
		assertEquals(a.getIdade(), encontrado.getIdade());
	}

	@Test
	void testFindByIdNaoExistente() {
		Aluno encontrado = dao.findById(999);
		assertNull(encontrado);
	}

	// update
	@Test
	void testUpdate() {
		Aluno a = new Aluno("Análise e Desenvolvimento de Sistemas", 5, 1, "Matheus", 20);
		dao.insert(a);

		a.setNome("Matheus Modificado");
		a.setIdade(30);

		boolean atualizado = dao.update(a);
		assertTrue(atualizado);

		Aluno novo = dao.findById(a.getId());
		assertEquals("Matheus Modificado", novo.getNome());
		assertEquals(30, novo.getIdade());
	}

	// delete
	@Test
	void testDelete() {
		Aluno a = new Aluno("Administração", 2, 1, "José", 19);
		dao.insert(a);

		boolean deletado = dao.delete(a.getId());
		assertTrue(deletado);

		assertNull(dao.findById(a.getId()));
	}

	// get minhaLista()
	@Test
	void testGetMinhaLista() {
		dao.insert(new Aluno("Ciências Contábeis", 3, 1, "Ana", 21));
		dao.insert(new Aluno("Design", 2, 1, "Julia", 20));
		dao.insert(new Aluno("Sistemas de Informação", 1, 1, "Monica", 23));

		List<Aluno> lista = dao.getMinhaLista();
		assertEquals(3, lista.size());
	}

}
