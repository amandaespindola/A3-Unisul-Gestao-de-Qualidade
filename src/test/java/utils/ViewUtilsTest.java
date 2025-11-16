package utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.junit.jupiter.api.Test;

class ViewUtilsTest {

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
    void testCriarPainelBotoesGerencia() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JButton bAtualizar = new JButton("Atualizar");
        JButton bCadastrar = new JButton("Cadastrar");
        JButton bEditar = new JButton("Editar");
        JButton bDeletar = new JButton("Deletar");
        JButton bExportar = new JButton("Exportar");

        JPanel painel = ViewUtils.criarPainelBotoesGerencia(
                bAtualizar, bCadastrar, bEditar, bDeletar, bExportar
        );

        assertNotNull(painel);
        assertEquals(5, painel.getComponentCount());
    }

    @Test
    void testCriarPainelGerenciaTopo() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JPanel topo = ViewUtils.criarPainelGerenciaTopo(
                "Gerenciar X",
                new JButton("Atualizar"),
                new JButton("Cadastrar"),
                new JButton("Editar"),
                new JButton("Deletar"),
                new JButton("Exportar")
        );

        assertNotNull(topo);
        assertTrue(topo.getComponent(0) instanceof JLabel);
        assertEquals("Gerenciar X", ((JLabel) topo.getComponent(0)).getText());
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

    @Test
    void testCriarPainelBase() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JFrame frame = new JFrame();
        JPanel painel = ViewUtils.criarPainelBase(frame);

        assertNotNull(painel);
        assertEquals(BorderLayout.class, painel.getLayout().getClass());
    }

    @Test
    void testAdicionarBotoesConfirmarCancelar() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JPanel painel = new JPanel(new BorderLayout());
        JRootPane root = new JRootPane();

        ViewUtils.adicionarBotoesConfirmarCancelar(
                painel,
                () -> {
                },
                () -> {
                },
                root
        );

        Component comp = painel.getComponent(0); // BorderLayout.SOUTH = index 0
        assertTrue(comp instanceof JPanel);

        JPanel botoes = (JPanel) comp;
        assertEquals(2, botoes.getComponentCount());
    }

    @Test
    void testCriarBotoesGerencia() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Sem ambiente gráfico — ignorado.");

        JButton[] btns = ViewUtils.criarBotoesGerencia(() -> {
        }, () -> {
        }, () -> {
        }, () -> {
        }, () -> {
        }, Logger.getLogger("test"));

        assertEquals(5, btns.length);
        assertEquals("Atualizar tabela", btns[0].getText());
        assertEquals("Cadastrar novo", btns[1].getText());
        assertEquals("Editar", btns[2].getText());
        assertEquals("Deletar", btns[3].getText());
        assertEquals("Exportar para Excel", btns[4].getText());
    }

}
