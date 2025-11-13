package utils;

import java.util.Arrays;
import java.util.List;

public final class Constantes {

	private Constantes() {
		throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
	}

	protected static final String[] CURSOS = { "-", "Administração", "Análise e Desenvolvimento de Sistemas",
			"Arquitetura e Urbanismo", "Ciências Contábeis", "Ciências da Computação", "Design", "Design de Moda",
			"Relações Internacionais", "Sistemas de Informação" };

	protected static final int[] FASES = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	protected static final String[] CAMPUS = { "-", "Continente", "Dib Mussi", "Ilha", "Pedra Branca", "Trajano",
			"Tubarão" };

	protected static final String[] TITULOS = { "-", "Graduação", "Especialização", "Mestrado", "Doutorado" };

	public static List<String> getCursos() {
		return List.copyOf(Arrays.asList(CURSOS));
	}

	public static List<String> getCampus() {
		return List.copyOf(Arrays.asList(CAMPUS));
	}

	public static List<String> getTitulos() {
		return List.copyOf(Arrays.asList(TITULOS));
	}

	public static List<Integer> getFases() {
		return List.copyOf(Arrays.stream(FASES).boxed().toList());
	}

	public class UIConstants {
		private UIConstants() {
			throw new UnsupportedOperationException("Classe de constantes UI não pode ser instanciada");
		}

		public static final String DEFAULT_FONT = "Segoe UI";
	}
}
