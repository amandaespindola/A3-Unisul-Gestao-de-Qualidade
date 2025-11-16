package model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

import dao.AlunoDAO;

class AlunoModelTest {

	// helper para injetar dao mockado via Reflection
	private void injectDao(Aluno aluno, AlunoDAO mock) {
		try {
			Field f = Aluno.class.getDeclaredField("dao");
			f.setAccessible(true);
			f.set(aluno, mock);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testGettersAndSetters() {
		Aluno aluno = new Aluno();
		aluno.setCurso("Design");
		aluno.setFase(3);
		aluno.setId(1);
		aluno.setNome("João");
		aluno.setIdade(20);

		Assertions.assertEquals("Design", aluno.getCurso());
		Assertions.assertEquals(3, aluno.getFase());
		Assertions.assertEquals(1, aluno.getId());
		Assertions.assertEquals("João", aluno.getNome());
		Assertions.assertEquals(20, aluno.getIdade());
	}

	@Test
	void testToStringContainsData() {
		Aluno aluno = new Aluno("Design de Moda", 3, 1, "João", 20);
		String result = aluno.toString();
		Assertions.assertTrue(result.contains("Design de Moda"));
		Assertions.assertTrue(result.contains("João"));
		Assertions.assertTrue(result.contains("Fase:3"));
	}

	// testes de construtores
	@Test
	void testConstrutorComCursoEFase() {
		Aluno aluno = new Aluno("Relações Internacionais", 7);

		Assertions.assertEquals("Relações Internacionais", aluno.getCurso());
		Assertions.assertEquals(7, aluno.getFase());
	}

	@Test
	void testConstrutorCompleto() {
		Aluno aluno = new Aluno("Arquitetura e Urbanismo", 5, 10, "Maria", 30);

		Assertions.assertEquals("Arquitetura e Urbanismo", aluno.getCurso());
		Assertions.assertEquals(5, aluno.getFase());
		Assertions.assertEquals(10, aluno.getId());
		Assertions.assertEquals("Maria", aluno.getNome());
		Assertions.assertEquals(30, aluno.getIdade());
	}

	// testes com dao
	@Test
	void testGetMinhaListaChamaDAO() {
		// Mock DAO
		AlunoDAO daoMock = mock(AlunoDAO.class);

		List<Aluno> listaFake = Arrays.asList(new Aluno("Sistemas de Informação", 3), new Aluno("Ciências da Computação", 5));

		when(daoMock.getMinhaLista()).thenReturn(listaFake);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		List<Aluno> resultado = aluno.getMinhaLista();

		Assertions.assertEquals(2, resultado.size());
		verify(daoMock, times(1)).getMinhaLista();
	}

	@Test
	void testInserirAlunoBdChamaDAO() {
		AlunoDAO daoMock = mock(AlunoDAO.class);
		when(daoMock.insert(any())).thenReturn(true);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		boolean r = aluno.inserirAlunoBd("Ciências Contabeis", 3, "João", 20);

		Assertions.assertTrue(r);
		verify(daoMock, times(1)).insert(any(Aluno.class));
	}

	@Test
	void testDeletarAlunoBDChamaDAO() {
		AlunoDAO daoMock = mock(AlunoDAO.class);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		boolean r = aluno.deletarAlunoBD(15);

		Assertions.assertTrue(r);
		verify(daoMock, times(1)).delete(15);
	}

	@Test
	void testAtualizarAlunoBDChamaDAO() {
		AlunoDAO daoMock = mock(AlunoDAO.class);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		boolean r = aluno.atualizarAlunoBD("Análise e Desenvolvimento de Sistemas", 4, 2, "Maria", 21);

		Assertions.assertTrue(r);
		verify(daoMock, times(1)).update(any(Aluno.class));
	}

	@Test
	void testCarregaAlunoChamaFindById() {
		AlunoDAO daoMock = mock(AlunoDAO.class);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		Aluno retorno = aluno.carregaAluno(50);

		Assertions.assertNull(retorno); // método original retorna null
		verify(daoMock, times(1)).findById(50);
	}

	@Test
	void testObterMaiorIdChamaDAO() {
		AlunoDAO daoMock = mock(AlunoDAO.class);
		when(daoMock.obterMaiorId()).thenReturn(88);

		Aluno aluno = new Aluno();
		injectDao(aluno, daoMock);

		int maiorId = aluno.obterMaiorId();

		Assertions.assertEquals(88, maiorId);
		verify(daoMock, times(1)).obterMaiorId();
	}

}