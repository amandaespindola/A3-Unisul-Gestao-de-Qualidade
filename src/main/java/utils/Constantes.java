package utils;

import java.util.Arrays;
import java.util.List;

/**
 * Classe utilitária que centraliza constantes usadas na aplicação.
 * 
 * <p>Contém listas imutáveis de cursos, fases, campi e títulos acadêmicos,
 * fornecendo acesso somente por métodos públicos que retornam cópias seguras.
 *
 * <p>Esta classe não deve ser instanciada.
 */
public final class Constantes {

    private Constantes() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }

    /** Lista fixa de cursos disponíveis no sistema. */
    protected static final String[] CURSOS = {"-", "Administração", "Análise e Desenvolvimento de Sistemas",
        "Arquitetura e Urbanismo", "Ciências Contábeis", "Ciências da Computação", "Design", "Design de Moda",
        "Relações Internacionais", "Sistemas de Informação"};

    /** Lista fixa de fases acadêmicas permitidas (1 a 10). */
    protected static final int[] FASES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    /** Lista fixa de campi da instituição. */
    protected static final String[] CAMPUS = {"-", "Continente", "Dib Mussi", "Ilha", "Pedra Branca", "Trajano",
        "Tubarão"};

    /** Lista de títulos acadêmicos aceitos para professores. */
    protected static final String[] TITULOS = {"-", "Graduação", "Especialização", "Mestrado", "Doutorado"};

    /**
     * Retorna a lista imutável de cursos.
     *
     * @return lista de cursos disponíveis
     */
    public static List<String> getCursos() {
        return List.copyOf(Arrays.asList(CURSOS));
    }

    /**
     * Retorna a lista imutável de campi.
     *
     * @return lista de campi
     */
    public static List<String> getCampus() {
        return List.copyOf(Arrays.asList(CAMPUS));
    }

    /**
     * Retorna a lista imutável de títulos acadêmicos.
     *
     * @return lista de títulos
     */
    public static List<String> getTitulos() {
        return List.copyOf(Arrays.asList(TITULOS));
    }

    /**
     * Retorna a lista imutável de fases acadêmicas.
     *
     * @return lista de fases
     */
    public static List<Integer> getFases() {
        return List.copyOf(Arrays.stream(FASES).boxed().toList());
    }

    /**
     * Conjunto de constantes relacionadas à interface gráfica da aplicação.
     * Contém nomes de botões, labels e textos exibidos na UI.
     *
     * <p>Classe interna não instanciável.
     */
    public class UIConstants {

        private UIConstants() {
            throw new UnsupportedOperationException("Classe de constantes UI não pode ser instanciada");
        }

        /** Fonte padrão utilizada na interface. */
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

        // Textos Sobre
        public static final String SOBRE_INSTITUICAO = "UNIVERSIDADE DO SUL DE SANTA CATARINA";
        public static final String SOBRE_UC = "PROGRAMAÇÃO DE SOLUÇÕES COMPUTACIONAIS";
        public static final String SOBRE_CURSO = "SISTEMAS DE INFORMAÇÃO";
        public static final String SOBRE_INTEGRANTE_1 = "• Camille Zimmermann";
        public static final String SOBRE_INTEGRANTE_2 = "• Julia Schaden Exterkoetter";
        public static final String SOBRE_INTEGRANTE_3 = "• Laura Maria Firta Foes";
        public static final String SOBRE_INTEGRANTE_4 = "• Lucas Felipe Rogério";
        public static final String SOBRE_INTEGRANTE_5 = "• Pablo Nevado";
        public static final String SOBRE_INTEGRANTE_6 = "• Rade Gabriel Farah ";
        public static final String SOBRE_DATA = "Florianópolis, 2022";
    }
}
