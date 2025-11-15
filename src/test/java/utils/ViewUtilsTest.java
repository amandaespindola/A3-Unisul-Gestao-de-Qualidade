package utils;

import org.junit.jupiter.api.Test;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import static org.junit.jupiter.api.Assertions.*;

public class ViewUtilsTest {

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

        assertNotNull(btn);
        assertEquals("Salvar", btn.getText());

        btn.doClick();

        assertTrue(clicado[0], "O botão deve acionar a ação associada.");
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
        assertEquals(SwingConstants.CENTER, lbl.getHorizontalAlignment());
        assertTrue(lbl.getFont().isBold());
    }

    @Test
    void testCriarPainelSuperiorTitulo() {
        JPanel painel = ViewUtils.criarPainelSuperiorTitulo("Cadastro de Professor");

        assertNotNull(painel);
        assertEquals(1, painel.getComponentCount());
        assertTrue(painel.getComponent(0) instanceof JLabel);
    }

    @Test
    void testCriarMenuGerencia() {
        JMenuBar menuBar = ViewUtils.criarMenuGerencia(
                "Principal",
                () -> {
                },
                () -> {
                }
        );

        assertNotNull(menuBar);
        assertEquals(1, menuBar.getMenuCount());
        assertEquals("Arquivo", menuBar.getMenu(0).getText());

        JMenu menu = menuBar.getMenu(0);
        assertEquals(3, menu.getItemCount());
    }
}
