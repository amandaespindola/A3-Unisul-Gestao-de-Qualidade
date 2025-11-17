package security;

import dao.ProfessorDAO;
import model.ProfessorDTO;
import model.Professor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import utils.ConexaoManager;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfessorDAO_SQLInjectionTest {

    private Connection conn;

    static class ProfessorDAOVulneravel extends ProfessorDAO {

        public ProfessorDAOVulneravel(Connection c) {
            super(c);
        }

        @Override
        public boolean insert(Professor p) {
            try (Statement stm = getConexao().createStatement()) {

                // concatena a string diretamente
                String sql = "INSERT INTO tb_professores (nome, idade, campus, cpf, contato, titulo, salario) VALUES ('"
                        + p.getNome() + "', "
                        + p.getIdade() + ", '"
                        + p.getCampus() + "', '"
                        + p.getCpf() + "', '"
                        + p.getContato() + "', '"
                        + p.getTitulo() + "', "
                        + p.getSalario() + ")";

                stm.execute(sql);
                return true;

            } catch (Exception e) {
                return false;
            }
        }
    }

    private Professor criarProfessor(String nome) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setNome(nome);
        dto.setIdade(40);
        dto.setCampus("Campus A");
        dto.setCpf("111.111.111-11");
        dto.setContato("(00) 00000-0000");
        dto.setTitulo("Mestre");
        dto.setSalario(3000.00);
        return new Professor(dto);
    }

    private void criarTabela() throws Exception {
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

    @BeforeEach
    void setup() throws Exception {
        ConexaoManager.setJdbcUrl("jdbc:sqlite::memory:");
        ConexaoManager.setDriverClass("org.sqlite.JDBC");

        ConexaoManager.close();
        ConexaoManager.init("", "");

        conn = ConexaoManager.getConnection();
        criarTabela();
    }

    @Test
    @DisplayName("DAO vulnerável aceita OR 1=1 na string (SQLite-safe)")
    void testOR1Igual1Funciona() throws Exception {

        ProfessorDAOVulneravel daoV = new ProfessorDAOVulneravel(conn);

        Professor atacante = criarProfessor("Fulano' OR '1'='1");

        boolean ok = daoV.insert(atacante);

        assertTrue(ok, "DAO vulnerável deveria aceitar payload malicioso");

        try (PreparedStatement ps = conn.prepareStatement("SELECT nome FROM tb_professores")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());

            assertEquals("1", rs.getString("nome"));
        }
    }

    @Test
    @DisplayName("DAO seguro não sofre alteração de comportamento com OR 1=1")
    void testDAOProtegidoContraOR1Igual1() throws Exception {

        ProfessorDAO daoSeguro = new ProfessorDAO(conn);

        Professor atacante = criarProfessor("Fulano' OR 1=1 --");

        boolean ok = daoSeguro.insert(atacante);
        assertTrue(ok);

        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) AS total FROM tb_professores")) {
            ResultSet rs = ps.executeQuery();
            assertEquals(1, rs.getInt("total")); // nenhum efeito colateral
        }
    }

    @Test
    @DisplayName("DAO seguro impede quebra de integridade por caracteres maliciosos")
    void testCaracteresMaliciosos() throws Exception {

        ProfessorDAO daoSeguro = new ProfessorDAO(conn);

        Professor malicioso = criarProfessor("João'); SELECT * FROM usuarios; --");

        boolean ok = daoSeguro.insert(malicioso);

        assertTrue(ok, "DAO seguro deve inserir sem modificar a query");

        // verifica se armazenou literalmente
        try (PreparedStatement ps = conn.prepareStatement("SELECT nome FROM tb_professores")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next());
            assertEquals("João'); SELECT * FROM usuarios; --", rs.getString("nome"));
        }
    }
}
