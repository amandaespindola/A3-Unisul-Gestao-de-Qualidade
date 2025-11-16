package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConstantesTest {

    // testa getters das listas
    @Test
    @DisplayName("getCursos deve retornar lista imutável com os valores esperados")
    void testGetCursosImutabilidadeEConteudo() {
        List<String> cursos = Constantes.getCursos();

        assertNotNull(cursos);
        // confere valores-chave
        assertEquals("-", cursos.get(0));
        assertTrue(cursos.contains("Administração"));
        assertTrue(cursos.contains("Sistemas de Informação"));

        // garante que a lista é imutável
        assertThrows(UnsupportedOperationException.class, () -> cursos.add("Novo Curso"));
    }

    @Test
    @DisplayName("getCampus deve retornar lista imutável com os valores esperados")
    void testGetCampusImutabilidadeEConteudo() {
        List<String> campus = Constantes.getCampus();

        assertNotNull(campus);
        assertEquals("-", campus.get(0));
        assertTrue(campus.contains("Continente"));
        assertTrue(campus.contains("Tubarão"));

        assertThrows(UnsupportedOperationException.class, () -> campus.add("Novo Campus"));
    }

    @Test
    @DisplayName("getTitulos deve retornar lista imutável com os valores esperados")
    void testGetTitulosImutabilidadeEConteudo() {
        List<String> titulos = Constantes.getTitulos();

        assertNotNull(titulos);
        assertEquals("-", titulos.get(0));
        assertTrue(titulos.contains("Graduação"));
        assertTrue(titulos.contains("Doutorado"));

        assertThrows(UnsupportedOperationException.class, () -> titulos.add("Novo Título"));
    }

    @Test
    @DisplayName("getFases deve retornar lista imutável de 1 a 10")
    void testGetFasesImutabilidadeEConteudo() {
        List<Integer> fases = Constantes.getFases();

        assertNotNull(fases);
        assertEquals(10, fases.size());
        assertEquals(1, fases.get(0));
        assertEquals(10, fases.get(9));

        assertThrows(UnsupportedOperationException.class, () -> fases.add(11));
    }

    @Test
    @DisplayName("Construtor de Constantes deve ser privado e lançar UnsupportedOperationException")
    void testConstrutorPrivadoConstantes() throws Exception {
        Constructor<Constantes> ctor = Constantes.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> ctor.newInstance()
        );

        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
        assertEquals("Classe de constantes não pode ser instanciada", ex.getCause().getMessage());
    }

    @Test
    @DisplayName("Construtor de UIConstants deve ser privado e lançar UnsupportedOperationException")
    void testConstrutorPrivadoUIConstants() throws Exception {
        Constructor<Constantes.UIConstants> ctor
                = Constantes.UIConstants.class.getDeclaredConstructor(Constantes.class);

        ctor.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> ctor.newInstance((Constantes) null)
        );

        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
        assertEquals("Classe de constantes UI não pode ser instanciada", ex.getCause().getMessage());
    }

    @Test
    @DisplayName("Constantes principais de UI devem manter valores esperados")
    void testUIConstantsValoresBasicos() {
        // fonte padrão
        assertEquals("Segoe UI", Constantes.UIConstants.DEFAULT_FONT);

        // alguns textos de botões
        assertEquals("Confirmar", Constantes.UIConstants.BTN_CONFIRMAR);
        assertEquals("Cancelar", Constantes.UIConstants.BTN_CANCELAR);
        assertEquals("Cadastrar", Constantes.UIConstants.BTN_CADASTRAR);
        assertEquals("Editar", Constantes.UIConstants.BTN_EDITAR);

        // alguns títulos
        assertEquals("Cadastro de Aluno", Constantes.UIConstants.TITULO_CAD_ALUNO);
        assertEquals("Cadastro de Professor", Constantes.UIConstants.TITULO_CAD_PROFESSOR);

        // texto Sobre (sanidade)
        assertEquals("UNIVERSIDADE DO SUL DE SANTA CATARINA", Constantes.UIConstants.SOBRE_INSTITUICAO);
    }
}
