package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import view.Mensagens;

import java.text.Normalizer;

import javax.swing.JFormattedTextField;

import java.util.logging.Level;

/**
 * Classe utilitária responsável por validações e formatações de entradas
 * utilizadas na aplicação, especialmente em telas Swing.
 * <p>
 * Contém métodos estáticos para validação de nomes, seleções de ComboBox,
 * documentos (como CPF e contato), idade, salário e campos numéricos em geral.
 * </p>
 *
 * A classe não pode ser instanciada.
 */
public final class ValidadorInput {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ValidadorInput.class.getName());

    /**
     * Construtor privado para impedir instância da classe. Esta é uma classe
     * utilitária.
     */
    private ValidadorInput() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Remove todos os caracteres não-numéricos de uma string. Útil para remover
     * máscaras de formatação de campos como CPF, telefone, CEP, etc.
     * <p>
     * Exemplos de uso:</p>
     * <ul>
     * <li>{@code removerMascara("123.456.789-00")} retorna
     * {@code "12345678900"}</li>
     * <li>{@code removerMascara("(11) 98765-4321")} retorna
     * {@code "11987654321"}</li>
     * </ul>
     *
     * @param input String contendo caracteres numéricos e não-numéricos
     * @return String contendo apenas os dígitos, ou string vazia se input for
     * null
     */
    public static String removerMascara(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString(); // <-- VOCÊ ESQUECEU DESTA LINHA!
    }

    /**
     * Calcula a idade com base na data de nascimento.
     *
     * @param dataNasc data de nascimento
     * @return idade em anos completos
     */
    public static int calculaIdade(Date dataNasc) {
        Calendar dataNascimento = new GregorianCalendar();
        dataNascimento.setTime(dataNasc);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);

        dataNascimento.add(Calendar.YEAR, age);

        if (today.before(dataNascimento)) {
            age--;
        }

        return age;
    }

    /**
     * Realiza validação de segurança básica em uma string de entrada. Verifica
     * se o input contém apenas caracteres seguros e não possui padrões
     * maliciosos como XSS, SQL Injection, Path Traversal, etc.
     *
     * @param input String a ser validada
     * @return true se a entrada for segura, false caso contrário
     * @see #temXSS(String)
     * @see #temSQLInjection(String)
     * @see #temPathTraversal(String)
     * @see #temUnicodeSuspeito(String)
     * @see #temShellInjection(String)
     */
    public static boolean validarSegurancaBasica(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        // Permitir letras, números, acentos e espaço
        if (input.matches("^[\\p{L}0-9 .'-]+$")) {
            return true; // seguro
        }

        String txt = Normalizer.normalize(input, Normalizer.Form.NFKC).toLowerCase();

        return !temXSS(txt)
                && !temSQLInjection(txt)
                && !temPathTraversal(txt)
                && !temUnicodeSuspeito(txt)
                && !temShellInjection(txt);
    }

    /**
     * Verifica se o texto contém padrões comuns de ataques XSS (Cross-Site
     * Scripting). Detecta tags HTML maliciosas e eventos JavaScript que podem
     * ser explorados.
     *
     * @param txt Texto normalizado em lowercase a ser analisado
     * @return true se padrões de XSS forem encontrados, false caso contrário
     */
    private static boolean temXSS(String txt) {
        String[] padroes = {
            "<script", "</script", "<img", "<iframe", "<object", "<embed",
            "<svg", "onerror", "onload", "<<", "<>", "</", "<script/>", "<!--"
        };
        return contemAlgum(txt, padroes);
    }

    /**
     * Verifica se o texto contém padrões comuns de SQL Injection. Detecta
     * comandos SQL maliciosos e técnicas de bypass de autenticação.
     *
     * @param txt Texto normalizado em lowercase a ser analisado
     * @return true se padrões de SQL Injection forem encontrados, false caso
     * contrário
     */
    private static boolean temSQLInjection(String txt) {
        String[] padroes = {
            "drop table", "select *", " or 1=1", "' or '1'='1",
            "\" or \"\"=\"", "xp_cmdshell", "--", ";--", "' or ",
            " exec ", " insert into "
        };
        return contemAlgum(txt, padroes);
    }

    /**
     * Verifica se o texto contém tentativas de Path Traversal. Detecta padrões
     * que tentam acessar diretórios fora do escopo permitido.
     *
     * @param txt Texto a ser analisado
     * @return true se padrões de Path Traversal forem encontrados, false caso
     * contrário
     */
    private static boolean temPathTraversal(String txt) {
        return txt.contains("../") || txt.contains("..\\");
    }

    /**
     * Verifica se o texto contém caracteres Unicode suspeitos. Detecta emojis,
     * símbolos especiais e caracteres de controle bidirecional que podem ser
     * usados para ofuscar conteúdo malicioso.
     *
     * @param txt Texto a ser analisado
     * @return true se caracteres Unicode suspeitos forem encontrados, false
     * caso contrário
     */
    private static boolean temUnicodeSuspeito(String txt) {
        return txt.codePoints().anyMatch(cp
                -> (cp >= 0x1F000)
                || // emojis
                (cp >= 0x2600 && cp <= 0x27BF)
                || // símbolos
                cp == 0x202E // bidi override
        );
    }

    /**
     * Verifica se o texto contém padrões comuns de Shell Injection. Detecta
     * caracteres especiais de shell que podem ser usados para executar comandos
     * maliciosos no sistema operacional.
     *
     * @param txt Texto normalizado em lowercase a ser analisado
     * @return true se padrões de Shell Injection forem encontrados, false caso
     * contrário
     * @see #contemAlgum(String, String[])
     */
    private static boolean temShellInjection(String txt) {
        String[] padroes = {"`", "$(", "|", "||", "&", ";"};
        return contemAlgum(txt, padroes);
    }

    /**
     * Verifica se o texto contém algum dos padrões especificados no array.
     * Método auxiliar usado pelas validações de segurança para detectar strings
     * maliciosas.
     *
     * @param txt Texto a ser analisado
     * @param padroes Array de strings contendo os padrões a serem procurados
     * @return true se qualquer padrão for encontrado no texto, false caso
     * contrário
     */
    private static boolean contemAlgum(String txt, String[] padroes) {
        for (String p : padroes) {
            if (txt.contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida o nome do usuário verificando tamanho mínimo e segurança. Realiza
     * validações de comprimento e caracteres permitidos, além de verificar
     * padrões de segurança contra ataques como XSS e SQL Injection.
     * <p>
     * Exemplos de uso:</p>
     * <ul>
     * <li>{@code validarNome("João Silva", 3)} retorna
     * {@code "João Silva"}</li>
     * <li>{@code validarNome("Jo", 3)} lança {@code Mensagens} com a mensagem
     * "Nome deve conter ao menos 3 caracteres."</li>
     * </ul>
     *
     * @param nome String contendo o nome a ser validado
     * @param minLength Comprimento mínimo exigido para o nome
     * @return O nome validado se todas as verificações passarem
     * @throws Mensagens Se o nome for nulo, vazio, menor que o tamanho mínimo
     * ou contiver caracteres inválidos/maliciosos
     * @see #validarSegurancaBasica(String)
     */
    public static String validarNome(String nome, int minLength) throws Mensagens {

        if (nome == null || nome.isBlank()) {
            throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
        }

        if (!validarSegurancaBasica(nome)) {
            throw new Mensagens("Entrada contém caracteres inválidos.");
        }

        if (nome.length() < minLength) {
            throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
        }

        return nome;
    }

    /**
     * Valida se a seleção feita em um ComboBox é válida.
     * <p>
     * O índice 0 é considerado valor padrão não selecionado.</p>
     *
     * @param selectedIndex índice selecionado no ComboBox
     * @param opcoes lista de opções disponíveis
     * @param campoMensagem nome exibido no erro
     * @return a opção selecionada
     * @throws Mensagens caso nenhuma opção válida seja selecionada
     */
    public static String validarSelecaoComboBox(int selectedIndex, List<String> opcoes, String campoMensagem)
            throws Mensagens {
        if (selectedIndex <= 0 || selectedIndex >= opcoes.size()) { // O índice 0 é sempre o valor padrão ("-" ou
            // similar)
            throw new Mensagens("Escolha o valor para o campo: " + campoMensagem);
        }
        return opcoes.get(selectedIndex);
    }

    /**
     * Valida campos numéricos que devem possuir tamanho exato, como CPF ou
     * número de contato.
     *
     * @param inputFormatado texto contendo o número possivelmente mascarado
     * @param tamanhoObrigatorio total de dígitos exigidos
     * @param campoMensagem nome do campo exibido em caso de erro
     * @return o valor formatado original
     * @throws Mensagens caso não possua o tamanho correto
     */
    public static String validarTamanhoNumericoFixo(String inputFormatado, int tamanhoObrigatorio, String campoMensagem)
            throws Mensagens {
        String inputLimpo = removerMascara(inputFormatado);
        if (inputLimpo.length() != tamanhoObrigatorio) {
            throw new Mensagens(
                    "O campo " + campoMensagem + " deve possuir " + tamanhoObrigatorio + " caracteres numéricos");
        }
        return inputFormatado; // Retorna o valor formatado (com máscara)
    }

    /**
     * Valida idade mínima com base em uma data de nascimento.
     *
     * @param dataNasc data de nascimento
     * @param idadeMinima idade mínima exigida
     * @return idade calculada
     * @throws Mensagens caso a data seja inválida ou a idade insuficiente
     */
    public static int validarIdadePorData(Date dataNasc, int idadeMinima) throws Mensagens {
        if (dataNasc == null) {
            throw new Mensagens("Data de Nascimento não pode ser vazia.");
        }
        int idade = calculaIdade(dataNasc);
        if (idade < idadeMinima) {
            throw new Mensagens("Data de Nascimento inválida ou professor menor de " + idadeMinima + " anos.");
        }
        return idade;
    }

    /**
     * Valida o salário informado em um campo formatado.
     * <p>
     * Aceita valores numéricos com máscara e tenta interpretar o texto
     * manualmente caso o {@link JFormattedTextField} não contenha valor.
     * </p>
     *
     * @param campo campo de texto formatado contendo o salário
     * @param tamanhoMinimo quantidade mínima de dígitos (sem considerar
     * decimais)
     * @return o valor do salário convertido para double
     * @throws Mensagens caso o campo esteja vazio, seja inválido ou tenha valor
     * curto demais
     */
    public static double validarSalario(JFormattedTextField campo, int tamanhoMinimo) throws Mensagens {
        Number valor = (Number) campo.getValue();
        String mensagemErroSalarioInvalido = "Informe um salário válido";

        if (valor == null) {
            // tenta parsear texto manualmente (fallback)
            try {
                String texto = campo.getText().replaceAll("[^0-9,]", "").replace(",", ".");
                if (texto.isEmpty()) {
                    throw new Mensagens(mensagemErroSalarioInvalido);
                }
                valor = Double.parseDouble(texto);
            } catch (Exception e) {
                throw new Mensagens(mensagemErroSalarioInvalido);
            }
        }

        if (valor.doubleValue() <= 0) {
            throw new Mensagens("Informe um salário válido");
        }

        String salarioLimpo = String.valueOf((long) valor.doubleValue());
        if (salarioLimpo.length() < tamanhoMinimo) {
            throw new Mensagens("O campo salário deve possuir no mínimo " + tamanhoMinimo + " caracteres numéricos");
        }

        return valor.doubleValue();
    }

    /**
     * Valida se um valor numérico atende ao tamanho mínimo especificado.
     * <p>
     * Exemplo: {@code validarTamanhoMinimoNumerico("25", 18)} retorna
     * {@code 25}</p>
     *
     * @param inputStr String contendo o valor numérico a ser validado
     * @param tamanhoMinimo Valor mínimo aceitável
     * @return O valor inteiro convertido se for válido
     * @throws Mensagens Se o valor for menor que o tamanho mínimo
     * @throws NumberFormatException Se a string não puder ser convertida para
     * inteiro
     */
    public static int validarTamanhoMinimoNumerico(String inputStr, int tamanhoMinimo)
            throws Mensagens, NumberFormatException {
        int valor = Integer.parseInt(inputStr);
        if (valor < tamanhoMinimo) {
            throw new Mensagens("O valor deve ser no mínimo " + tamanhoMinimo + ".");
        }
        return valor;
    }

    /**
     * Aplica formatações de máscara para os campos de um formulário de
     * professor. Configura máscaras para CPF, contato telefônico e formatação
     * monetária para salário seguindo o padrão brasileiro (pt-BR).
     *
     * <p>
     * Formatos aplicados:</p>
     * <ul>
     * <li>CPF: ###.###.###-## (ex: 123.456.789-00)</li>
     * <li>Contato: (##) # ####-#### (ex: (11) 9 8765-4321)</li>
     * <li>Salário: #,##0.00 (ex: 5.000,00) - formato decimal brasileiro</li>
     * </ul>
     *
     * @param cpfFormatado Campo de texto formatado para CPF
     * @param contatoFormatado Campo de texto formatado para telefone/contato
     * @param salarioFormatado Campo de texto formatado para valor de salário
     * @throws java.text.ParseException Se houver erro ao aplicar as máscaras de
     * formatação
     * @see javax.swing.text.MaskFormatter
     * @see javax.swing.text.NumberFormatter
     */
    public static void aplicarFormatacaoProfessor(javax.swing.JFormattedTextField cpfFormatado,
            javax.swing.JFormattedTextField contatoFormatado, javax.swing.JFormattedTextField salarioFormatado)
            throws java.text.ParseException {

        try {
            // Máscara de CPF
            MaskFormatter mask = new MaskFormatter("###.###.###-##");
            mask.install(cpfFormatado);

            // Máscara de Contato
            MaskFormatter mask2 = new MaskFormatter("(##) # ####-####");
            mask2.install(contatoFormatado);

            // Formatação de Salário (similar à versão do CadastroProfessor)
            java.text.DecimalFormat formatoDecimal = (java.text.DecimalFormat) java.text.NumberFormat
                    .getNumberInstance(new java.util.Locale("pt", "BR"));
            formatoDecimal.applyPattern("#,##0.00");

            NumberFormatter formatter = new NumberFormatter(formatoDecimal);
            formatter.setAllowsInvalid(true);
            formatter.setMinimum(0.0);
            formatter.setValueClass(Double.class);

            salarioFormatado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
            salarioFormatado.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);

        } catch (java.text.ParseException ex) {
            logger.log(Level.WARNING, "Erro de formatação", ex);
        }
    }
}
