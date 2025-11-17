package security;

import dao.AlunoDAO;
import model.Aluno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import utils.ConexaoManager;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AlunoDAO_SQLInjectionTest {

    private Connection conn;

    // criação do DAO vulnerável
    static class AlunoDAOVulneravel extends AlunoDAO {

        public AlunoDAOVulneravel(Connection c) {
            super(c);
        }

        @Override
        public boolean insert(Aluno a) {
            String sql = "INSERT INTO tb_alunos (nome, idade, curso, fase) VALUES ('"
                    + a.getNome() + "', " + a.getIdade() + ", '" + a.getCurso() + "', " + a.getFase() + ")";

            try (Statement st = getConexao().createStatement()) {
                return st.executeUpdate(sql) > 0;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    // configuração do banco em memória
    @BeforeEach
    void setup() throws Exception {
        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close();
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabela();
    }

    private void criarTabela() throws SQLException {
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

    // helper para criar objeto manipulável
    private Aluno criarAluno(String nome) {
        return new Aluno("Curso X", 1, 0, nome, 20);
    }

    @Test
    @DisplayName("DAO vulnerável deve permitir SQL Injection em campo nome")
    void testInjectionFuncionaNoVulneravel() throws Exception {

        AlunoDAOVulneravel daoV = new AlunoDAOVulneravel(conn);

        // Payload compatível com SQLite (não quebra string)
        Aluno atacante = criarAluno("Fulano' OR '1'='1");

        boolean ok = daoV.insert(atacante);
        assertTrue(ok, "DAO vulnerável deveria aceitar payload malicioso");

        // Agora verificamos o resultado na tabela
        try (PreparedStatement ps = conn.prepareStatement("SELECT nome FROM tb_alunos")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());

            // SQLite converte o booleano em 1 (TRUE)
            assertEquals("1", rs.getString("nome"),
                    "SQLite deve registrar 1 quando expressão booleana é usada em campo textual");
        }
    }

    @Test
    @DisplayName("AlunoDAO oficial deve bloquear SQL Injection")
    void testDAOSeguroBloqueiaInjection() {

        AlunoDAO daoSeguro = new AlunoDAO(conn);

        Aluno atacante = criarAluno("Fulano' OR '1'='1");

        boolean ok = daoSeguro.insert(atacante);

        assertTrue(ok, "DAO deve inserir normalmente quando usa PreparedStatement");

        // E o dado salvo deve ser EXATAMENTE o nome original — sem mudar para “1”
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT nome FROM tb_alunos WHERE nome LIKE 'Fulano%'")) {

            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            assertEquals("Fulano' OR '1'='1", rs.getString("nome"));
        } catch (Exception e) {
            fail("DAO seguro deve retornar exatamente o nome sem interpretar código SQL");
        }
    }

    @Test
    @DisplayName("Payload ' OR '1'='1 deve ser inserido literalmente no DAO seguro")
    void testLiteral_OR1igual1() {

        AlunoDAO dao = new AlunoDAO(conn);

        Aluno atacante = criarAluno("X' OR '1'='1");

        assertTrue(dao.insert(atacante));

        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS qtd FROM tb_alunos")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("qtd"),
                    "DAO seguro NÃO deve multiplicar linhas mesmo com payload malicioso");
        } catch (Exception e) {
            fail("Erro inesperado ao validar segurança");
        }
    }


    @Test
    @DisplayName("DAO seguro não deve lançar SQLException com payload malicioso")
    void testDAOSeguroNaoExplode() {

        AlunoDAO dao = new AlunoDAO(conn);

        Aluno atacante = criarAluno("aaaa'); DROP TABLE tb_alunos; --");

        assertDoesNotThrow(() -> dao.insert(atacante),
                "DAO seguro deve resistir a payloads extremos sem lançar exceções");
    }
}
