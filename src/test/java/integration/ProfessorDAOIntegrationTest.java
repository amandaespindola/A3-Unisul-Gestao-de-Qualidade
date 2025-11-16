package integration;

import dao.ProfessorDAO;
import model.Professor;
import model.ProfessorDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import utils.ConexaoManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorDAOIntegrationTest {

    private Connection conn;
    private ProfessorDAO dao;

    static class ProfessorDAOTestable extends ProfessorDAO {

        public ProfessorDAOTestable(Connection c) {
            super(c);
        }

        @Override
        protected String getNomeTabela() {
            return "tb_professores"; // garante uso da tabela correta
        }
    }

    @BeforeEach
    void setup() throws Exception {

        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close();
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabelaProfessores(conn);

        dao = new ProfessorDAOTestable(conn);
    }

    private void criarTabelaProfessores(Connection conn) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            stm.execute("""
                CREATE TABLE tb_professores (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT,
                    idade INTEGER,
                    campus TEXT,
                    cpf TEXT,
                    contato TEXT,
                    titulo TEXT,
                    salario REAL
                )
            """);
        }
    }

    @Test
    void testInsert() {
        Professor novo = criarProfessor("Laura");

        boolean ok = dao.insert(novo);

        assertTrue(ok);
        assertTrue(novo.getId() > 0);
    }

    @Test
    void testFindById() {
        Professor novo = criarProfessor("João");
        dao.insert(novo);

        Professor encontrado = dao.findById(novo.getId());

        assertNotNull(encontrado);
        assertEquals("João", encontrado.getNome());
    }

    @Test
    void testUpdate() {
        Professor novo = criarProfessor("Carlos");
        dao.insert(novo);

        novo.setNome("Carlos Silva");
        boolean ok = dao.update(novo);

        assertTrue(ok);

        Professor atualizado = dao.findById(novo.getId());
        assertEquals("Carlos Silva", atualizado.getNome());
    }

    @Test
    void testDelete() {
        Professor novo = criarProfessor("Marina");
        dao.insert(novo);

        boolean ok = dao.delete(novo.getId());

        assertTrue(ok);
        assertNull(dao.findById(novo.getId()));
    }

    @Test
    void testGetMinhaLista() {
        dao.insert(criarProfessor("Ana"));
        dao.insert(criarProfessor("Pedro"));

        List<Professor> lista = dao.getMinhaLista();

        assertEquals(2, lista.size());
    }

    @Test
    void testObterMaiorId() {
        dao.insert(criarProfessor("Laura")); // id 1
        dao.insert(criarProfessor("Gabi"));  // id 2

        int maiorId = dao.obterMaiorId();

        assertEquals(2, maiorId);
    }

    @Test
    void testFluxoCompleto() {
        Professor p = criarProfessor("Julia");

        assertTrue(dao.insert(p));

        p.setNome("Julia Souza");
        assertTrue(dao.update(p));

        assertEquals("Julia Souza", dao.findById(p.getId()).getNome());

        assertTrue(dao.delete(p.getId()));
        assertNull(dao.findById(p.getId()));
    }

    // helpers
    private Professor criarProfessor(String nome) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setNome(nome);
        dto.setCampus("Campus Norte");
        dto.setCpf("12345678901");
        dto.setContato("48999887766");
        dto.setTitulo("Mestre");
        dto.setSalario(5000.00);
        dto.setIdade(30);
        dto.setId(0);

        return new Professor(dto);
    }
}
