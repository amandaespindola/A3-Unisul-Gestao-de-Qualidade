package regression;

import dao.AlunoDAO;
import model.Aluno;
import org.junit.jupiter.api.*;
import utils.ConexaoManager;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlunoDAORegressionTest {

    private Connection conn;
    private AlunoDAO dao;

    @BeforeEach
    void setup() throws Exception {
        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close();
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabela(conn);

        dao = new AlunoDAO(conn);
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

    @Test
    @DisplayName("REGRESSÃO: Insert deve persistir corretamente e retornar ID válido")
    void testInsertPersisteId() {
        Aluno a = new Aluno("Administração", 4, 0, "Amanda", 22);
        assertTrue(dao.insert(a));
        assertTrue(a.getId() > 0, "ID não gerado — regressão detectada");
    }

    @Test
    @DisplayName("REGRESSÃO: Update deve realmente alterar atributos")
    void testUpdateRealmenteAtualiza() {
        Aluno a = new Aluno("Administração", 4, 0, "Amanda", 22);
        dao.insert(a);

        a.setNome("Amanda Teste");
        dao.update(a);

        Aluno novo = dao.findById(a.getId());
        assertEquals("Amanda Teste", novo.getNome());
    }

    @Test
    @DisplayName("REGRESSÃO: Delete deve remover o registro")
    void testDeleteRegressao() {
        Aluno a = new Aluno("Administração", 4, 0, "Amanda", 22);
        dao.insert(a);

        assertTrue(dao.delete(a.getId()));
        assertNull(dao.findById(a.getId()), "Registro deveria ter sido removido");
    }

    @Test
    @DisplayName("REGRESSÃO: getMinhaLista deve retornar registros persistidos")
    void testListaRegressao() {
        dao.insert(new Aluno("Arquitetura e Urbanismo", 2, 0, "Laura", 20));
        dao.insert(new Aluno("Ciências Contábeis", 5, 0, "Gabi", 21));

        List<Aluno> lista = dao.getMinhaLista();

        assertEquals(2, lista.size(), "Lista não reflete registros reais — possível regressão");
    }

    @Test
    @DisplayName("REGRESSÃO: obterMaiorId deve acompanhar autoincrement")
    void testObterMaiorIdRegressao() {
        dao.insert(new Aluno("Arquitetura e Urbanismo", 2, 0, "Laura", 20));
        dao.insert(new Aluno("Ciências Contábeis", 5, 0, "Gabi", 21));

        assertEquals(2, dao.obterMaiorId());
    }
}
