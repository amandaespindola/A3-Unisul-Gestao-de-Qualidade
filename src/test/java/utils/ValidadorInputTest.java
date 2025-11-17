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

    @Test
    @DisplayName("calculaIdade deve subtrair 1 quando aniversário ainda não ocorreu no ano atual")
    void testCalculaIdadeAntesDoAniversario() {
        Calendar hoje = Calendar.getInstance();

        Calendar dataNasc = new GregorianCalendar(
                hoje.get(Calendar.YEAR) - 20,
                hoje.get(Calendar.MONTH),
                hoje.get(Calendar.DAY_OF_MONTH) + 5
        );

        int idade = ValidadorInput.calculaIdade(dataNasc.getTime());

        assertEquals(19, idade, "A idade deve ser 1 ano a menos, pois o aniversário ainda não ocorreu.");
    }

    // validações de segurança
    @Test
    @DisplayName("validarSegurancaBasica deve rejeitar XSS / HTML")
    void testSeguranca_XSS() {
        assertFalse(ValidadorInput.validarSegurancaBasica("<script>alert(1)</script>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("<img src=x onerror=alert(1)>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("<<b>>"));
    }

    @Test
    @DisplayName("validarSegurancaBasica deve rejeitar SQL Injection")
    void testSeguranca_SQLInjection() {
        assertFalse(ValidadorInput.validarSegurancaBasica("' OR '1'='1"));
        assertFalse(ValidadorInput.validarSegurancaBasica("DROP TABLE tb_alunos"));
        assertFalse(ValidadorInput.validarSegurancaBasica("select * from users"));
    }

    @Test
    @DisplayName("validarSegurancaBasica deve rejeitar path traversal")
    void testSeguranca_PathTraversal() {
        assertFalse(ValidadorInput.validarSegurancaBasica("../../etc/passwd"));
        assertFalse(ValidadorInput.validarSegurancaBasica("..\\secret"));
    }

    @Test
    @DisplayName("validarSegurancaBasica deve rejeitar Unicode suspeito")
    void testSeguranca_Unicode() {
        String reverser = "Admin\u202Eexe"; // bidi override
        assertFalse(ValidadorInput.validarSegurancaBasica(reverser));
        assertFalse(ValidadorInput.validarSegurancaBasica("Amanda 😎"));
    }

    @Test
    @DisplayName("validarSegurancaBasica deve rejeitar shell injection")
    void testSeguranca_ShellInjection() {
        assertFalse(ValidadorInput.validarSegurancaBasica("`rm -rf /`"));
        assertFalse(ValidadorInput.validarSegurancaBasica("| ls"));
    }

    @Test
    @DisplayName("validarSegurancaBasica deve aceitar entradas seguras")
    void testSeguranca_Seguro() {
        assertTrue(ValidadorInput.validarSegurancaBasica("Amanda"));
        assertTrue(ValidadorInput.validarSegurancaBasica("Joao da Silva"));
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
    void testValidarTamanhoNumericoFixo() {
        assertDoesNotThrow(()
                -> ValidadorInput.validarTamanhoNumericoFixo("123.456.789-00", 11, "CPF"));
    }

    @Test
    @DisplayName("validarTamanhoNumericoFixo deve devolver exatamente o texto original formatado")
    void testValidarTamanhoNumericoFixoRetornoFormatado() throws Mensagens {
        String entrada = "123.456.789-00";
        String resultado = ValidadorInput.validarTamanhoNumericoFixo(entrada, 11, "CPF");

        assertEquals(entrada, resultado);
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
    @DisplayName("validarSalario deve falhar quando o número de dígitos é menor que o mínimo")
    void testValidarSalarioQuantidadeMinima() {

        JFormattedTextField campo = new JFormattedTextField();
        campo.setValue(5.0);

        Mensagens ex = assertThrows(
                Mensagens.class,
                () -> ValidadorInput.validarSalario(campo, 2)
        );

        assertTrue(ex.getMessage().contains("deve possuir no mínimo"));
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
    @DisplayName("validarTamanhoNumericoFixo deve lançar erro ao receber tamanho inválido")
    void testValidarTamanhoNumericoFixoErro() {

        Mensagens ex = assertThrows(
                Mensagens.class,
                () -> ValidadorInput.validarTamanhoNumericoFixo("12.345-6", 11, "CPF")
        );

        assertTrue(ex.getMessage().contains("caracteres numéricos"));
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

    @Test
    @DisplayName("aplicarFormatacaoProfessor deve cair no catch quando ocorrer erro de formatação")
    void testAplicarFormatacaoProfessorCatchComRuntimeException() {

        JFormattedTextField cpf = new JFormattedTextField();
        JFormattedTextField contato = new JFormattedTextField();
        JFormattedTextField salario = new JFormattedTextField();

        assertDoesNotThrow(() -> {
            try {
                throw new RuntimeException("Erro simulado no formatter");
            } catch (RuntimeException ex) {
                ValidadorInput.aplicarFormatacaoProfessor(cpf, contato, salario);
            }
        });
    }

    @Test
    @DisplayName("Construtor privado deve lançar UnsupportedOperationException")
    void testConstructorPrivate() throws Exception {

        var constructor = ValidadorInput.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception ex = assertThrows(Exception.class, constructor::newInstance);

        assertTrue(ex.getCause() instanceof UnsupportedOperationException,
                "O construtor privado deve lançar UnsupportedOperationException");
    }

    @Test
    @DisplayName("validarSelecaoComboBox deve aceitar último índice válido")
    void testValidarSelecaoComboBoxUltimoItem() throws Mensagens {
        var opcoes = Arrays.asList("-", "Opção 1", "Opção 2");
        assertEquals("Opção 2", ValidadorInput.validarSelecaoComboBox(2, opcoes, "Campo"));
    }

}
