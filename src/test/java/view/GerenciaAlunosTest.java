package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mockStatic;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.AlunoDAO;
import model.Aluno;
import utils.ExcelExporter;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

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
    @DisplayName("Inicialização deve ser segura e sem exceções")
    void testInicializacao() {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaAlunos tela = new GerenciaAlunos();
        assertEquals("Gerência de Alunos", tela.getTitle());
        assertNotNull(tela.getContentPane());
    }

    @Test
    @DisplayName("Painel superior deve conter botões gerenciais")
    void testPainelSuperiorEBotoes() {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaAlunos tela = new GerenciaAlunos();
        JButton botao = buscarComponente(tela.getContentPane(), JButton.class);

        assertNotNull(botao);
    }

    @Test
    @DisplayName("Menu superior deve existir")
    void testMenuSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaAlunos tela = new GerenciaAlunos();
        JMenuBar menu = tela.getJMenuBar();

        assertNotNull(menu);
        assertEquals(1, menu.getMenuCount());
    }

    @Test
    @DisplayName("Tabela deve existir e ter as colunas esperadas")
    void testTabelaExiste() {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaAlunos tela = new GerenciaAlunos();
        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);

        assertNotNull(tabela);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        assertEquals(5, modelo.getColumnCount());
        assertEquals("ID", modelo.getColumnName(0));
        assertEquals("Nome", modelo.getColumnName(1));
    }

    @Test
    @DisplayName("carregarTabela() deve popular a tabela com dados simulados (mock)")
    void testCarregarTabelaComMock() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        // mock da DAO
        AlunoDAO daoMock = mock(AlunoDAO.class);
        when(daoMock.getMinhaLista()).thenReturn(List.of(
                new Aluno("ADS", 4, 1, "Amanda", 22),
                new Aluno("Direito", 2, 2, "Laura", 20)
        ));

        // injeta mock
        GerenciaAlunos tela = new GerenciaAlunos();
        Field f = GerenciaAlunos.class.getDeclaredField("alunoDAO");
        f.setAccessible(true);
        f.set(tela, daoMock);

        // executa carregarTabela()
        Method m = GerenciaAlunos.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(tela));

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertEquals(2, tabela.getRowCount());
    }

    @Test
    @DisplayName("Mouse click atualiza linhaSelecionada com segurança")
    void testMouseClick() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaAlunos tela = new GerenciaAlunos();
        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{1, "Aluno Teste", 20, "Curso X", "1ª"});

        tabela.setRowSelectionInterval(0, 0);

        Method metodo = GerenciaAlunos.class.getDeclaredMethod(
                "jTableMouseClick",
                java.awt.event.MouseEvent.class
        );
        metodo.setAccessible(true);

        metodo.invoke(tela, new java.awt.event.MouseEvent(
                tabela, 0, 0, 0, 5, 5, 1, false
        ));

        Field campo = GerenciaAlunos.class.getDeclaredField("linhaSelecionada");
        campo.setAccessible(true);

        assertEquals(0, campo.getInt(tela));
    }

    // Testes de botões (avertura de telas / exportação)
    @Test
    @DisplayName("exportarExcel deve chamar método corretamente sem abrir JFileChooser nem JOptionPane")
    void testExportarExcelMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI.");

        GerenciaAlunos tela = new GerenciaAlunos();

        // Mock estático do ExcelExporter e do JOptionPane
        try (MockedStatic<ExcelExporter> mockExcel = mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> mockDialogs = mockStatic(JOptionPane.class)) {

            mockExcel.when(() -> ExcelExporter.exportTableToExcel(any()))
                    .then(invocation -> null);

            mockDialogs.when(() -> JOptionPane.showMessageDialog(any(), anyString()))
                    .then(invocation -> null);

            Method metodo = GerenciaAlunos.class.getDeclaredMethod("exportarExcel");
            metodo.setAccessible(true);

            assertDoesNotThrow(() -> metodo.invoke(tela));

            mockExcel.verify(() -> ExcelExporter.exportTableToExcel(any()), times(1));
            mockDialogs.verify(() -> JOptionPane.showMessageDialog(any(), anyString()), times(1));
        }
    }

    @Test
    @DisplayName("abrirCadastro não deve lançar exceções (mockado)")
    void testAbrirCadastroMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        try (MockedStatic<JOptionPane> mockDialogs = mockStatic(JOptionPane.class)) {

            GerenciaAlunos tela = new GerenciaAlunos();

            Method metodo = GerenciaAlunos.class.getDeclaredMethod("abrirCadastro");
            metodo.setAccessible(true);

            assertDoesNotThrow(() -> metodo.invoke(tela));
        }
    }
}
