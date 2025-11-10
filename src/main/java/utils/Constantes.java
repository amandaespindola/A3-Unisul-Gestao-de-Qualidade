package utils;

public final class Constantes {

    private Constantes() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }

    public static final String[] CURSOS = {
        "-",
        "Administração",
        "Análise e Desenvolvimento de Sistemas",
        "Arquitetura e Urbanismo",
        "Ciências Contábeis",
        "Ciências da Computação",
        "Design",
        "Design de Moda",
        "Relações Internacionais",
        "Sistemas de Informação"
    };

    public static final int[] FASES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public static final String[] CAMPUS = {
        "-",
        "Continente",
        "Dib Mussi",
        "Ilha",
        "Pedra Branca",
        "Trajano",
        "Tubarão"
    };

    public static final String[] TITULOS = {
        "-",
        "Graduação",
        "Especialização",
        "Mestrado",
        "Doutorado"
    };

    public class UIConstants {
    	public static final String DEFAULT_FONT = "Segoe UI";
    }
}
