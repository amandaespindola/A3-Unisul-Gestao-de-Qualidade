package utils;

import java.util.*;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import view.Mensagens;
import java.util.logging.Level;

public final class ValidadorInput {

    private static final java.util.logging.Logger logger
            = java.util.logging.Logger.getLogger(ValidadorInput.class.getName());

    private ValidadorInput() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static String removerMascara(String input) {
        return (input == null) ? "" : input.replaceAll("\\D", "");
    }

    public static int calculaIdade(Date dataNasc) {
        Calendar nasc = Calendar.getInstance();
        nasc.setTime(dataNasc);

        Calendar hoje = Calendar.getInstance();

        int idade = hoje.get(Calendar.YEAR) - nasc.get(Calendar.YEAR);
        nasc.add(Calendar.YEAR, idade);

        if (hoje.before(nasc)) {
            idade--;
        }

        return idade;
    }

    public static String validarNome(String nome, int minLength) throws Mensagens {
        if (nome == null || nome.length() < minLength) {
            throw new Mensagens("Nome deve conter ao menos " + minLength + " caracteres.");
        }
        return nome;
    }

    public static String validarSelecaoComboBox(int index, List<String> opcoes, String mensagem)
            throws Mensagens {
        if (index <= 0 || index >= opcoes.size()) {
            throw new Mensagens("Escolha o valor para o campo: " + mensagem);
        }
        return opcoes.get(index);
    }

    public static String validarTamanhoNumericoFixo(String input, int tamanho, String msg)
            throws Mensagens {

        String limpo = removerMascara(input);

        if (limpo.length() != tamanho) {
            throw new Mensagens("O campo " + msg + " deve possuir " + tamanho + " caracteres numéricos");
        }
        return input;
    }

    public static int validarIdadePorData(Date dataNasc, int idadeMin) throws Mensagens {
        if (dataNasc == null) {
            throw new Mensagens("Data de Nascimento não pode ser vazia.");
        }

        int idade = calculaIdade(dataNasc);
        if (idade < idadeMin) {
            throw new Mensagens("Professor deve ter ao menos " + idadeMin + " anos.");
        }

        return idade;
    }

    public static double validarSalario(JFormattedTextField campo, int minDigits) throws Mensagens {

        try {
            String texto = campo.getText()
                    .replace(".", "")
                    .replace(",", ".")
                    .replaceAll("[^0-9.]", "");

            if (texto.isBlank()) {
                throw new Mensagens("Informe um salário válido");
            }

            double valor = Double.parseDouble(texto);

            if (valor <= 0) {
                throw new Mensagens("Informe um salário válido");
            }

            if (String.valueOf((long) valor).length() < minDigits) {
                throw new Mensagens("O campo salário deve possuir no mínimo " + minDigits + " caracteres numéricos");
            }

            return valor;

        } catch (Exception e) {
            throw new Mensagens("Informe um salário válido");
        }
    }

    public static int validarTamanhoMinimoNumerico(String texto, int min)
            throws Mensagens {

        try {
            int n = Integer.parseInt(texto);
            if (n < min) {
                throw new Mensagens("Valor deve ser no mínimo " + min);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new Mensagens("Informe um número válido.");
        }
    }

    public static void aplicarFormatacaoProfessor(
            JFormattedTextField cpf,
            JFormattedTextField contato,
            JFormattedTextField salario) {

        try {
            MaskFormatter maskCpf = new MaskFormatter("###.###.###-##");
            maskCpf.install(cpf);

            MaskFormatter maskContato = new MaskFormatter("(##) # ####-####");
            maskContato.install(contato);

            java.text.DecimalFormat decimal
                    = (java.text.DecimalFormat) java.text.NumberFormat.getNumberInstance(new Locale("pt", "BR"));

            decimal.applyPattern("#,##0.00");

            NumberFormatter formatter = new NumberFormatter(decimal);
            formatter.setValueClass(Double.class);
            formatter.setMinimum(0.0);

            salario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));

        } catch (Exception e) {
            logger.log(Level.WARNING, "Erro ao formatar campos do professor", e);
        }
    }
}
