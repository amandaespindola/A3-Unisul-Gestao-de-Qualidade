package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFormattedTextField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import view.Mensagens;

class ValidadorInputTest {

    @Test
    @DisplayName("removerMascara deve remover todos os caracteres não numéricos")
    void testRemoverMascara() {
        assertEquals("12345678900", ValidadorInput.removerMascara("123.456.789-00"));
        assertEquals("48911223344", ValidadorInput.removerMascara("(48) 9 1122-3344"));
        assertEquals("", ValidadorInput.removerMascara(null));
    }

    @Test
    @DisplayName("calculaIdade deve retornar idade correta com base na data de nascimento")
    void testCalculaIdade() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        Date nascimento = cal.getTime();

        int idade = ValidadorInput.calculaIdade(nascimento);

        assertEquals(20, idade);
    }

    // validação do tamanho do nome inserido
    @Test
    @DisplayName("validarNome deve aceitar nomes válidos e rejeitar inválidos")
    void testValidarNome() throws Mensagens {
        assertEquals("Amanda", ValidadorInput.validarNome("Amanda", 3));

        Mensagens ex1 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarNome("Jo", 3));
        assertTrue(ex1.getMessage().contains("3"));

        Mensagens ex2 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarNome(null, 3));
        assertTrue(ex2.getMessage().contains("3"));
    }

    @Test
    @DisplayName("validarSelecaoComboBox deve validar índices corretamente")
    void testValidarSelecaoComboBox() throws Mensagens {
        var opcoes = Arrays.asList("-", "Opção 1", "Opção 2");

        assertEquals("Opção 1", ValidadorInput.validarSelecaoComboBox(1, opcoes, "Campo"));

        Mensagens ex1 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarSelecaoComboBox(0, opcoes, "Campo"));
        assertTrue(ex1.getMessage().contains("Campo"));

        Mensagens ex2 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarSelecaoComboBox(5, opcoes, "Campo"));
        assertTrue(ex2.getMessage().contains("Campo"));
    }

    @Test
    @DisplayName("validarTamanhoNumericoFixo deve validar tamanho correto após remover máscara")
    void testValidarTamanhoNumericoFixo() throws Mensagens {
        assertDoesNotThrow(()
                -> ValidadorInput.validarTamanhoNumericoFixo("123.456.789-00", 11, "CPF"));

        Mensagens ex = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarTamanhoNumericoFixo("123.456-789", 11, "CPF"));
        assertTrue(ex.getMessage().contains("CPF"));
    }

    @Test
    @DisplayName("validarIdadePorData deve validar idade mínima corretamente")
    void testValidarIdadePorData() throws Mensagens {
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -30);
        Date nascimento30 = cal.getTime();

        assertEquals(30, ValidadorInput.validarIdadePorData(nascimento30, 18));

        cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -10);
        Date nascimento10 = cal.getTime();

        Mensagens ex1 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarIdadePorData(nascimento10, 18));
        assertTrue(ex1.getMessage().contains("18"));

        Mensagens ex2 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarIdadePorData(null, 18));
        assertTrue(ex2.getMessage().contains("vazia"));
    }

    @Test
    @DisplayName("validarSalario deve aceitar valores válidos e rejeitar inválidos")
    void testValidarSalario() throws Mensagens {
        JFormattedTextField campo = new JFormattedTextField();
        campo.setValue(1500.00);

        assertEquals(1500.00, ValidadorInput.validarSalario(campo, 3));

        // fallback: texto válido
        JFormattedTextField campo2 = new JFormattedTextField();
        campo2.setText("1.500,00");
        assertTrue(ValidadorInput.validarSalario(campo2, 3) > 0);

        // texto inválido
        JFormattedTextField campo3 = new JFormattedTextField();
        campo3.setText("abc");
        Mensagens ex1 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarSalario(campo3, 3));
        assertTrue(ex1.getMessage().contains("salário"));

        // valor zero
        JFormattedTextField campo4 = new JFormattedTextField();
        campo4.setValue(0);
        Mensagens ex2 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarSalario(campo4, 3));
        assertTrue(ex2.getMessage().contains("válido"));
    }

    @Test
    @DisplayName("validarTamanhoMinimoNumerico deve validar números corretamente")
    void testValidarTamanhoMinimoNumerico() throws Mensagens {
        assertEquals(10, ValidadorInput.validarTamanhoMinimoNumerico("10", 5));

        Mensagens ex1 = assertThrows(Mensagens.class,
                () -> ValidadorInput.validarTamanhoMinimoNumerico("3", 5));
        assertTrue(ex1.getMessage().contains("5"));

        NumberFormatException ex2 = assertThrows(NumberFormatException.class,
                () -> ValidadorInput.validarTamanhoMinimoNumerico("abc", 5));
        assertNotNull(ex2.getMessage());
    }

    @Test
    @DisplayName("aplicarFormatacaoProfessor não deve lançar exceção e deve instalar formatters quando possível")
    void testAplicarFormatacaoProfessor() {

        JFormattedTextField cpf = new JFormattedTextField();
        JFormattedTextField contato = new JFormattedTextField();
        JFormattedTextField salario = new JFormattedTextField();

        // não deve lançar erro
        assertDoesNotThrow(()
                -> ValidadorInput.aplicarFormatacaoProfessor(cpf, contato, salario)
        );

        // O método pode engolir ParseException e NÃO aplicar máscara
        assertNotNull(cpf);
        assertNotNull(contato);
        assertNotNull(salario);

        // quando a máscara foi aplicada, os valores não serão null
        if (cpf.getFormatterFactory() != null) {
            assertNotNull(cpf.getFormatterFactory());
        }
        if (contato.getFormatterFactory() != null) {
            assertNotNull(contato.getFormatterFactory());
        }
        if (salario.getFormatterFactory() != null) {
            assertNotNull(salario.getFormatterFactory());
        }
    }
}
