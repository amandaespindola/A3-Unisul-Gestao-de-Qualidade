package security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.ValidadorInput;

class ValidadorInput_SecurityTest {

    // XSS / HTML Injection
    @Test
    @DisplayName("XSS básico não deve ser aceito")
    void testXSSBasico() {
        assertFalse(ValidadorInput.validarSegurancaBasica("<script>alert(1)</script>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("<img src=x onerror=alert(1)>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("<b>negrito</b>"));
    }

    @Test
    @DisplayName("HTML disfarçado não deve ser aceito")
    void testHTMLDisfarcado() {
        assertFalse(ValidadorInput.validarSegurancaBasica("<<script>>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("<script/>"));
        assertFalse(ValidadorInput.validarSegurancaBasica("</closeTag>"));
    }

    // SQL Injection
    @Test
    @DisplayName("SQL Injection básico deve ser bloqueado")
    void testSQLInjection() {
        assertFalse(ValidadorInput.validarSegurancaBasica("' OR '1'='1"));
        assertFalse(ValidadorInput.validarSegurancaBasica("' OR 1=1 --"));
        assertFalse(ValidadorInput.validarSegurancaBasica("DROP TABLE tb_alunos"));
        assertFalse(ValidadorInput.validarSegurancaBasica("SELECT * FROM users"));
    }

    @Test
    @DisplayName("SQL Injection obfuscado deve ser bloqueado")
    void testSQLInjectionObfuscado() {
        assertFalse(ValidadorInput.validarSegurancaBasica("'; EXEC xp_cmdshell('dir'); --"));
        assertFalse(ValidadorInput.validarSegurancaBasica("%00 OR 1=1"));
        assertFalse(ValidadorInput.validarSegurancaBasica("\" OR \"\"=\""));
    }

    // unicode & emojis
    @Test
    @DisplayName("Emojis devem ser rejeitados")
    void testEmojis() {
        assertFalse(ValidadorInput.validarSegurancaBasica("Amanda 😎"));
        assertFalse(ValidadorInput.validarSegurancaBasica("Teste 🚀🔥"));
        assertFalse(ValidadorInput.validarSegurancaBasica("Oi 😊"));
    }

    @Test
    @DisplayName("Unicode suspeito deve ser bloqueado")
    void testUnicodeSuspeito() {
        String reverser = "Admin\u202Eexe"; // Bidi override
        assertFalse(ValidadorInput.validarSegurancaBasica(reverser));
    }

    // shell ou command Injection
    @Test
    @DisplayName("Shell injection deve ser rejeitado")
    void testShellInjection() {
        assertFalse(ValidadorInput.validarSegurancaBasica("`rm -rf /`"));
        assertFalse(ValidadorInput.validarSegurancaBasica("$(shutdown -h now)"));
        assertFalse(ValidadorInput.validarSegurancaBasica("| ls"));
        assertFalse(ValidadorInput.validarSegurancaBasica("|| whoami"));
    }

    // path traversal
    @Test
    @DisplayName("Path Traversal deve ser rejeitado")
    void testPathTraversal() {
        assertFalse(ValidadorInput.validarSegurancaBasica("../../etc/passwd"));
        assertFalse(ValidadorInput.validarSegurancaBasica("..\\..\\secrets.txt"));
        assertFalse(ValidadorInput.validarSegurancaBasica("/../../boot.ini"));
    }

    // entradas seguras
    @Test
    @DisplayName("Entradas limpas devem ser aceitas")
    void testEntradasSeguras() {
        assertTrue(ValidadorInput.validarSegurancaBasica("Amanda"));
        assertTrue(ValidadorInput.validarSegurancaBasica("Joao da Silva"));
        assertTrue(ValidadorInput.validarSegurancaBasica("Maria Eduarda"));
        assertTrue(ValidadorInput.validarSegurancaBasica("Aluno123"));
    }
}
