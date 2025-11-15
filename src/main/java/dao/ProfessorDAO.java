package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import model.Professor;
import model.ProfessorDTO;
import utils.DaoUtils;

public class ProfessorDAO extends BaseDAO<Professor> {

    private static final ArrayList<Professor> minhaLista = new ArrayList<>();
    private static final String ENTIDADE = "Professor";

    private ProfessorDTO mapResultSetToDTO(ResultSet res) throws SQLException {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setCampus(res.getString("campus"));
        dto.setCpf(res.getString("cpf"));
        dto.setContato(res.getString("contato"));
        dto.setTitulo(res.getString("titulo"));
        dto.setSalario(res.getDouble("salario"));
        dto.setId(res.getInt("id"));
        dto.setNome(res.getString("nome"));
        dto.setIdade(res.getInt("idade"));
        return dto;
    }

    public ProfessorDAO() {
    }

    public ProfessorDAO(Connection conexao) {
        super(conexao);
    }

    @Override
    public boolean insert(Professor objeto) {
        String sql = "INSERT INTO tb_professores "
                + "(nome, idade, campus, cpf, contato, titulo, salario) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCampus());
            stmt.setString(4, objeto.getCpf());
            stmt.setString(5, objeto.getContato());
            stmt.setString(6, objeto.getTitulo());
            stmt.setDouble(7, objeto.getSalario());

            return DaoUtils.tratarInsercao(stmt, objeto, ENTIDADE, objeto::setId);

        } catch (SQLException ex) {
            DaoUtils.logErro("inserir", ENTIDADE, objeto.getNome(), ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return false;
    }

    @Override
    public boolean update(Professor objeto) {
        String sql = "UPDATE tb_professores SET "
                + "nome=?, idade=?, campus=?, cpf=?, contato=?, titulo=?, salario=? WHERE id=?";

        Connection conn = getConexao();
        if (conn == null) {
            return false;
        }

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, objeto.getNome());
            stmt.setInt(2, objeto.getIdade());
            stmt.setString(3, objeto.getCampus());
            stmt.setString(4, objeto.getCpf());
            stmt.setString(5, objeto.getContato());
            stmt.setString(6, objeto.getTitulo());
            stmt.setDouble(7, objeto.getSalario());
            stmt.setInt(8, objeto.getId());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                logger.info(() -> "Professor atualizado: ID " + objeto.getId());
                return true;
            }

            logger.warning(() -> "Nenhuma linha atualizada no update de professor ID: " + objeto.getId());
            return false;

        } catch (SQLException ex) {
            return DaoUtils.tratarErroUpdate(ENTIDADE, objeto.getId(), ex, conn, this::fecharConexaoSeInterna);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM tb_professores WHERE id=?";
        Connection conn = getConexao();
        return DaoUtils.executarDelete(conn, sql, id, ENTIDADE, this::fecharConexaoSeInterna);
    }

    @Override
    public Professor findById(int id) {
        String sql = "SELECT * FROM tb_professores WHERE id=?";
        Connection conn = getConexao();
        if (conn == null) {
            return null;
        }

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try ( ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return new Professor(mapResultSetToDTO(res));
                }
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex, () -> "Erro ao buscar professor " + id);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return null;
    }

    @Override
    protected String getNomeTabela() {
        return "tb_professores";
    }

    public List<Professor> getMinhaLista() {
        minhaLista.clear();
        String sql = "SELECT * FROM tb_professores";
        Connection conn = getConexao();
        if (conn == null) {
            return minhaLista;
        }

        try ( Statement stmt = conn.createStatement();  ResultSet res = stmt.executeQuery(sql)) {

            while (res.next()) {
                minhaLista.add(new Professor(mapResultSetToDTO(res)));
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao buscar lista de professores", ex);
        } finally {
            fecharConexaoSeInterna(conn);
        }

        return minhaLista;
    }

    public boolean existeCpf(String cpf) {
        return getMinhaLista().stream().anyMatch(p -> p.getCpf().equals(cpf));
    }

    public boolean existeCpf(String cpf, int idIgnorado) {
        return getMinhaLista().stream()
                .anyMatch(p -> p.getCpf().equals(cpf) && p.getId() != idIgnorado);
    }
}
