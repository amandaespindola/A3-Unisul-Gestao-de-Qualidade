package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import view.Mensagens;

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
     * Construtor privado para impedir instância da classe.
     * Esta é uma classe utilitária.
     */
	private ValidadorInput() {
		throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
	}

	/**
     * Remove todos os caracteres não numéricos de uma string.
     *
     * @param input texto possivelmente contendo máscara ou caracteres especiais
     * @return apenas os dígitos presentes na string; vazio se {@code input} for nulo
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

		return sb.toString();
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
     * Valida um nome verificando se atinge o tamanho mínimo exigido.
     *
     * @param nome texto a validar
     * @param minLength número mínimo de caracteres
     * @return o nome, caso válido
     * @throws Mensagens caso o nome seja nulo ou curto demais
     */
	public static String validarNome(String nome, int minLength) throws Mensagens {
		if (nome == null || nome.length() < minLength) {
			throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
		}
		return nome;
	}

	/**
     * Valida se a seleção feita em um ComboBox é válida.
     * <p>O índice 0 é considerado valor padrão não selecionado.</p>
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
     * Valida campos numéricos que devem possuir tamanho exato,
     * como CPF ou número de contato.
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
     * @param tamanhoMinimo quantidade mínima de dígitos (sem considerar decimais)
     * @return o valor do salário convertido para double
     * @throws Mensagens caso o campo esteja vazio, seja inválido ou tenha valor curto demais
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
     * Valida um valor numérico garantindo que atinja um tamanho mínimo.
     *
     * @param inputStr string contendo número
     * @param tamanhoMinimo valor mínimo permitido
     * @return o valor convertido para inteiro
     * @throws Mensagens caso o valor seja menor que o permitido
     * @throws NumberFormatException caso o texto não seja numérico
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
     * Aplica máscara e formatação aos campos relacionados ao cadastro/edição de professor:
     * <ul>
     *     <li>CPF</li>
     *     <li>Contato telefônico</li>
     *     <li>Salário formatado no padrão brasileiro</li>
     * </ul>
     *
     * @param cpfFormatado campo de CPF
     * @param contatoFormatado campo de telefone
     * @param salarioFormatado campo de salário
     * @throws java.text.ParseException caso as máscaras não possam ser aplicadas
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
