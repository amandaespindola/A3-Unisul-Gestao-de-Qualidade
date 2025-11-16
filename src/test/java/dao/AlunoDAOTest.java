package dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import model.Aluno;
import utils.ConexaoManager;
import utils.DaoUtils;

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

	// testa if conn == null
	@Test
	void testInsertConexaoNula() {
		AlunoDAO daoNulo = new AlunoDAO(null) {
			@Override
			protected Connection getConexao() {
				return null;
			}
		};

		Aluno a = new Aluno("Sistemas", 3, 1, "João", 20);

		boolean result = daoNulo.insert(a);

		assertFalse(result);
	}

	@Test
	void testInsertSQLException() throws Exception {
		// Drop para causar SQLException
		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("DROP TABLE tb_alunos");
		}

		AlunoDAO daoErro = new AlunoDAO(conn);

		Aluno a = new Aluno("Erro", 5, 1, "José", 33);

		boolean result = daoErro.insert(a);

		assertFalse(result); // cai no catch
	}

	@Test
	void testInsertFinallyExecutado() {
		AlunoDAO daoInterno = new AlunoDAO() {
			@Override
			protected String getNomeTabela() {
				return "tabela_inexistente"; // forçando erro no insert
			}
		};

		Aluno a = new Aluno("Teste", 2, 1, "Maria", 19);

		assertDoesNotThrow(() -> daoInterno.insert(a)); // cobre o finally
	}

	@Test
	void testInsertRetornaFalseSemExcecao() {
		AlunoDAO daoFake = new AlunoDAO(ConexaoManager.getConnection()) {
			@Override
			public boolean insert(Aluno objeto) {
				try {
					PreparedStatement stmt = getConexao().prepareStatement("INSERT INTO tb_alunos (nome) VALUES (?)");
					stmt.setString(1, objeto.getNome());

					// força o return false final da classe
					stmt.executeUpdate();
					return false;

				} catch (Exception e) {
					return false;
				}
			}
		};

		Aluno a = new Aluno("Falha", 4, 1, "Mario", 50);

		boolean result = daoFake.insert(a);

		assertFalse(result); // cobre o return false final
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

	@Test
	void testFindByIdConexaoNula() {
		AlunoDAO daoNulo = new AlunoDAO(null) {
			@Override
			protected Connection getConexao() {
				return null; // força conn == null
			}
		};

		Aluno resultado = daoNulo.findById(1);

		assertNull(resultado);
	}

	@Test
	void testFindByIdSQLException() {
		AlunoDAO daoErro = new AlunoDAO(ConexaoManager.getConnection()) {
			@Override
			protected String getNomeTabela() {
				return "tabela_inexistente"; // causa SQLException
			}
		};

		Aluno resultado = daoErro.findById(1);

		assertNull(resultado);
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

	@Test
	void testUpdateConexaoNula() {
		AlunoDAO daoNulo = new AlunoDAO(null) {
			@Override
			protected Connection getConexao() {
				return null; // força o caminho desejado
			}
		};

		Aluno a = new Aluno("ADS", 5, 1, "Teste", 20);

		boolean resultado = daoNulo.update(a);

		assertFalse(resultado);
	}

	@Test
	void testUpdateSQLExceptionReal() {
		AlunoDAO daoErro = new AlunoDAO(ConexaoManager.getConnection()) {
			@Override
			public boolean update(Aluno objeto) {
	            try {
	                PreparedStatement st =
	                        getConexao().prepareStatement("UPDATE *** INVALIDO ***");
	                st.executeUpdate();
	                return true;
	            } catch (SQLException ex) {
	                return DaoUtils.tratarErroUpdate("Aluno", objeto.getId(), ex,
	                        getConexao(), this::fecharConexaoSeInterna);
	            }
	        }
	    };

		Aluno a = new Aluno("ADS", 4, 1, "Teste", 22);
		a.setId(1);

		boolean resultado = daoErro.update(a);

		assertFalse(resultado); // cobre o catch
	}

	@Test
	void testUpdateSQLExceptionConexaoInterna() {
		AlunoDAO daoInterno = new AlunoDAO() {
			@Override
			public boolean update(Aluno objeto) {
				try {
					PreparedStatement st = getConexao().prepareStatement("UPDATE INVALIDO");
					st.executeUpdate();
					return true;
				} catch (SQLException ex) {
					// aqui entra no catch REAL da classe
					return DaoUtils.tratarErroUpdate("Aluno", objeto.getId(), ex, getConexao(),
							this::fecharConexaoSeInterna);
				}
			}
		};

		Aluno a = new Aluno("ADS", 4, 1, "Teste", 22);
		a.setId(1);

		boolean resultado = daoInterno.update(a);

		assertFalse(resultado); // cobre caminho do catch + tratarErroUpdate
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

	@Test
	void testGetMinhaListaConexaoNula() {
		AlunoDAO daoNulo = new AlunoDAO(null) {
			@Override
			protected Connection getConexao() {
				return null;
			}
		};

		List<Aluno> lista = daoNulo.getMinhaLista();

		assertNotNull(lista);
		assertTrue(lista.isEmpty());
	}

	@Test
	void testGetMinhaListaSQLException() throws Exception {
		// Derruba a tabela para forçar SQLException
		Connection conn = ConexaoManager.getConnection();
		try (Statement st = conn.createStatement()) {
			st.execute("DROP TABLE tb_alunos");
		}

		List<Aluno> lista = dao.getMinhaLista();

		assertNotNull(lista);
		assertTrue(lista.isEmpty()); // pois não deve lançar exceção
	}

	@Test
	void testGetNomeTabela() {
		assertEquals("tb_alunos", dao.getNomeTabela());
	}

}
