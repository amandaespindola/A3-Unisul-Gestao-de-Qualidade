package utils;

import java.util.Arrays;
import java.util.List;


/**
 * Classe utilitária responsável por armazenar constantes utilizadas em 
 * diferentes partes da aplicação. Inclui listas de cursos, fases, campus 
 * e títulos acadêmicos. 
 *
 * <p>A classe é declarada como <strong>final</strong> para impedir herança,
 * e possui construtor privado para evitar instanciamento.</p>
 *
 * <p>Todos os dados são imutáveis e expostos através de métodos 
 * estáticos que retornam coleções não modificáveis.</p>
 */
public final class Constantes {

	/**
     * Construtor privado para impedir instanciamento da classe.
     * Sempre lança uma {@link UnsupportedOperationException}.
     */
    private Constantes() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }

    /** Lista fixa de cursos disponíveis. */
    protected static final String[] CURSOS = {"-", "Administração", "Análise e Desenvolvimento de Sistemas",
        "Arquitetura e Urbanismo", "Ciências Contábeis", "Ciências da Computação", "Design", "Design de Moda",
        "Relações Internacionais", "Sistemas de Informação"};

    /** Lista fixa de fases disponíveis. */
    protected static final int[] FASES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    /** Lista de campus disponíveis. */
    protected static final String[] CAMPUS = {"-", "Continente", "Dib Mussi", "Ilha", "Pedra Branca", "Trajano",
        "Tubarão"};

    /** Lista de títulos acadêmicos. */
    protected static final String[] TITULOS = {"-", "Graduação", "Especialização", "Mestrado", "Doutorado"};

    /**
     * Retorna uma lista imutável contendo todos os cursos.
     *
     * @return lista não modificável de cursos
     */
    public static List<String> getCursos() {
        return List.copyOf(Arrays.asList(CURSOS));
    }

    /**
     * Retorna uma lista imutável contendo todos os campus.
     *
     * @return lista não modificável de campus
     */
    public static List<String> getCampus() {
        return List.copyOf(Arrays.asList(CAMPUS));
    }

    /**
     * Retorna uma lista imutável contendo todos os títulos acadêmicos.
     *
     * @return lista não modificável de títulos
     */
    public static List<String> getTitulos() {
        return List.copyOf(Arrays.asList(TITULOS));
    }

    /**
     * Retorna uma lista imutável contendo todas as fases possíveis.
     *
     * @return lista não modificável de fases
     */
    public static List<Integer> getFases() {
        return List.copyOf(Arrays.stream(FASES).boxed().toList());
    }

    /**
     * Classe interna dedicada a armazenar constantes relacionadas à interface
     * gráfica (UI), como textos de botões, rótulos, títulos e informações da
     * seção "Sobre".
     *
     * <p>Da mesma forma que {@link Constantes}, esta classe também não pode ser
     * instanciada.</p>
     */
    public static class UIConstants {

    	/**
         * Construtor privado para impedir instanciamento.
         */
        private UIConstants() {
            throw new UnsupportedOperationException("Classe de constantes UI não pode ser instanciada");
        }

        /** Fonte padrão utilizada na interface. */
        public static final String DEFAULT_FONT = "Segoe UI";

        // -------------------
        // Botões
        // -------------------
        
        public static final String BTN_CONFIRMAR = "Confirmar";
        public static final String BTN_CANCELAR = "Cancelar";
        public static final String BTN_CADASTRAR = "Cadastrar";
        public static final String BTN_EDITAR = "Editar";
        public static final String BTN_DELETAR = "Excluir";
        public static final String BTN_ATUALIZAR = "Atualizar";
        public static final String BTN_EXPORTAR = "Exportar";
        public static final String BTN_VOLTAR = "Voltar";
        public static final String BTN_SAIR = "Sair";
        public static final String BTN_LOGIN = "LOGIN";
        public static final String BTN_ALUNOS = "Alunos";
        public static final String BTN_PROFESSORES = "Professores";

        // -------------------
        // Labels
        // -------------------
        
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
        public static final String LABEL_USUARIO = "DIGITE O USUÁRIO (MySQL)";
        public static final String LABEL_SENHA = "DIGITE A SENHA (MySQL)";

        // -------------------
        // Títulos de janelas
        // -------------------
        
        public static final String TITULO_SISTEMA = "SisUni - Sistema de Gerenciamento Universitário";

        public static final String TITULO_CAD_ALUNO = "Cadastro de Aluno";
        public static final String TITULO_EDITAR_ALUNO = "Editar Aluno";
        public static final String TITULO_GERENCIA_ALUNOS = "Cadastro de Alunos";

        public static final String TITULO_CAD_PROFESSOR = "Cadastro de Professor";
        public static final String TITULO_EDITAR_PROFESSOR = "Editar Professor";
        public static final String TITULO_GERENCIA_PROFESSORES = "Cadastro de Professores";

        // -------------------
        // Menus
        // -------------------

        public static final String MENU_PRINCIPAL = "Menu";
        public static final String GERENCIAR_PROFESSORES = "Gerenciar Professores";
        public static final String GERENCIAR_ALUNOS = "Gerenciar Alunos";
        public static final String REFRESH = "Atualizar";
        public static final String EXPORTAR = "Exportar";
        public static final String SOBRE = "Sobre";
        public static final String LEAVE = "Sair";

        // -------------------
        // Sessão "Sobre"
        // -------------------
        
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
