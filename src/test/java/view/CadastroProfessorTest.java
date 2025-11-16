package view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;

import javax.swing.JPanel;
import javax.swing.JLabel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CadastroProfessorTest {

    @Test
    @DisplayName("A janela deve ser instanciada sem lançar exceções")
    void testCriacaoJanela() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");
        assertDoesNotThrow(CadastroProfessor::new);
    }

    @Test
    @DisplayName("Painel principal deve utilizar BorderLayout")
    void testLayoutPrincipal() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        CadastroProfessor tela = new CadastroProfessor();

        Component painel = tela.getContentPane().getComponent(0);
        assertTrue(painel instanceof JPanel);
        assertEquals(BorderLayout.class, ((JPanel) painel).getLayout().getClass());
    }

    @Test
    @DisplayName("Título deve existir no painel superior")
    void testTitulo() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        CadastroProfessor tela = new CadastroProfessor();

        JPanel painel = (JPanel) tela.getContentPane().getComponent(0);

        Component titulo = ((BorderLayout) painel.getLayout())
                .getLayoutComponent(BorderLayout.NORTH);

        assertNotNull(titulo);
        assertTrue(titulo instanceof JLabel);
    }

    @Test
    @DisplayName("Formulário deve existir e conter componentes")
    void testFormularioExiste() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        CadastroProfessor tela = new CadastroProfessor();

        JPanel painel = (JPanel) tela.getContentPane().getComponent(0);

        Component centro = ((BorderLayout) painel.getLayout())
                .getLayoutComponent(BorderLayout.CENTER);

        assertNotNull(centro);
        assertTrue(centro instanceof JPanel);

        JPanel form = (JPanel) centro;
        assertTrue(form.getComponentCount() > 0);
    }

    @Test
    @DisplayName("Construtor não deve abrir diálogos nem fechar a janela automaticamente")
    void testNaoExecutaConfirmarOuDAO() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        CadastroProfessor tela = new CadastroProfessor();

        assertTrue(tela.isDisplayable());
        assertTrue(tela.isEnabled());
    }
}