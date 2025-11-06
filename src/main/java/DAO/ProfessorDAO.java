package DAO;

import Model.Professor;
import View.TelaLogin;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessorDAO {

    private static final Logger LOGGER = Logger.getLogger(ProfessorDAO.class.getName());
    public static final ArrayList<Professor> MinhaLista2 = new ArrayList<>();

    private Connection conexao;           // conexão opcional externa (testes)
    private boolean conexaoExterna = false;

    // Construtor padrão (usa MySQL)
    public ProfessorDAO() {
    }

    // Construtor para testes (usa conexão injetada)
    public ProfessorDAO(Connection conexao) {
        this.conexao = conexao;
        this.conexaoExterna = true;
    }

    // Retorna a conexão conforme o contexto
    public Connection getConexao() {
        if (conexaoExterna && conexao != null) {
            return conexao; // conexão injetada (SQLite)
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/db_alunos?useTimezone=true&serverTimezone=UTC";
            String user = TelaLogin.userDB;
            String password = TelaLogin.passwordDB;

            Connection connection = DriverManager.getConnection(url, user, password);
            LOGGER.log(Level.INFO, "Conexão com o banco de dados (MySQL) estabelecida com sucesso.");
            return connection;

        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver JDBC não encontrado.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
        }

        return null;
    }

    // Fecha apenas conexões internas
    private void fecharConexaoSeInterna(Connection conn) {
        if (!conexaoExterna && conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erro ao fechar conexão interna", e);
            }
        }
    }

    public int maiorID() {
        int maiorID = 0;
        String sql = "SELECT MAX(id) id FROM tb_professores";

        Connection conn = getConexao();
        if (conn == null) {
            return 0;
        }

        try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            if (res.next()) {
                maiorID = res.getInt("id");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar maior ID de professores", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return maiorID;
    }

    public ArrayList<Professor> getMinhaLista() {
        MinhaLista2.clear();
        String sql = "SELECT * FROM tb_professores";

        Connection conn = getConexao();
        if (conn == null) {
            return MinhaLista2;
        }

        try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                Professor objeto = new Professor(
                        res.getString("campus"),
                        res.getString("cpf"),
                        res.getString("contato"),
                        res.getString("titulo"),
                        res.getInt("salario"),
                        res.getInt("id"),
                        res.getString("nome"),
                        res.getInt("idade")
                );
                MinhaLista2.add(objeto);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar lista de professores", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return MinhaLista2;
    }

    public boolean InsertProfessorBD(Professor objeto) {
        String sql = "INSERT INTO tb_professores(id, nome, idade, campus, cpf, contato, titulo, salario) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, objeto.getId());
            stmt.setString(2, objeto.getNome());
            stmt.setInt(3, objeto.getIdade());
            stmt.setString(4, objeto.getCampus());
            stmt.setString(5, objeto.getCpf());
            stmt.setString(6, objeto.getContato());
            stmt.setString(7, objeto.getTitulo());
            stmt.setInt(8, objeto.getSalario());

            stmt.executeUpdate();
            LOGGER.log(Level.INFO, () -> "Professor inserido com sucesso: ID " + objeto.getId());
            return true;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao inserir professor com id %d", objeto.getId()), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public boolean DeleteProfessorBD(int id) {
        String sql = "DELETE FROM tb_professores WHERE id = ?";

        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            LOGGER.log(Level.INFO, () -> "Professor deletado: ID " + id);
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao deletar professor com id %d", id), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public boolean UpdateProfessorBD(Professor objeto) {
        String sql = "UPDATE tb_professores SET nome = ?, idade = ?, campus = ?, cpf = ?, contato = ?, titulo = ?, salario = ? WHERE id = ?";

        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCampus());
            stmt.setString(4, objeto.getCpf());
            stmt.setString(5, objeto.getContato());
            stmt.setString(6, objeto.getTitulo());
            stmt.setInt(7, objeto.getSalario());
            stmt.setInt(8, objeto.getId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LOGGER.log(Level.INFO, () -> "Professor atualizado com sucesso: ID " + objeto.getId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, () -> "Nenhum professor encontrado para atualizar: ID " + objeto.getId());
                return false;
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao atualizar professor com id %d", objeto.getId()), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    public Professor carregaProfessor(int id) {
        Professor objeto = new Professor();
        objeto.setId(id);
        String sql = "SELECT * FROM tb_professores WHERE id = ?";

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
                    objeto.setCampus(res.getString("campus"));
                    objeto.setCpf(res.getString("cpf"));
                    objeto.setContato(res.getString("contato"));
                    objeto.setTitulo(res.getString("titulo"));
                    objeto.setSalario(res.getInt("salario"));
                }
            }

            LOGGER.log(Level.INFO, () -> "Professor carregado: ID " + id);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, String.format("Erro ao carregar professor com id %d", id), ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return objeto;
    }
}
