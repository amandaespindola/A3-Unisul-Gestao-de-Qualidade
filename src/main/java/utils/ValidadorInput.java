package utils;

import View.Mensagens;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class ValidadorInput {

    private ValidadorInput() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    // Remove caracteres não-numéricos (máscara) de uma string
    public static String removerMascara(String input) {
        String str = "";
        if (input == null) {
            return str;
        }
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                str += input.charAt(i);
            }
        }
        return str;
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
    public static String validarSelecaoComboBox(int selectedIndex, String[] arrayValores, String campoMensagem) throws Mensagens {
        if (selectedIndex <= 0) { // O índice 0 é sempre o valor padrão ("-" ou similar)
            throw new Mensagens("Escolha o valor para o campo: " + campoMensagem);
        }
        return arrayValores[selectedIndex];
    }

    // Validação de CPF/Contato (Tamanho Numérico Fixo)
    public static String validarTamanhoNumericoFixo(String inputFormatado, int tamanhoObrigatorio, String campoMensagem) throws Mensagens {
        String inputLimpo = removerMascara(inputFormatado);
        if (inputLimpo.length() != tamanhoObrigatorio) {
            throw new Mensagens("O campo " + campoMensagem + " deve possuir " + tamanhoObrigatorio + " caracteres numéricos");
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
    public static double validarSalario(String salarioFormatado, int tamanhoMinimo) throws Mensagens, NumberFormatException {
        String salarioLimpo = removerMascara(salarioFormatado);
        if (salarioLimpo.length() < tamanhoMinimo) {
            throw new Mensagens("O campo salário deve possuir no mínimo " + tamanhoMinimo + " caracteres numéricos");
        }

        return Double.parseDouble(salarioLimpo);
    }

    // Validação de Tamanho Mínimo Numérico (Para Campos como Idade/Fase)
    public static int validarTamanhoMinimoNumerico(String inputStr, int tamanhoMinimo) throws Mensagens, NumberFormatException {
        int valor = Integer.parseInt(inputStr);
        if (valor < tamanhoMinimo) {
            throw new Mensagens("O valor deve ser no mínimo " + tamanhoMinimo + ".");
        }
        return valor;
    }
}
