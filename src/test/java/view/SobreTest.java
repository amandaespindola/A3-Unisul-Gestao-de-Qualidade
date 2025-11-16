package view;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import utils.Constantes;

class SobreTest {

    @BeforeAll
    static void configurarAmbiente() {
        System.setProperty("java.awt.headless", "false");
    }

    @Test
    @DisplayName("A janela Sobre deve ser criada sem lançar exceções")
    void testCriacaoDaJanela() {
        assertDoesNotThrow(() -> {
            Sobre sobre = new Sobre();
            assertNotNull(sobre);
        });
    }

    @Test
    @DisplayName("Componentes principais devem existir após initComponents()")
    void testComponentesPrincipais() throws InvocationTargetException, InterruptedException {
        final Sobre[] janela = new Sobre[1];

        EventQueue.invokeAndWait(() -> janela[0] = new Sobre());

        Sobre sobre = janela[0];

        assertEquals(BorderLayout.class, sobre.getLayout().getClass());

        // verifica topo
        Component topo = sobre.getContentPane().getComponent(0);
        assertTrue(topo instanceof JPanel);

        // verifica separador central
        Component sep1 = sobre.getContentPane().getComponent(1);
        assertTrue(sep1 instanceof JSeparator);

        // painel do centro
        Component centro = sobre.getContentPane().getComponent(2);
        assertTrue(centro instanceof JPanel);
    }

    @Test
    @DisplayName("Título principal deve ser criado corretamente")
    void testTituloPrincipal() throws InvocationTargetException, InterruptedException {
        final Sobre[] janela = new Sobre[1];

        EventQueue.invokeAndWait(() -> janela[0] = new Sobre());
        Sobre sobre = janela[0];

        JPanel topo = (JPanel) sobre.getContentPane().getComponent(0);
        JLabel titulo = (JLabel) topo.getComponent(0);

        assertEquals(Constantes.UIConstants.SOBRE_INSTITUICAO, titulo.getText());
        assertTrue(titulo.getFont().isBold());
    }

    @Test
    @DisplayName("Painel de integrantes deve conter exatamente 6 labels")
    void testIntegrantes() throws InvocationTargetException, InterruptedException {
        final Sobre[] janela = new Sobre[1];

        EventQueue.invokeAndWait(() -> janela[0] = new Sobre());
        Sobre sobre = janela[0];

        JPanel centro = (JPanel) sobre.getContentPane().getComponent(2);
        JPanel integrantes = (JPanel) ((JPanel) centro).getComponent(0);

        assertEquals(6, integrantes.getComponentCount());
    }

    @Test
    @DisplayName("O label de data deve estar presente e correto")
    void testData() throws InvocationTargetException, InterruptedException {
        final Sobre[] janela = new Sobre[1];

        EventQueue.invokeAndWait(() -> janela[0] = new Sobre());
        Sobre sobre = janela[0];

        JLabel lblData = (JLabel) sobre.getContentPane().getComponent(3);

        assertEquals(Constantes.UIConstants.SOBRE_DATA, lblData.getText());
        assertTrue(lblData.getFont().isBold());
    }
}
