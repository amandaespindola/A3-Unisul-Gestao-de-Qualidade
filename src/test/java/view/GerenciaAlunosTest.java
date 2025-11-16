package view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GerenciaAlunosTest {

    // busca recursiva de componentes
    private <T> T buscarComponente(Component comp, Class<T> tipo) {
        if (tipo.isInstance(comp)) {
            return tipo.cast(comp);
        }

        if (comp instanceof Container cont) {
            for (Component filho : cont.getComponents()) {
                T achado = buscarComponente(filho, tipo);
                if (achado != null) {
                    return achado;
                }
            }
        }
        return null;
    }

    @Test
    @DisplayName("Janela deve inicializar sem exceções")
    void testInicializacao() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI sem gráfico.");

        GerenciaAlunos tela = new GerenciaAlunos();

        assertEquals("Gerência de Alunos", tela.getTitle());
        assertNotNull(tela.getContentPane());
    }

    @Test
    @DisplayName("Painel superior deve existir e conter ao menos 1 botão")
    void testPainelSuperiorEBotoes() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();

        JButton botao = buscarComponente(tela.getContentPane(), JButton.class);

        assertNotNull(botao, "Painel superior deve conter ao menos um botão gerencial");
    }

    @Test
    @DisplayName("Menu superior deve existir")
    void testMenuSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();
        JMenuBar menu = tela.getJMenuBar();

        assertNotNull(menu, "Menu deve existir");
        assertEquals(1, menu.getMenuCount());
    }

    @Test
    @DisplayName("Tabela deve existir e ter colunas corretas")
    void testTabelaExiste() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela, "Tabela não encontrada");

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        assertEquals(5, modelo.getColumnCount());
        assertEquals("ID", modelo.getColumnName(0));
        assertEquals("Nome", modelo.getColumnName(1));
    }

    @Test
    @DisplayName("carregarTabela() deve executar sem lançar exceções")
    void testCarregarTabelaNaoExplode() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();

        Method m = GerenciaAlunos.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                m.invoke(tela);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        assertTrue(tabela.getRowCount() >= 0);
    }

    @Test
    @DisplayName("Mouse click deve atualizar linhaSelecionada com segurança")
    void testMouseClick() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{1, "Aluno Teste", 20, "Curso X", "1ª"});

        tabela.setRowSelectionInterval(0, 0);

        var metodoClick = GerenciaAlunos.class.getDeclaredMethod("jTableMouseClick", java.awt.event.MouseEvent.class);
        metodoClick.setAccessible(true);

        metodoClick.invoke(tela,
                new java.awt.event.MouseEvent(tabela, 0, 0, 0, 5, 5, 1, false));

        var campo = GerenciaAlunos.class.getDeclaredField("linhaSelecionada");
        campo.setAccessible(true);
        int selecionada = campo.getInt(tela);

        assertEquals(0, selecionada);
    }
}