package integration;

import dao.AlunoDAO;
import model.Aluno;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import utils.ConexaoManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AlunoDAOIntegrationTest {

    private Connection conn;
    private AlunoDAO dao;

    // Subclasse para permitir testar usando SQLite e a tabela correta
    static class AlunoDAOTestable extends AlunoDAO {

        public AlunoDAOTestable(Connection c) {
            super(c);
        }

        @Override
        protected String getNomeTabela() {
            return "tb_alunos";
        }
    }

    @BeforeEach
    void setup() throws Exception {

        // Configura SQLite in-memory
        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close(); // garante reset
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabela(conn);

        dao = new AlunoDAOTestable(conn); // usa DAO com tabela correta
    }

    private void criarTabela(Connection conn) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            stm.execute("""
            CREATE TABLE tb_alunos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                idade INTEGER,
                curso TEXT,
                fase INTEGER
            )
        """);
        }
    }

    // TESTES DE INTEGRAÇÃO
    @Test
    void testInsert() {
        Aluno aluno = new Aluno("Administração", 4, 1, "Amanda", 22);

        boolean ok = dao.insert(aluno);

        assertTrue(ok, "Insert deveria retornar true");
        assertTrue(aluno.getId() > 0, "ID deveria ter sido gerado pelo AUTOINCREMENT");
    }

    @Test
    void testFindById() {
        Aluno aluno = new Aluno("Administração", 4, 1, "Amanda", 22);
        dao.insert(aluno);

        Aluno encontrado = dao.findById(aluno.getId());

        assertNotNull(encontrado);
        assertEquals("Amanda", encontrado.getNome());
    }

    @Test
    void testUpdate() {
        Aluno aluno = new Aluno("Administração", 4, 1, "Amanda", 22);
        dao.insert(aluno);

        aluno.setNome("Amanda Atualizada");
        boolean ok = dao.update(aluno);

        assertTrue(ok);

        Aluno novo = dao.findById(aluno.getId());
        assertEquals("Amanda Atualizada", novo.getNome());
    }

    @Test
    void testDelete() {
        Aluno aluno = new Aluno("Administração", 4, 1, "Amanda", 22);
        dao.insert(aluno);

        boolean ok = dao.delete(aluno.getId());

        assertTrue(ok);
        assertNull(dao.findById(aluno.getId()));
    }

    @Test
    void testGetMinhaLista() {
        dao.insert(new Aluno("Relações Internacionais", 3, 1, "Laura", 20));
        dao.insert(new Aluno("Arquitetura e Urbanismo", 4, 2, "Gabi", 21));

        List<Aluno> lista = dao.getMinhaLista();

        assertEquals(2, lista.size());
    }

    @Test
    void testObterMaiorId() {
        dao.insert(new Aluno("Relações Internacionais", 3, 1, "Laura", 20)); // id 1
        dao.insert(new Aluno("Arquitetura e Urbanismo", 4, 2, "Gabi", 21));  // id 2

        int maiorId = dao.obterMaiorId();

        assertEquals(2, maiorId, "Maior ID deveria ser 2");
    }

    @Test
    void testFluxoCompleto() {
        Aluno aluno = new Aluno("Administração", 4, 1, "Amanda", 22);

        // insert
        assertTrue(dao.insert(aluno));

        // update
        aluno.setNome("Novo Nome");
        assertTrue(dao.update(aluno));

        assertEquals("Novo Nome", dao.findById(aluno.getId()).getNome());

        // delete
        assertTrue(dao.delete(aluno.getId()));
        assertNull(dao.findById(aluno.getId()));
    }
}
