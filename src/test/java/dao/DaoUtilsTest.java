package dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import utils.DaoUtils;

class DaoUtilsTest {

	// ======================
	// tratarInsercao()
	// ======================

	@Test
	void testTratarInsercaoSucessoComId() throws Exception {
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
		ResultSet rs = Mockito.mock(ResultSet.class);

		Mockito.when(stmt.executeUpdate()).thenReturn(1);
		Mockito.when(stmt.getGeneratedKeys()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true);
		Mockito.when(rs.getInt(1)).thenReturn(50);

		AtomicInteger idSet = new AtomicInteger();

		boolean resultado = DaoUtils.tratarInsercao(stmt, new Object(), "Aluno", idSet::set);

		assertTrue(resultado);
		assertEquals(50, idSet.get());
	}

	@Test
	void testTratarInsercaoSucessoSemId() throws Exception {
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
		ResultSet rs = Mockito.mock(ResultSet.class);

		Mockito.when(stmt.executeUpdate()).thenReturn(1);
		Mockito.when(stmt.getGeneratedKeys()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(false);

		AtomicInteger idSet = new AtomicInteger();

		boolean resultado = DaoUtils.tratarInsercao(stmt, new Object(), "Aluno", idSet::set);

		assertTrue(resultado);
		assertEquals(0, idSet.get());
	}

	@Test
	void testTratarInsercaoSemLinhasAfetadas() throws Exception {
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);

		Mockito.when(stmt.executeUpdate()).thenReturn(0);

		boolean resultado = DaoUtils.tratarInsercao(stmt, new Object(), "Aluno", id -> {
		});

		assertFalse(resultado);
	}

	@Test
	void testTratarInsercaoSQLException() throws Exception {
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);

		Mockito.when(stmt.executeUpdate()).thenThrow(new SQLException("erro"));

		assertThrows(SQLException.class, () -> DaoUtils.tratarInsercao(stmt, new Object(), "Aluno", id -> {
		}));
	}

	// ======================
	// executarDelete()
	// ======================

	@Test
	void testExecutarDeleteConexaoNula() {
		boolean resultado = DaoUtils.executarDelete(null, "DELETE ...", 10, "Aluno", c -> {
		});
		assertFalse(resultado);
	}

	@Test
	void testExecutarDeleteSucesso() throws Exception {
		Connection conn = Mockito.mock(Connection.class);
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);

		Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);

		AtomicBoolean fechado = new AtomicBoolean(false);

		boolean resultado = DaoUtils.executarDelete(conn, "DELETE FROM tb_alunos WHERE id=?", 5, "Aluno",
				c -> fechado.set(true));

		assertTrue(resultado);
		assertTrue(fechado.get());
		Mockito.verify(stmt).executeUpdate();
	}

	@Test
	void testExecutarDeleteSQLException() throws Exception {
		Connection conn = Mockito.mock(Connection.class);
		PreparedStatement stmt = Mockito.mock(PreparedStatement.class);

		Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(stmt);
		Mockito.when(stmt.executeUpdate()).thenThrow(new SQLException("erro"));

		AtomicBoolean fechado = new AtomicBoolean(false);

		boolean resultado = DaoUtils.executarDelete(conn, "DELETE ...", 1, "Aluno", c -> fechado.set(true));

		assertFalse(resultado);
		assertTrue(fechado.get());
	}

	// ======================
	// tratarErroUpdate()
	// ======================

	@Test
	void testTratarErroUpdate() {
		AtomicBoolean fechado = new AtomicBoolean(false);

		boolean resultado = DaoUtils.tratarErroUpdate("Aluno", 10, new SQLException("erro"), null,
				c -> fechado.set(true));

		assertFalse(resultado);
		assertTrue(fechado.get());
	}

	// ======================
	// logErro()
	// ======================

	@Test
	void testLogErroNaoLanca() {
		assertDoesNotThrow(() -> DaoUtils.logErro("inserir", "Aluno", 10, new SQLException("erro")));
	}
}
