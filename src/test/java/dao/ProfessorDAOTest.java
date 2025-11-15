package dao;

import model.Professor;
import model.ProfessorDTO;
import utils.ConexaoManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProfessorDAOTest {
	private ProfessorDAO dao;

	@BeforeEach
	void setUp() throws Exception {

		// Configurar SQLite
		ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
		ConexaoManager.setDriverClass("org.sqlite.JDBC");

		ConexaoManager.close();
		ConexaoManager.init("", "");

		Connection conn = ConexaoManager.getConnection();
		criarTabelaSQLite(conn);

		dao = new ProfessorDAO(conn);
	}

	private void criarTabelaSQLite(Connection conn) throws SQLException {
		try (Statement stm = conn.createStatement()) {
			stm.execute("CREATE TABLE IF NOT EXISTS tb_professores (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "nome TEXT, " + "idade INTEGER, " + "campus TEXT, " + "cpf TEXT, " + "contato TEXT, "
					+ "titulo TEXT, " + "salario REAL)");
		}
	}

	private Professor criarProfessorFake(String nome, String cpf) {
		ProfessorDTO dto = new ProfessorDTO();
		dto.setNome(nome);
		dto.setIdade(40);
		dto.setCampus("Campus A");
		dto.setCpf(cpf);
		dto.setContato("9999-9999");
		dto.setTitulo("Doutor");
		dto.setSalario(8000.0);

		return new Professor(dto);
	}

	// insert
	@Test
	void testInsert() {
		Professor p = criarProfessorFake("Ana", "11111111111");
		boolean inserido = dao.insert(p);

		assertTrue(inserido);
		assertTrue(p.getId() > 0);
	}

	// findById
	@Test
	void testFindById() {
		Professor p = criarProfessorFake("Carlos", "22222222222");
		dao.insert(p);

		Professor encontrado = dao.findById(p.getId());
		assertNotNull(encontrado);
		assertEquals("Carlos", encontrado.getNome());
		assertEquals("22222222222", encontrado.getCpf());
	}

	@Test
	void testFindByIdNaoExistente() {
		Professor inexistente = dao.findById(999);
		assertNull(inexistente);
	}

	// update
	@Test
	void testUpdate() {
		Professor p = criarProfessorFake("João", "33333333333");
		dao.insert(p);

		p.setNome("João Modificado");
		p.setSalario(9000.0);

		boolean atualizado = dao.update(p);
		assertTrue(atualizado);

		Professor novo = dao.findById(p.getId());
		assertEquals("João Modificado", novo.getNome());
		assertEquals(9000.0, novo.getSalario());
	}

	// delete
	@Test
	void testDelete() {
		Professor p = criarProfessorFake("Maria", "44444444444");
		dao.insert(p);

		boolean deletado = dao.delete(p.getId());
		assertTrue(deletado);

		assertNull(dao.findById(p.getId()));
	}

	// getMinhaLista
	@Test
	void testGetMinhaLista() {
		dao.insert(criarProfessorFake("Ana", "55555555555"));
		dao.insert(criarProfessorFake("Beatriz", "66666666666"));
		dao.insert(criarProfessorFake("Carla", "77777777777"));

		List<Professor> lista = dao.getMinhaLista();
		assertEquals(3, lista.size());
	}

	// existeCpf
	@Test
	void testExisteCpf() {
		dao.insert(criarProfessorFake("Marcos", "88888888888"));

		assertTrue(dao.existeCpf("88888888888"));
		assertFalse(dao.existeCpf("99999999999"));
	}

	@Test
	void testExisteCpfIgnorandoId() {
		Professor p1 = criarProfessorFake("Otávio", "00011122233");
		dao.insert(p1);

		// mesmo cpf, mas ignorando o próprio id -> NÃO deve acusar duplicidade
		assertFalse(dao.existeCpf("00011122233", p1.getId()));

		// outro professor com mesmo CPF -> deve acusar duplicidade
		Professor p2 = criarProfessorFake("Pedro", "00011122233");
		dao.insert(p2);

		assertTrue(dao.existeCpf("00011122233", p1.getId()));
	}
}
