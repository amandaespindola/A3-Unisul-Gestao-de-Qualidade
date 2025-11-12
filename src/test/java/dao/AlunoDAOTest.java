package dao;

import model.Aluno;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlunoDAOTest {

    private Connection connection;
    private AlunoDAO alunoDAO;

    @BeforeAll
    void setupDatabase() throws Exception {
        // Carrega o driver SQLite explicitamente
        Class.forName("org.sqlite.JDBC");

        // Cria uma conexão em memória compartilhada (persistente até o fim dos testes)
        connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared");

        // Cria a tabela se não existir
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_alunos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome TEXT, "
                    + "idade INTEGER, "
                    + "curso TEXT, "
                    + "fase INTEGER)");

        }

        // Cria o DAO usando a mesma conexão
        alunoDAO = new AlunoDAO(connection);
    }

    @BeforeEach
    void resetTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM tb_alunos");
        }
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testInsertAlunoBD() {
        Aluno aluno = new Aluno("Engenharia", 3, 1, "João", 20);
        Assertions.assertTrue(alunoDAO.insert(aluno));
    }

    @Test
    void testGetMinhaLista() {
        alunoDAO.insert(new Aluno("Engenharia", 3, 1, "João", 20));
        alunoDAO.insert(new Aluno("Medicina", 2, 2, "Maria", 22));

        List<Aluno> lista = alunoDAO.getMinhaLista();
        Assertions.assertEquals(2, lista.size());
    }
}
