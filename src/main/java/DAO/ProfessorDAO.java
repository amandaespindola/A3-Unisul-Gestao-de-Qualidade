package DAO;

import Model.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class ProfessorDAO extends BaseDAO<Professor> {

    private static final ArrayList<Professor> minhaLista = new ArrayList<>();

    public ProfessorDAO() {
    }

    public ProfessorDAO(Connection conexao) {
        super(conexao);
    }

    @Override
    public boolean insert(Professor objeto) {
        String sql = "INSERT INTO tb_professores (nome, idade, campus, cpf, contato, titulo, salario) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = getConexao();
        if (conn == null) {
            logger.warning("Conexão nula ao tentar inserir professor.");
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCampus());
            stmt.setString(4, objeto.getCpf());
            stmt.setString(5, objeto.getContato());
            stmt.setString(6, objeto.getTitulo());
            stmt.setDouble(7, objeto.getSalario());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int novoId = rs.getInt(1);
                        objeto.setId(novoId);
                        logger.info(() -> "Professor inserido: ID " + novoId);
                    }
                }
                return true;
            } else {
                logger.warning(() -> "Nenhuma linha inserida para o professor: " + objeto.getNome());
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao inserir professor: " + objeto.getNome(), ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return false;
    }

    @Override
    public boolean update(Professor objeto) {
        String sql = "UPDATE tb_professores SET nome=?, idade=?, campus=?, cpf=?, contato=?, titulo=?, salario=? WHERE id=?";
        Connection conn = getConexao();
        if (conn == null) {
            logger.warning("Conexão nula ao tentar atualizar professor.");
            return false;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCampus());
            stmt.setString(4, objeto.getCpf());
            stmt.setString(5, objeto.getContato());
            stmt.setString(6, objeto.getTitulo());
            stmt.setDouble(7, objeto.getSalario()); // ✓ CORRIGIDO: Double em vez de Int
            stmt.setInt(8, objeto.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                logger.info(() -> "Professor atualizado: ID " + objeto.getId());
                return true;
            } else {
                logger.warning(() -> "Nenhuma linha atualizada para professor ID: " + objeto.getId());
                return false;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao atualizar professor " + objeto.getId(), ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM tb_professores WHERE id=?";
        Connection conn = getConexao();
        if (conn == null) {
            logger.warning("Conexão nula ao tentar deletar professor.");
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.info(() -> "Professor deletado: ID " + id);
            return true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao deletar professor " + id, ex);
            return false;
        } finally {
            fecharConexaoSeInterna(conn);
        }
    }

    @Override
    public Professor findById(int id) {
        String sql = "SELECT * FROM tb_professores WHERE id=?";
        Connection conn = getConexao();
        if (conn == null) {
            logger.warning("Conexão nula ao tentar buscar professor por ID.");
            return null;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return new Professor(
                            res.getString("campus"),
                            res.getString("cpf"),
                            res.getString("contato"),
                            res.getString("titulo"),
                            res.getInt("salario"),
                            res.getInt("id"),
                            res.getString("nome"),
                            res.getInt("idade")
                    );
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao carregar professor " + id, ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return null;
    }

    public ArrayList<Professor> getMinhaLista() {
        minhaLista.clear();
        String sql = "SELECT * FROM tb_professores";
        Connection conn = getConexao();
        if (conn == null) {
            logger.warning("Conexão nula ao tentar carregar lista de professores.");
            return minhaLista;
        }

        try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                minhaLista.add(new Professor(
                        res.getString("campus"),
                        res.getString("cpf"),
                        res.getString("contato"),
                        res.getString("titulo"),
                        res.getInt("salario"),
                        res.getInt("id"),
                        res.getString("nome"),
                        res.getInt("idade")
                ));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao buscar lista de professores", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return minhaLista;
    }

    public int maiorId() {
        return super.maiorID("tb_professores");
    }
}
