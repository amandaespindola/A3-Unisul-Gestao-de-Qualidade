package DAO;

import Model.Aluno;
import View.TelaLogin;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlunoDAO {

    private static final Logger LOGGER = Logger.getLogger(AlunoDAO.class.getName());
    private static final ArrayList<Aluno> MinhaLista = new ArrayList<>();

    private Connection conexao;          // conexão opcional externa (para testes)
    private boolean conexaoExterna = false;

    // Construtor padrão (usa MySQL)
    public AlunoDAO() {
    }

    // Construtor para testes (usa conexão injetada)
    public AlunoDAO(Connection conexao) {
        this.conexao = conexao;
        this.conexaoExterna = true;
    }

    // Retorna a conexão conforme o contexto
    public Connection getConexao() {
        if (conexaoExterna && conexao != null) {
            return conexao; // conexão externa (SQLite nos testes)
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";
            String user = TelaLogin.userDB;
            String password = TelaLogin.passwordDB;

            Connection connection = DriverManager.getConnection(url, user, password);
            LOGGER.log(Level.INFO, "Conexão com o banco de dados estabelecida com sucesso (MySQL).");
            return connection;

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver JDBC não encontrado.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
        }

        return null;
    }

    public int maiorID() {
        int maiorID = 0;
        String sql = "SELECT MAX(id) id FROM tb_alunos";

        Connection conn = getConexao();
        if (conn == null) {
            return 0;
        }

        try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            if (res.next()) {
                maiorID = res.getInt("id");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar maior ID de alunos", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return maiorID;
    }

    public ArrayList<Aluno> getMinhaLista() {
        MinhaLista.clear();
        String sql = "SELECT * FROM tb_alunos";

        Connection conn = getConexao();
        if (conn == null) {
            return MinhaLista;
        }

        try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                Aluno objeto = new Aluno(
                        res.getString("curso"),
                        res.getInt("fase"),
                        res.getInt("id"),
                        res.getString("nome"),
                        res.getInt("idade")
                );
                MinhaLista.add(objeto);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar lista de alunos", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return MinhaLista;
    }

    public boolean InsertAlunoBD(Aluno objeto) {
        String sql = "INSERT INTO tb_alunos(id, nome, idade, curso, fase) VALUES (?, ?, ?, ?, ?)";
        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, objeto.getId());
            stmt.setString(2, objeto.getNome());
            stmt.setInt(3, objeto.getIdade());
            stmt.setString(4, objeto.getCurso());
            stmt.setInt(5, objeto.getFase());
            stmt.executeUpdate();

            LOGGER.log(Level.INFO, () -> "Aluno inserido com sucesso: ID " + objeto.getId());
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao inserir aluno com id %d", objeto.getId()), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public boolean DeleteAlunoBD(int id) {
        String sql = "DELETE FROM tb_alunos WHERE id = ?";
        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            LOGGER.log(Level.INFO, () -> "Aluno deletado: ID " + id);
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao deletar aluno com id %d", id), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public boolean UpdateAlunoBD(Aluno objeto) {
        String sql = "UPDATE tb_alunos SET nome = ?, idade = ?, curso = ?, fase = ? WHERE id = ?";
        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCurso());
            stmt.setInt(4, objeto.getFase());
            stmt.setInt(5, objeto.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, () -> "Aluno atualizado com sucesso: ID " + objeto.getId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, () -> "Nenhum aluno encontrado para atualizar: ID " + objeto.getId());
                return false;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao atualizar aluno com id %d", objeto.getId()), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public Aluno carregaAluno(int id) {
        Aluno objeto = new Aluno();
        objeto.setId(id);

        String sql = "SELECT * FROM tb_alunos WHERE id = ?";
        Connection conn = getConexao();
        if (conn == null) {
            return objeto;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    objeto.setNome(res.getString("nome"));
                    objeto.setIdade(res.getInt("idade"));
                    objeto.setCurso(res.getString("curso"));
                    objeto.setFase(res.getInt("fase"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao carregar aluno com id %d", id), ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return objeto;
    }

    // Fecha a conexão apenas se o DAO for interno (MySQL)
    private void fecharConexaoSeInterna(Connection conn) {
        if (!conexaoExterna && conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar conexão interna", e);
            }
        }
    }
}
