package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import model.Aluno;
import utils.DaoUtils;

/**
 * DAO responsável por executar operações de persistência da entidade
 * {@link Aluno}. Implementa os métodos básicos de CRUD, utilizando a
 * infraestrutura fornecida por {@link BaseDAO}.
 */
public class AlunoDAO extends BaseDAO<Aluno> {

        /** 
        * Lista utilizada como cache temporário dos alunos carregados do banco. 
        * Renomada a cada chamada de {@link #getMinhaLista()}.
        */
	private static final ArrayList<Aluno> MinhaLista = new ArrayList<>();
        
        /** Nome da entidade usado para compor mensagens de log e erros. */
	private static final String ENTIDADE = "Aluno";

        /**
        * Construtor padrão. Utiliza a conexão interna definida em {@link BaseDAO}.
        */
	public AlunoDAO() {
	}

        /**
        * Construtor que permite receber uma conexão externa.
        *
        * @param conexao conexão já aberta que será utilizada pelas operações do DAO
        */
	public AlunoDAO(Connection conexao) {
		super(conexao);
	}

        /**
        * Insere um novo aluno no banco.
        *
        * @param objeto aluno a ser inserido
        * @return {@code true} se a inserção ocorreu com sucesso
        */
	@Override
	public boolean insert(Aluno objeto) {
		String sql = "INSERT INTO tb_alunos (nome, idade, curso, fase) VALUES (?, ?, ?, ?)";
		Connection conn = getConexao();
		if (conn == null) {
			logger.warning("Conexão nula ao tentar inserir aluno.");
			return false;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, objeto.getNome());
			stmt.setInt(2, objeto.getIdade());
			stmt.setString(3, objeto.getCurso());
			stmt.setInt(4, objeto.getFase());

			return DaoUtils.tratarInsercao(stmt, objeto, ENTIDADE, objeto::setId);
		} catch (SQLException ex) {
			DaoUtils.logErro("inserir", ENTIDADE, objeto.getNome(), ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return false;
	}

        /**
        * Atualiza os dados de um aluno existente.
        *
        * @param objeto aluno com os novos valores
        * @return {@code true} se a atualização foi bem-sucedida
        */
	@Override
	public boolean update(Aluno objeto) {
		String sql = "UPDATE tb_alunos SET nome=?, idade=?, curso=?, fase=? WHERE id=?";
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
			stmt.executeUpdate();
			logger.info(() -> "Aluno atualizado: ID " + objeto.getId());
			return true;
		} catch (SQLException ex) {
			return DaoUtils.tratarErroUpdate(ENTIDADE, objeto.getId(), ex, conn, this::fecharConexaoSeInterna);
		}
	}

        /**
        * Exclui um aluno pelo seu ID.
        *
        * @param id identificador do aluno
        * @return {@code true} se a exclusão foi realizada
        */
	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM tb_alunos WHERE id=?";
		Connection conn = getConexao();
		return DaoUtils.executarDelete(conn, sql, id, ENTIDADE, this::fecharConexaoSeInterna);
	}

        /**
        * Busca um aluno pelo ID.
        *
        * @param id identificador do aluno
        * @return o aluno encontrado, ou {@code null} se não existir
        */
	@Override
	public Aluno findById(int id) {
		String sql = "SELECT id, nome, idade, curso, fase FROM tb_alunos WHERE id=?";
		Connection conn = getConexao();
		if (conn == null) {
			return null;
		}

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet res = stmt.executeQuery()) {
				if (res.next()) {
					return new Aluno(res.getString("curso"), res.getInt("fase"), res.getInt("id"),
							res.getString("nome"), res.getInt("idade"));
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, ex, () -> "Erro ao carregar aluno " + id);

		} finally {
			fecharConexaoSeInterna(conn);
		}
		return null;
	}

        /**
        * Retorna uma lista contendo todos os alunos presentes no banco.
        * A lista interna é limpa e recarregada a cada chamada.
        *
        * @return lista dos alunos cadastrados
        */
	public List<Aluno> getMinhaLista() {
		MinhaLista.clear();
		String sql = "SELECT id, nome, idade, curso, fase FROM tb_alunos";
		Connection conn = getConexao();
		if (conn == null) {
			return MinhaLista;
		}

		try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
			while (res.next()) {
				MinhaLista.add(new Aluno(res.getString("curso"), res.getInt("fase"), res.getInt("id"),
						res.getString("nome"), res.getInt("idade")));
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Erro ao buscar lista de alunos", ex);
		} finally {
			fecharConexaoSeInterna(conn);
		}
		return MinhaLista;
	}

        /**
        * Retorna o nome da tabela correspondente à entidade.
        *
        * @return nome da tabela de alunos
            */
	@Override
	protected String getNomeTabela() {
		return "tb_alunos";
	}

}
