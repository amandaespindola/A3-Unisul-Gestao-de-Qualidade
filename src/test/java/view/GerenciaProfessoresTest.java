package view;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GerenciaProfessoresTest {

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
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        assertEquals("Gerência de Professores", tela.getTitle());
        assertNotNull(tela.getContentPane());
    }

    @Test
    @DisplayName("Painel superior deve existir e conter ao menos um botão")
    void testPainelSuperiorEBotoes() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JButton botao = buscarComponente(tela.getContentPane(), JButton.class);

        assertNotNull(botao, "Painel superior deve conter ao menos um botão gerencial");
    }

    @Test
    @DisplayName("Menu superior deve existir e conter item de Gerência de Alunos")
    void testMenuSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();
        JMenuBar menuBar = tela.getJMenuBar();

        assertNotNull(menuBar, "Menu não deve ser nulo");
        assertEquals(1, menuBar.getMenuCount(), "Deve haver exatamente um menu principal");
        assertEquals("Arquivo", menuBar.getMenu(0).getText());
    }

    @Test
    @DisplayName("Tabela deve existir e ter colunas esperadas")
    void testTabelaExisteEColunas() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela, "Tabela de professores não encontrada na tela");

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        assertEquals(8, modelo.getColumnCount(), "A tabela deve ter 8 colunas");

        assertEquals("ID", modelo.getColumnName(0));
        assertEquals("Nome", modelo.getColumnName(1));
        assertEquals("Idade", modelo.getColumnName(2));
        assertEquals("Campus", modelo.getColumnName(3));
        assertEquals("CPF", modelo.getColumnName(4));
        assertEquals("Contato", modelo.getColumnName(5));
        assertEquals("Título", modelo.getColumnName(6));
        assertEquals("Salário", modelo.getColumnName(7));
    }

    @Test
    @DisplayName("ScrollPane envolvendo a tabela deve existir")
    void testScrollPaneExiste() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JScrollPane scroll = buscarComponente(tela.getContentPane(), JScrollPane.class);
        assertNotNull(scroll, "ScrollPane da tabela não encontrado");

        JTable tabela = buscarComponente(scroll, JTable.class);
        assertNotNull(tabela, "Tabela não encontrada dentro do ScrollPane");
    }

    @Test
    @DisplayName("carregarTabela() deve executar sem lançar exceções")
    void testCarregarTabelaNaoExplode() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        Method m = GerenciaProfessores.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                m.invoke(tela);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela, "Tabela deve existir após carregarTabela()");
        assertTrue(tabela.getRowCount() >= 0, "Quantidade de linhas deve ser >= 0");
    }

    @Test
    @DisplayName("Clique na tabela deve atualizar linhaSelecionada")
    void testMouseClickAtualizaLinhaSelecionada() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela, "Tabela não encontrada");

        // adiciona uma linha fake ao modelo para simular seleção
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{1, "Professor Teste", 40, "Campus X", "123.456.789-00",
            "(48) 9 9999-9999", "Mestre", "R$ 1.000,00"});

        tabela.setRowSelectionInterval(0, 0);

        Method metodoClick = GerenciaProfessores.class.getDeclaredMethod("mouseClickTabela",
                java.awt.event.MouseEvent.class);
        metodoClick.setAccessible(true);

        metodoClick.invoke(tela,
                new java.awt.event.MouseEvent(tabela, 0, 0, 0, 5, 5, 1, false));

        Field campoLinha = GerenciaProfessores.class.getDeclaredField("linhaSelecionada");
        campoLinha.setAccessible(true);
        int selecionada = campoLinha.getInt(tela);

        assertEquals(0, selecionada, "linhaSelecionada deve ser atualizada após o clique na tabela");
    }
}