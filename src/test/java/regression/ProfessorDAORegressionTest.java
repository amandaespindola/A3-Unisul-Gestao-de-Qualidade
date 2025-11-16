package regression;

import dao.ProfessorDAO;
import model.Professor;
import model.ProfessorDTO;
import utils.ConexaoManager;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorDAORegressionTest {

    private Connection conn;
    private ProfessorDAO dao;

    @BeforeEach
    void setup() throws Exception {
        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close();
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabela();

        dao = new ProfessorDAO(conn);
    }

    private void criarTabela() throws SQLException {
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


    private Professor novoProfessor() {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setNome("Amanda");
        dto.setIdade(30);
        dto.setCampus("UFSC");
        dto.setCpf("12345678900");
        dto.setContato("48999999999");
        dto.setTitulo("Mestre");
        dto.setSalario(3500.55);
        dto.setId(0);
        return new Professor(dto);
    }

    @Test
    @DisplayName("REGRESSÃO: Insert deve persistir e gerar ID válido")
    void testInsertRegressao() {
        Professor p = novoProfessor();
        assertTrue(dao.insert(p));
        assertTrue(p.getId() > 0);
    }

    @Test
    @DisplayName("REGRESSÃO: Update altera valores corretamente")
    void testUpdateRegressao() {
        Professor p = novoProfessor();
        dao.insert(p);

        p.setNome("Alterado");
        p.setSalario(9999.99);

        assertTrue(dao.update(p));

        Professor novo = dao.findById(p.getId());
        assertEquals("Alterado", novo.getNome());
        assertEquals(9999.99, novo.getSalario());
    }

    @Test
    @DisplayName("REGRESSÃO: Delete remove registro")
    void testDeleteRegressao() {
        Professor p = novoProfessor();
        dao.insert(p);

        assertTrue(dao.delete(p.getId()));
        assertNull(dao.findById(p.getId()));
    }

    @Test
    @DisplayName("REGRESSÃO: getMinhaLista reflete registros atuais")
    void testListaRegressao() {
        dao.insert(novoProfessor());
        dao.insert(novoProfessor());

        List<Professor> lista = dao.getMinhaLista();
        assertEquals(2, lista.size());
    }
}
