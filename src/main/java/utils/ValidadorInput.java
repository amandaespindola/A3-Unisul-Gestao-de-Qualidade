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
 * Classe utilitária responsável por validações e formatação de campos usados
 * no cadastro e edição de Alunos e Professores.
 * <p>
 * Contém métodos para validar nomes, seleções de combo box, CPF, contato,
 * salário, datas e valores numéricos. Também aplica formatações padrão em
 * campos formatados.
 */
public final class ValidadorInput {
	private static final java.util.logging.Logger logger = java.util.logging.Logger
			.getLogger(ValidadorInput.class.getName());

        /**
        * Construtor privado para impedir instanciação.
        */
	private ValidadorInput() {
		throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
	}

	/**
        * Remove todos os caracteres não numéricos de uma string (remove máscara).
        *
        * @param input valor de entrada possivelmente mascarado
        * @return string contendo apenas dígitos
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
        * Calcula idade a partir da data de nascimento.
        *
        * @param dataNasc data de nascimento
        * @return idade calculada
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
        * Valida um nome com tamanho mínimo obrigatório.
        *
        * @param nome       nome informado
        * @param minLength  mínimo de caracteres
        * @return o nome válido
        * @throws Mensagens caso o nome seja inválido
        */
	public static String validarNome(String nome, int minLength) throws Mensagens {
		if (nome == null || nome.length() < minLength) {
			throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
		}
		return nome;
	}

	/**
        * Valida se a opção selecionada de um ComboBox é válida.
        *
        * @param selectedIndex índice selecionado
        * @param opcoes        lista de opções disponíveis
        * @param campoMensagem nome do campo exibido na mensagem
        * @return valor selecionado
        * @throws Mensagens caso a seleção seja inválida
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
        * Valida campos numéricos de tamanho fixo (CPF, contato, etc.).
        *
        * @param inputFormatado      valor formatado
        * @param tamanhoObrigatorio  tamanho desejado (somente dígitos)
        * @param campoMensagem       nome do campo
        * @return valor formatado válido
        * @throws Mensagens caso o tamanho seja inválido
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
        * Valida idade mínima com base na data de nascimento.
        *
        * @param dataNasc    data informada
        * @param idadeMinima valor mínimo permitido
        * @return idade calculada
        * @throws Mensagens caso a idade seja inferior ao mínimo
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
        * Valida um campo de salário, garantindo valor numérico e tamanho mínimo.
        *
        * @param campo         componente JFormattedTextField de entrada
        * @param tamanhoMinimo quantidade mínima de dígitos numéricos
        * @return salário convertido para double
        * @throws Mensagens caso o salário seja inválido
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
        * Valida um valor numérico mínimo (usado para fase, idade, etc.).
        *
        * @param inputStr      string numérica
        * @param tamanhoMinimo valor mínimo permitido
        * @return valor convertido para int
        * @throws Mensagens caso o valor seja menor que o mínimo
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
        * Aplica formatação padrão aos campos de CPF, contato e salário do
        * formulário de Professor.
        *
        * @param cpfFormatado     campo de CPF
        * @param contatoFormatado campo de contato
        * @param salarioFormatado campo de salário
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
