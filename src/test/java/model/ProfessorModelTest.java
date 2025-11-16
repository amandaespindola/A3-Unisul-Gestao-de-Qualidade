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

import dao.ProfessorDAO;

class ProfessorModelTest {

	private void injectDao(Professor professor, ProfessorDAO mock) {
		try {
			Field f = Professor.class.getDeclaredField("dao");
			f.setAccessible(true);
			f.set(professor, mock);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testGettersAndSetters() {
		Professor professor = new Professor();
		professor.setCampus("Ilha");
		professor.setCpf("12345678900");
		professor.setContato("999999999");
		professor.setTitulo("Doutorado");
		professor.setSalario(5000);
		professor.setId(1);
		professor.setNome("Maria");
		professor.setIdade(40);

		Assertions.assertEquals("Ilha", professor.getCampus());
		Assertions.assertEquals("12345678900", professor.getCpf());
		Assertions.assertEquals("999999999", professor.getContato());
		Assertions.assertEquals("Doutorado", professor.getTitulo());
		Assertions.assertEquals(5000, professor.getSalario());
		Assertions.assertEquals("Maria", professor.getNome());
	}

	@Test
	void testToStringContainsData() {
		model.ProfessorDTO dto = new model.ProfessorDTO();
		dto.setCampus("Dib Mussi");
		dto.setCpf("12345678900");
		dto.setContato("99999-9999");
		dto.setTitulo("Doutorado");
		dto.setSalario((double) 5000);
		dto.setId(1);
		dto.setNome("Maria");
		dto.setIdade(40);

		Professor professor = new Professor(dto);
		String result = professor.toString();
		Assertions.assertTrue(result.contains("Dib Mussi"));
		Assertions.assertTrue(result.contains("Maria"));
		Assertions.assertTrue(result.contains("Salário:5000"));
	}

	@Test
	void testGetMinhaListaChamaDAO() {
		ProfessorDAO daoMock = mock(ProfessorDAO.class);

		List<Professor> listaFake = Arrays.asList(new Professor(), new Professor());

		when(daoMock.getMinhaLista()).thenReturn(listaFake);

		Professor professor = new Professor();
		injectDao(professor, daoMock);

		Assertions.assertEquals(2, professor.getMinhaLista().size());
		verify(daoMock).getMinhaLista();
	}

	@Test
	void testInserirProfessorBD() {
		ProfessorDAO daoMock = mock(ProfessorDAO.class);
		when(daoMock.insert(any())).thenReturn(true);

		ProfessorDTO dto = new ProfessorDTO();
		dto.setNome("João");
		dto.setCampus("Campus A");
		dto.setCpf("11111111111");
		dto.setContato("99999");
		dto.setTitulo("Mestre");
		dto.setSalario(2000);
		dto.setIdade(30);

		Professor professor = new Professor();
		injectDao(professor, daoMock);

		boolean r = professor.inserirProfessorBD(dto);

		Assertions.assertTrue(r);
		verify(daoMock).insert(any(Professor.class));
	}

	@Test
	void testDeletarProfessorBdChamaDAO() {
		ProfessorDAO daoMock = mock(ProfessorDAO.class);

		Professor professor = new Professor();
		injectDao(professor, daoMock);

		boolean r = professor.deletarProfessorBD(10);

		Assertions.assertTrue(r);
		verify(daoMock).delete(10);
	}


	@Test
	void testCarregaProfessorChamaFindById() {
		ProfessorDAO daoMock = mock(ProfessorDAO.class);

		Professor professor = new Professor();
		injectDao(professor, daoMock);

		Professor retorno = professor.carregaProfessor(77);

		Assertions.assertNull(retorno);
		verify(daoMock).findById(77);
	}

	@Test
	void testObterMaiorIdChamaDAO() {
		ProfessorDAO daoMock = mock(ProfessorDAO.class);
		when(daoMock.obterMaiorId()).thenReturn(99);

		Professor professor = new Professor();
		injectDao(professor, daoMock);

		int maior = professor.obterMaiorId();

		Assertions.assertEquals(99, maior);
		verify(daoMock).obterMaiorId();
	}
}