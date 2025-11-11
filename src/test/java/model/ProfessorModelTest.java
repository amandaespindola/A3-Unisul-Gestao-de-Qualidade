package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import model.Professor;

public class ProfessorModelTest {

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
		Professor professor = new Professor("Campus A", "12345678900", "99999-9999", "Doutor", 5000, 1, "Maria", 40);
		String result = professor.toString();
		Assertions.assertTrue(result.contains("Campus A"));
		Assertions.assertTrue(result.contains("Maria"));
		Assertions.assertTrue(result.contains("Salário:5000"));
	}
}