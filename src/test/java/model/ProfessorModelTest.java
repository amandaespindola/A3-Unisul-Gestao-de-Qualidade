package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProfessorModelTest {

	@Test
	void testGettersAndSetters() {
		Professor professor = new Professor();
		professor.setCampus("Campus A");
		professor.setCpf("12345678900");
		professor.setContato("99999-9999");
		professor.setTitulo("Doutor");
		professor.setSalario(5000);
		professor.setId(1);
		professor.setNome("Maria");
		professor.setIdade(40);

		Assertions.assertEquals("Campus A", professor.getCampus());
		Assertions.assertEquals("12345678900", professor.getCpf());
		Assertions.assertEquals("99999-9999", professor.getContato());
		Assertions.assertEquals("Doutor", professor.getTitulo());
		Assertions.assertEquals(5000, professor.getSalario());
		Assertions.assertEquals("Maria", professor.getNome());
	}

	@Test
	void testToStringContainsData() {
		model.ProfessorDTO dto = new model.ProfessorDTO();
		dto.setCampus("Campus A");
		dto.setCpf("12345678900");
		dto.setContato("99999-9999");
		dto.setTitulo("Doutor");
		dto.setSalario((double)5000);
		dto.setId(1);
		dto.setNome("Maria");
		dto.setIdade(40);

		Professor professor = new Professor(dto);
		String result = professor.toString();
		Assertions.assertTrue(result.contains("Campus A"));
		Assertions.assertTrue(result.contains("Maria"));
		Assertions.assertTrue(result.contains("Salário:5000"));
	}
}