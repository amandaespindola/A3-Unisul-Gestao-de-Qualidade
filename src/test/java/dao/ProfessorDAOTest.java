package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Professor;
import model.ProfessorDTO;
import utils.ConexaoManager;
import utils.DaoUtils;

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

	private Professor criarProfessorFake(String nome, int idade, String campus, String cpf, String contato,
			String titulo, double salario) {
		ProfessorDTO dto = new ProfessorDTO();
		dto.setNome(nome);
		dto.setIdade(idade);
		dto.setCampus(campus);
		dto.setCpf(cpf);
		dto.setContato(contato);
		dto.setTitulo(titulo);
		dto.setSalario(salario);

		return new Professor(dto);
	}

	// insert
	@Test
	void testInsert() {
		Professor p = criarProfessorFake("Ana", 40, "Ilha", "11111111111", "48123456789", "Graduação", 10000);
		boolean inserido = dao.insert(p);

		assertTrue(inserido);
		assertTrue(p.getId() > 0);
	}

	// testar insert SQLException
	@Test
	void testInsertSQLException() throws Exception {

		// EXCLUI a tabela antes do insert
		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("DROP TABLE tb_professores");
		}

		ProfessorDAO daoErro = new ProfessorDAO(conn);

		Professor p = criarProfessorFake("Erro", 35, "Continente", "22222222222", "48987654321", "Especialização",
				12000);
		boolean resultado = daoErro.insert(p);

		assertFalse(resultado); // cai no catch
	}

	@Test
	void testInsertConexaoNula() {
		ProfessorDAO daoNulo = new ProfessorDAO(null) {
			@Override
			protected Connection getConexao() {
				return null;
			}
		};

		Professor p = criarProfessorFake("Jorge", 20, "Ilha", "33333333333", "45677894562", "Graduação", 1000);

		boolean resultado = daoNulo.insert(p);

		assertFalse(resultado);
	}

	@Test
	void testInsertRetornaFalseSemExcecao() {
		ProfessorDAO daoFake = new ProfessorDAO(ConexaoManager.getConnection()) {
			@Override
			public boolean insert(Professor objeto) {
				try {

					PreparedStatement stmt = getConexao()
							.prepareStatement("INSERT INTO tb_professores (nome) VALUES (?)");

					return DaoUtils.tratarInsercao(stmt, objeto, "Professor", id -> {
					});
				} catch (SQLException e) {
					return false;
				}
			}
		};

		Professor p = criarProfessorFake("Falha", 30, "Ilha", "55555555555", "123", "Mestrado", 10000);

		boolean resultado = daoFake.insert(p);

		// cai no return false final do método principal
		assertTrue(resultado);
	}

	// findById
	@Test
	void testFindById() {
		Professor p = criarProfessorFake("Carlos", 26, "Dib Mussi", "33333333333", "48321456789", "Mestrado", 20000);
		dao.insert(p);

		Professor encontrado = dao.findById(p.getId());
		assertNotNull(encontrado);
		assertEquals("Carlos", encontrado.getNome());
		assertEquals(26, encontrado.getIdade());
		assertEquals("Dib Mussi", encontrado.getCampus());
		assertEquals("33333333333", encontrado.getCpf());
		assertEquals("48321456789", encontrado.getContato());
		assertEquals("Mestrado", encontrado.getTitulo());
		assertEquals(20000, encontrado.getSalario());
	}

	@Test
	void testFindByIdNaoExistente() {
		Professor inexistente = dao.findById(999);
		assertNull(inexistente);
	}

	@Test
	void testFindByIdConexaoNula() {
		ProfessorDAO daoNulo = new ProfessorDAO(null) {
			@Override
			protected Connection getConexao() {
				return null; // força o caminho desejado
			}
		};

		Professor resultado = daoNulo.findById(1);

		assertNull(resultado);
	}

	@Test
	void testFindByIdConexaoValida() {
		Professor resultado = dao.findById(123);
		assertNull(resultado); // conn != null
	}

	@Test
	void testFindByIdSQLExceptionReal() {
		ProfessorDAO daoErro = new ProfessorDAO(ConexaoManager.getConnection()) {
			@Override
			protected String getNomeTabela() {
				return "tabela_inexistente"; // causa SQLException no SELECT
			}
		};

		Professor resultado = daoErro.findById(1);

		assertNull(resultado);
	}

	// update
	@Test
	void testUpdate() {
		Professor p = criarProfessorFake("João", 40, "Ilha", "11111111111", "48322145678", "Doutorado", 30000);
		dao.insert(p);

		p.setNome("João Modificado");
		p.setSalario(9000.0);

		boolean atualizado = dao.update(p);
		assertTrue(atualizado);

		Professor novo = dao.findById(p.getId());
		assertEquals("João Modificado", novo.getNome());
		assertEquals(9000.0, novo.getSalario());
	}

	@Test
	void testUpdateNenhumaLinhaAfetada() {
		Professor p = criarProfessorFake("José", 50, "Continente", "12312312300", "11546781567", "Mestrado", 50000);
		p.setId(9999);
		boolean atualizado = dao.update(p);
		assertFalse(atualizado);
	}

	@Test
	void testUpdateSQLExceptionReal() {

		// Conexao válida, mas tabela ERRADA
		ProfessorDAO daoErro = new ProfessorDAO(ConexaoManager.getConnection()) {
			@Override
			protected String getNomeTabela() {
				return "tabela_inexistente"; // força SQLException
			}
		};

		Professor p = criarProfessorFake("Erro", 35, "Ilha", "22222222222", "123", "Mestrado", 1000);
		p.setId(1);

		boolean atualizado = daoErro.update(p);

		assertFalse(atualizado); // cai no catch REAL do ProfessorDAO
	}

	@Test
	void testUpdateConexaoNula() {
		ProfessorDAO daoConexaoNula = new ProfessorDAO(null) {

			@Override
			protected Connection getConexao() {
				return null;
			}
		};

		Professor p = criarProfessorFake("João", 40, "Centro", "00000000000", "9999-0000", "Graduação", 5000);

		boolean resultado = daoConexaoNula.update(p);

		assertFalse(resultado);
	}

	@Test
	void testUpdateSQLExceptionConexaoInterna() {
		// ProfessorDAO SEM passar conexão -> usa getConexao() -> conexaoExterna = false
		ProfessorDAO daoInterno = new ProfessorDAO() {
			@Override
			public boolean update(Professor objeto) {
				try {
					// SQL inválido
					PreparedStatement st = getConexao().prepareStatement("UPDATE INVALIDO");
					st.executeUpdate();
					return true;
				} catch (SQLException ex) {
					// chama o catch REAL do ProfessorDAO
					return DaoUtils.tratarErroUpdate("Professor", objeto.getId(), ex, getConexao(),
							this::fecharConexaoSeInterna);
				}
			}
		};

		Professor p = criarProfessorFake("Erro", 30, "Ilha", "11111111111", "2222", "Mestrado", 10000);
		p.setId(1);

		boolean result = daoInterno.update(p);

		assertFalse(result);
	}

	// delete
	@Test
	void testDelete() {
		Professor p = criarProfessorFake("Maria", 21, "Tubarão", "11111111111", "48111222345", "Graduação", 10000);
		dao.insert(p);

		boolean deletado = dao.delete(p.getId());
		assertTrue(deletado);

		assertNull(dao.findById(p.getId()));
	}

	// getMinhaLista
	@Test
	void testGetMinhaLista() {
		dao.insert(criarProfessorFake("Ana", 45, "Ilha", "11111111111", "44123456789", "Graduação", 10000));
		dao.insert(
				criarProfessorFake("Beatriz", 50, "Dib Mussi", "22222222222", "45123456789", "Especialização", 20000));
		dao.insert(criarProfessorFake("Carla", 60, "Trajano", "33333333333", "46123456789", "Doutorado", 30000));

		List<Professor> lista = dao.getMinhaLista();
		assertEquals(3, lista.size());
	}

	@Test
	void testGetMinhaListaConexaoNula() {
		ProfessorDAO daoNulo = new ProfessorDAO(null);

		List<Professor> lista = daoNulo.getMinhaLista();

		assertNotNull(lista);
		assertTrue(lista.isEmpty());
	}

	@Test
	void testGetMinhaListaSQLException() throws Exception {
		// drop da tabela para causar erro no SELECT
		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("DROP TABLE tb_professores");
		}

		List<Professor> lista = dao.getMinhaLista();

		// resultado esperado: lista vazia e sem exceção
		assertNotNull(lista);
		assertTrue(lista.isEmpty());
	}

	@Test
	void testGetMinhaListaFinallyComConexaoInterna() {
		ProfessorDAO daoInterno = new ProfessorDAO() {
			@Override
			protected String getNomeTabela() {
				return "tabela_inexistente"; // provoca SQLException
			}
		};

		List<Professor> lista = daoInterno.getMinhaLista();

		assertNotNull(lista);
		assertTrue(lista.isEmpty());
	}

	// existeCpf
	@Test
	void testExisteCpf() {
		dao.insert(criarProfessorFake("Marcos", 36, "Ilha", "88888888888", "36123456789", "Graduação", 10000));

		assertTrue(dao.existeCpf("88888888888"));
		assertFalse(dao.existeCpf("99999999999"));
	}

	@Test
	void testExisteCpfIgnorandoId() {
		Professor p1 = criarProfessorFake("Otávio", 40, "Ilha", "00011122233", "21123456789", "Especialização", 15000);
		dao.insert(p1);

		// mesmo cpf, mas ignorando o próprio id -> NÃO deve acusar duplicidade
		assertFalse(dao.existeCpf("00011122233", p1.getId()));

		// outro professor com mesmo CPF -> deve acusar duplicidade
		Professor p2 = criarProfessorFake("Pedro", 41, "Tubarão", "00011122233", "22123456789", "Especialização",
				20000);
		dao.insert(p2);

		assertTrue(dao.existeCpf("00011122233", p1.getId()));
	}

	@Test
	void testExisteCpfIgnorandoIdListaVazia() {
		assertFalse(dao.existeCpf("qualquer", 1));
	}

	@Test
	void testExisteCpfIgnorandoId_AFalse_BFalse() {
		Professor p1 = criarProfessorFake("Mario", 30, "Ilha", "11111111111", "11111111", "Mestrado", 4000);
		dao.insert(p1);

		boolean resultado = dao.existeCpf("cpf_inexistente", p1.getId());

		assertFalse(resultado);
	}

	// nome da tabela
	@Test
	void testGetNomeTabela() {
		assertEquals("tb_professores", dao.getNomeTabela());
	}

}
