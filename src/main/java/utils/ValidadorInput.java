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

public final class ValidadorInput {
	private static final java.util.logging.Logger logger = java.util.logging.Logger
			.getLogger(ValidadorInput.class.getName());

	private ValidadorInput() {
		throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
	}

	// Remove caracteres não-numéricos (máscara) de uma string
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

	// Calcula a idade com base na data de nascimento
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

	// Validação de Nome
	public static String validarNome(String nome, int minLength) throws Mensagens {
		if (nome == null || nome.length() < minLength) {
			throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
		}
		return nome;
	}

	// Validação de Seleção de ComboBox
	public static String validarSelecaoComboBox(int selectedIndex, List<String> opcoes, String campoMensagem)
			throws Mensagens {
		if (selectedIndex <= 0 || selectedIndex >= opcoes.size()) { // O índice 0 é sempre o valor padrão ("-" ou
																	// similar)
			throw new Mensagens("Escolha o valor para o campo: " + campoMensagem);
		}
		return opcoes.get(selectedIndex);
	}

	// Validação de CPF/Contato (Tamanho Numérico Fixo)
	public static String validarTamanhoNumericoFixo(String inputFormatado, int tamanhoObrigatorio, String campoMensagem)
			throws Mensagens {
		String inputLimpo = removerMascara(inputFormatado);
		if (inputLimpo.length() != tamanhoObrigatorio) {
			throw new Mensagens(
					"O campo " + campoMensagem + " deve possuir " + tamanhoObrigatorio + " caracteres numéricos");
		}
		return inputFormatado; // Retorna o valor formatado (com máscara)
	}

	// Validação de Idade por Data de Nascimento
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

	// Validação de Salário (Tamanho Numérico Mínimo e Conversão)
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

	// Validação de Tamanho Mínimo Numérico (Para Campos como Idade/Fase)
	public static int validarTamanhoMinimoNumerico(String inputStr, int tamanhoMinimo)
			throws Mensagens, NumberFormatException {
		int valor = Integer.parseInt(inputStr);
		if (valor < tamanhoMinimo) {
			throw new Mensagens("O valor deve ser no mínimo " + tamanhoMinimo + ".");
		}
		return valor;
	}

	// Formatação de campos de Cadastro/Edição de Professor
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
