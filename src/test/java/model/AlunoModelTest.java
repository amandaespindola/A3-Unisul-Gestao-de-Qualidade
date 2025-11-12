package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AlunoModelTest {

	@Test
	void testGettersAndSetters() {
		Aluno aluno = new Aluno();
		aluno.setCurso("Engenharia");
		aluno.setFase(3);
		aluno.setId(1);
		aluno.setNome("João");
		aluno.setIdade(20);

		Assertions.assertEquals("Engenharia", aluno.getCurso());
		Assertions.assertEquals(3, aluno.getFase());
		Assertions.assertEquals(1, aluno.getId());
		Assertions.assertEquals("João", aluno.getNome());
		Assertions.assertEquals(20, aluno.getIdade());
	}

	@Test
	void testToStringContainsData() {
		Aluno aluno = new Aluno("Engenharia", 3, 1, "João", 20);
		String result = aluno.toString();
		Assertions.assertTrue(result.contains("Engenharia"));
		Assertions.assertTrue(result.contains("João"));
		Assertions.assertTrue(result.contains("Fase:3"));
	}
}