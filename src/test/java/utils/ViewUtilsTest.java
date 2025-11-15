package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewUtilsTest {

    @Test
    @DisplayName("criarLabel deve criar JLabel com texto e name corretos")
    void testCriarLabel() {
        JLabel lbl = ViewUtils.criarLabel("Nome:", "lblNome");

        assertNotNull(lbl);
        assertEquals("Nome:", lbl.getText());
        assertEquals("lblNome", lbl.getName());
    }

    @Test
    @DisplayName("criarLabelTitulo deve criar título com fonte grande, bold e alinhamento central")
    void testCriarLabelTitulo() {
        JLabel lbl = ViewUtils.criarLabelTitulo("Título Teste");

        assertNotNull(lbl);
        assertEquals("Título Teste", lbl.getText());
        assertEquals(Font.BOLD, lbl.getFont().getStyle());
        assertTrue(lbl.getFont().getSize() >= 20);
        assertEquals(JLabel.CENTER, lbl.getHorizontalAlignment());
    }

    @Test
    @DisplayName("criarTituloTela deve criar JLabel centralizado, com borda e fonte grande")
    void testCriarTituloTela() {
        JLabel lbl = ViewUtils.criarTituloTela("Gerenciamento");

        assertNotNull(lbl);
        assertEquals("Gerenciamento", lbl.getText());
        assertEquals(JLabel.CENTER, lbl.getHorizontalAlignment());
        assertTrue(lbl.getFont().getSize() >= 30);
    }

    @Test
    @DisplayName("criarBotao deve criar JButton com texto correto e ação executada")
    void testCriarBotao() {
        AtomicBoolean clicado = new AtomicBoolean(false);

        JButton btn = ViewUtils.criarBotao("Salvar", evt -> clicado.set(true));

        assertNotNull(btn);
        assertEquals("Salvar", btn.getText());

        btn.doClick();
        assertTrue(clicado.get());
    }

    @Test
    @DisplayName("criarBotoesGerencia deve retornar array com 5 botões funcionando")
    void testCriarBotoesGerencia() {
        AtomicBoolean a = new AtomicBoolean(false);
        AtomicBoolean b = new AtomicBoolean(false);
        AtomicBoolean c = new AtomicBoolean(false);
        AtomicBoolean d = new AtomicBoolean(false);
        AtomicBoolean e = new AtomicBoolean(false);

        Logger logger = Logger.getLogger("teste");

        JButton[] botoes = ViewUtils.criarBotoesGerencia(
                () -> a.set(true),
                () -> b.set(true),
                () -> c.set(true),
                () -> d.set(true),
                () -> e.set(true),
                logger
        );

        assertEquals(5, botoes.length);

        botoes[0].doClick();
        assertTrue(a.get());

        botoes[1].doClick();
        assertTrue(b.get());
    }

    @Test
    @DisplayName("configurarJanela deve aplicar configurações básicas no JFrame")
    void testConfigurarJanela() {
        JFrame frame = new JFrame();
        ViewUtils.configurarJanela(frame, "Teste");

        assertEquals("Teste", frame.getTitle());
        assertFalse(frame.isResizable());
        assertEquals(JFrame.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
    }

    @Test
    @DisplayName("tratarErroCadastro deve exibir erro sem lançar exceção")
    void testTratarErroCadastro() {
        assertDoesNotThrow(() -> ViewUtils.tratarErroCadastro(new Exception("Erro genérico")));
        assertDoesNotThrow(() -> ViewUtils.tratarErroCadastro(new NumberFormatException()));
    }

    @Test
    @DisplayName("tratarErroCadastro deve tratar qualquer exceção sem lançar erro")
    void testTratarErroCadastroComExcecaoCustomizada() {
        Exception ex = new Exception("Mensagem customizada");

        assertDoesNotThrow(() -> ViewUtils.tratarErroCadastro(ex));
    }

    @Test
    @DisplayName("fecharJanelaAoCancelar deve fechar a janela apenas se source for JButton")

    void testFecharJanelaAoCancelar() {
        JFrame frame = new JFrame();
        frame.setVisible(true);

        JButton btn = new JButton();
        ViewUtils.fecharJanelaAoCancelar(btn, frame);
        assertFalse(frame.isVisible()); // janela fechada

        JFrame frame2 = new JFrame();
        frame2.setVisible(true);
        ViewUtils.fecharJanelaAoCancelar("texto", frame2);
        assertTrue(frame2.isVisible()); // não fecha
    }

    @Test
    @DisplayName("fecharJanelaAoCancelar não deve fechar janela quando source é null")
    void testFecharJanelaAoCancelarComSourceNull() {
        JFrame frame = new JFrame();
        frame.setVisible(true);

        ViewUtils.fecharJanelaAoCancelar(null, frame);

        assertTrue(frame.isVisible(), "Janela não deveria ser fechada quando source é null");
    }

    @Test
    @DisplayName("addLabel deve adicionar componente no painel")
    void testAddLabel() {
        JPanel painel = new JPanel();
        var gbc = new java.awt.GridBagConstraints();

        ViewUtils.addLabel(painel, gbc, 0, 0, "Nome", "lblNome");

        assertEquals(1, painel.getComponentCount());
        assertTrue(painel.getComponent(0) instanceof JLabel);
    }

    @Test
    @DisplayName("addCampo deve adicionar campo ao painel")
    void testAddCampo() {
        JPanel painel = new JPanel();
        var gbc = new java.awt.GridBagConstraints();
        JTextField campo = new JTextField();

        ViewUtils.addCampo(painel, gbc, 1, 1, 2, campo);

        assertEquals(1, painel.getComponentCount());
        assertEquals(campo, painel.getComponent(0));
    }

    @Test
    @DisplayName("criarPainelSuperiorTitulo deve retornar painel com título")
    void testCriarPainelSuperiorTitulo() {
        JPanel painel = ViewUtils.criarPainelSuperiorTitulo("Titulo");

        assertNotNull(painel);
        assertTrue(painel.getComponentCount() >= 1);
        assertTrue(painel.getComponent(0) instanceof JLabel);
    }

    @Test
    @DisplayName("criarPainelGerenciaTopo deve criar painel com título e botões")
    void testCriarPainelGerenciaTopo() {
        JButton a = new JButton();
        JPanel p = ViewUtils.criarPainelGerenciaTopo(
                "Teste",
                a, a, a, a, a
        );

        assertNotNull(p);
        assertTrue(p.getComponentCount() >= 2);
    }

    @Test
    @DisplayName("criarPainelBase deve adicionar painel ao frame")
    void testCriarPainelBase() {
        JFrame frame = new JFrame();
        JPanel painel = ViewUtils.criarPainelBase(frame);

        assertNotNull(painel);
        assertEquals(BorderLayout.CENTER, ((BorderLayout) frame.getContentPane().getLayout()).getConstraints(painel));
    }

    @Test
    @DisplayName("criarMenuGerencia deve criar JMenuBar com itens funcionando")
    void testCriarMenuGerencia() {
        AtomicBoolean principal = new AtomicBoolean(false);
        AtomicBoolean sobre = new AtomicBoolean(false);

        JMenuBar bar = ViewUtils.criarMenuGerencia("Professores",
                () -> principal.set(true),
                () -> sobre.set(true)
        );

        assertNotNull(bar);
        assertEquals(1, bar.getMenuCount());

        JMenu menu = bar.getMenu(0);
        assertEquals("Arquivo", menu.getText());

        JMenuItem item0 = menu.getItem(0);
        item0.doClick();
        assertTrue(principal.get());

        JMenuItem item1 = menu.getItem(1);
        item1.doClick();
        assertTrue(sobre.get());
    }

    @Test
    @DisplayName("adicionarBotoesConfirmarCancelar deve adicionar ambos os botões")
    void testAdicionarBotoesConfirmarCancelar() {
        JPanel painel = new JPanel(new BorderLayout());
        JRootPane root = new JRootPane();

        AtomicBoolean c1 = new AtomicBoolean(false);
        AtomicBoolean c2 = new AtomicBoolean(false);

        ViewUtils.adicionarBotoesConfirmarCancelar(
                painel,
                () -> c1.set(true),
                () -> c2.set(true),
                root
        );

        Component comp = painel.getComponent(0);
        assertTrue(comp instanceof JPanel);

        JPanel botoes = (JPanel) comp;

        assertEquals(2, botoes.getComponentCount());

        ((JButton) botoes.getComponent(0)).doClick();
        assertTrue(c1.get());
    }

    @Test
    @DisplayName("aplicarFormatacaoProfessorComAlerta não deve lançar exceção")
    void testAplicarFormatacaoProfessorComAlerta() {
        JFormattedTextField cpf = new JFormattedTextField();
        JFormattedTextField contato = new JFormattedTextField();
        JFormattedTextField salario = new JFormattedTextField();

        assertDoesNotThrow(() -> ViewUtils.aplicarFormatacaoProfessorComAlerta(
                null, cpf, contato, salario
        ));
    }
}
