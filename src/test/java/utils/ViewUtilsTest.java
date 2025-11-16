package utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewUtilsTest {

    // classe auxiliar para simular erro no campo formatado
    static class CampoQueSempreErra extends JFormattedTextField {

        @Override
        public void setValue(Object value) {
            throw new RuntimeException(new ParseException("erro", 0));
        }
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void testConfigurarJanela() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JFrame frame = new JFrame();
        ViewUtils.configurarJanela(frame, "Teste");

        assertEquals("Teste", frame.getTitle());
        assertFalse(frame.isResizable());
        assertEquals(WindowConstants.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
    }

    @Test
    void testFecharJanelaAoCancelar() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JFrame frame = new JFrame();
        JButton botao = new JButton();

        ViewUtils.fecharJanelaAoCancelar(botao, frame);

        assertFalse(frame.isDisplayable(), "Frame deve ter sido fechado.");
    }

    @Test
    void testCriarPainelBotoesGerencia() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JButton b1 = new JButton("A");
        JButton b2 = new JButton("B");
        JButton b3 = new JButton("C");
        JButton b4 = new JButton("D");
        JButton b5 = new JButton("E");

        JPanel painel = ViewUtils.criarPainelBotoesGerencia(b1, b2, b3, b4, b5);

        assertEquals(5, painel.getComponentCount());
    }

    @Test
    void testCriarPainelGerenciaTopo() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JPanel topo = ViewUtils.criarPainelGerenciaTopo(
                "Gerenciar X",
                new JButton("A"),
                new JButton("B"),
                new JButton("C"),
                new JButton("D"),
                new JButton("E")
        );

        assertTrue(topo.getComponent(0) instanceof JLabel);
        assertEquals("Gerenciar X", ((JLabel) topo.getComponent(0)).getText());
    }

    @Test
    void testCriarPainelBase() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JFrame f = new JFrame();
        JPanel p = ViewUtils.criarPainelBase(f);

        assertEquals(BorderLayout.class, p.getLayout().getClass());
    }

    @Test
    void testAdicionarBotoesConfirmarCancelar() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JPanel painel = new JPanel(new BorderLayout());
        JRootPane root = new JRootPane();

        ViewUtils.adicionarBotoesConfirmarCancelar(painel, () -> {
        }, () -> {
        }, root);

        Component comp = painel.getComponent(0);
        assertTrue(comp instanceof JPanel);
        assertEquals(2, ((JPanel) comp).getComponentCount());
    }

    @Test
    void testCriarBotoesGerencia() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JButton[] btns = ViewUtils.criarBotoesGerencia(() -> {
        }, () -> {
        }, () -> {
        }, () -> {
        }, () -> {
        }, Logger.getLogger("x"));

        assertEquals(5, btns.length);
    }

    @Test
    void testCriarLabel() {
        JLabel label = ViewUtils.criarLabel("Nome:", "lblNome");

        assertNotNull(label);
        assertEquals("Nome:", label.getText());
        assertEquals("lblNome", label.getName());
    }

    @Test
    void testCriarLabelTitulo() {
        JLabel label = ViewUtils.criarLabelTitulo("Título Teste");

        assertNotNull(label);
        assertEquals("Título Teste", label.getText());
        assertEquals(SwingConstants.CENTER, label.getHorizontalAlignment());
        assertTrue(label.getFont().isBold());
    }

    @Test
    void testCriarBotao() {
        final boolean[] clicado = {false};

        JButton btn = ViewUtils.criarBotao("Salvar", e -> clicado[0] = true);

        assertEquals("Salvar", btn.getText());
        btn.doClick();
        assertTrue(clicado[0]);
    }

    @Test
    void testAddLabel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ViewUtils.addLabel(form, gbc, 0, 0, "Nome", "lblNome");

        assertEquals(1, form.getComponentCount());
        Component comp = form.getComponent(0);
        assertTrue(comp instanceof JLabel);
        assertEquals("lblNome", comp.getName());
    }

    @Test
    void testAddCampo() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JTextField campo = new JTextField("Teste");
        ViewUtils.addCampo(form, gbc, 1, 2, 2, campo);

        assertEquals(1, form.getComponentCount());
        assertTrue(form.getComponent(0) instanceof JTextField);
    }

    @Test
    void testCriarTituloTela() {
        JLabel lbl = ViewUtils.criarTituloTela("Gerenciar Alunos");

        assertNotNull(lbl);
        assertEquals("Gerenciar Alunos", lbl.getText());
        assertTrue(lbl.getFont().isBold());
    }

    @Test
    void testCriarPainelSuperiorTitulo() {
        JPanel painel = ViewUtils.criarPainelSuperiorTitulo("Cadastro");

        assertNotNull(painel);
        assertEquals(1, painel.getComponentCount());
        assertTrue(painel.getComponent(0) instanceof JLabel);
    }

    @Test
    void testCriarMenuGerencia() {
        JMenuBar menuBar = ViewUtils.criarMenuGerencia("Principal", () -> {
        }, () -> {
        });

        assertEquals("Arquivo", menuBar.getMenu(0).getText());
        assertEquals(3, menuBar.getMenu(0).getItemCount());
    }

    @Test
    void testAplicarFormatacaoProfessorComAlerta_ParseException() {
        JFormattedTextField c1 = new CampoQueSempreErra();
        JFormattedTextField c2 = new CampoQueSempreErra();
        JFormattedTextField c3 = new CampoQueSempreErra();

        if (GraphicsEnvironment.isHeadless()) {
            assertThrows(HeadlessException.class,
                    () -> ViewUtils.aplicarFormatacaoProfessorComAlerta(null, c1, c2, c3));
        } else {
            assertDoesNotThrow(
                    () -> ViewUtils.aplicarFormatacaoProfessorComAlerta(null, c1, c2, c3));
        }
    }
}
