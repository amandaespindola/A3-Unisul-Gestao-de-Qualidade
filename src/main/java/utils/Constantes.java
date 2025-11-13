package utils;

import java.util.Arrays;
import java.util.List;

public final class Constantes {

    private Constantes() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }

    protected static final String[] CURSOS = {"-", "Administração", "Análise e Desenvolvimento de Sistemas",
        "Arquitetura e Urbanismo", "Ciências Contábeis", "Ciências da Computação", "Design", "Design de Moda",
        "Relações Internacionais", "Sistemas de Informação"};

    protected static final int[] FASES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    protected static final String[] CAMPUS = {"-", "Continente", "Dib Mussi", "Ilha", "Pedra Branca", "Trajano",
        "Tubarão"};

    protected static final String[] TITULOS = {"-", "Graduação", "Especialização", "Mestrado", "Doutorado"};

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

        // Botões
        public static final String BTN_CONFIRMAR = "Confirmar";
        public static final String BTN_CANCELAR = "Cancelar";
        public static final String BTN_CADASTRAR = "Cadastrar";
        public static final String BTN_EDITAR = "Editar";
        public static final String BTN_DELETAR = "Excluir";
        public static final String BTN_ATUALIZAR = "Atualizar";
        public static final String BTN_EXPORTAR = "Exportar";
        public static final String BTN_VOLTAR = "Voltar";
        public static final String BTN_SAIR = "Sair";

        // Labels
        public static final String NOME = "Nome:";
        public static final String NASCIMENTO = "Nasc:";
        public static final String IDADE = "Idade:";
        public static final String CPF = "CPF:";
        public static final String CURSO = "Curso:";
        public static final String FASE = "Fase:";
        public static final String CAMPUS = "Campus:";
        public static final String CONTATO = "Contato:";
        public static final String SALARIO = "Salário:";
        public static final String TITULO = "Título:";

        // Titulos
        public static final String TITULO_CAD_ALUNO = "Cadastro de Aluno";
        public static final String TITULO_EDITAR_ALUNO = "Editar Aluno";
        public static final String TITULO_GERENCIA_ALUNOS = "Cadastro de Alunos";

        public static final String TITULO_CAD_PROFESSOR = "Cadastro de Professor";
        public static final String TITULO_EDITAR_PROFESSOR = "Editar Professor";
        public static final String TITULO_GERENCIA_PROFESSORES = "Cadastro de Professores";

        // Textos de Menu
        public static final String MENU_PRINCIPAL = "Menu";
        public static final String GERENCIAR_PROFESSORES = "Gerenciar Professores";
        public static final String GERENCIAR_ALUNOS = "Gerenciar Alunos";
        public static final String REFRESH = "Atualizar";
        public static final String EXPORTAR = "Exportar";
        public static final String SOBRE = "Sobre";
        public static final String LEAVE = "Sair";

    }
}
