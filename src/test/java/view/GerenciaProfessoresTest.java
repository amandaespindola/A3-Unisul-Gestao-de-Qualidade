package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import java.io.IOException; 
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import dao.ProfessorDAO;
import model.Professor;
import model.ProfessorDTO;
import utils.ExcelExporter;

class GerenciaProfessoresTest {

    private <T> T buscar(Component c, Class<T> tipo) {
        if (tipo.isInstance(c)) {
            return tipo.cast(c);
        }
        if (c instanceof Container cont) {
            for (Component f : cont.getComponents()) {
                T achado = buscar(f, tipo);
                if (achado != null) {
                    return achado;
                }
            }
        }
        return null;
    }

    @Test
    void testInicializacao() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        GerenciaProfessores tela = new GerenciaProfessores();
        assertEquals("Gerência de Professores", tela.getTitle());
    }

    @Test
    void testPainelSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        GerenciaProfessores tela = new GerenciaProfessores();

        JButton btn = buscar(tela.getContentPane(), JButton.class);
        assertNotNull(btn);
    }

    @Test
    void testMenu() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        GerenciaProfessores tela = new GerenciaProfessores();

        JMenuBar bar = tela.getJMenuBar();
        assertNotNull(bar);
    }

    @Test
    void testTabela() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscar(tela.getContentPane(), JTable.class);

        assertNotNull(tabela);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        assertEquals(8, modelo.getColumnCount());
    }

    @Test
    void testScroll() {
        assumeFalse(GraphicsEnvironment.isHeadless());
        GerenciaProfessores tela = new GerenciaProfessores();

        JScrollPane scroll = buscar(tela.getContentPane(), JScrollPane.class);
        assertNotNull(scroll);
    }

    @Test
    void testCarregarTabelaComMock() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        ProfessorDAO daoMock = mock(ProfessorDAO.class);

        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(1);
        dto.setNome("Teste");
        dto.setIdade(40);
        dto.setCampus("C1");
        dto.setCpf("123");
        dto.setContato("999");
        dto.setTitulo("Mestre");
        dto.setSalario(1000);

        Professor p = new Professor(dto);

        when(daoMock.getMinhaLista()).thenReturn(List.of(p));

        GerenciaProfessores tela = new GerenciaProfessores();

        Field f = GerenciaProfessores.class.getDeclaredField("professorDAO");
        f.setAccessible(true);
        f.set(tela, daoMock);

        Method m = GerenciaProfessores.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);
        m.invoke(tela);

        JTable tabela = buscar(tela.getContentPane(), JTable.class);
        assertEquals(1, tabela.getRowCount());
    }

    @Test
    void testMouseClick() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaProfessores tela = new GerenciaProfessores();
        JTable tabela = buscar(tela.getContentPane(), JTable.class);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{"1", "A", "20", "C", "1", "2", "T", "R$ 10"});

        tabela.setRowSelectionInterval(0, 0);

        Method m = GerenciaProfessores.class.getDeclaredMethod(
                "mouseClickTabela", java.awt.event.MouseEvent.class
        );
        m.setAccessible(true);
        m.invoke(tela,
                new java.awt.event.MouseEvent(tabela, 0, 0, 0, 5, 5, 1, false));

        Field f = GerenciaProfessores.class.getDeclaredField("linhaSelecionada");
        f.setAccessible(true);

        assertEquals(0, f.getInt(tela));
    }

    @Test
    void testExportarExcelMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless());

        GerenciaProfessores tela = new GerenciaProfessores();

        try (MockedStatic<ExcelExporter> excel = mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> jop = mockStatic(JOptionPane.class)) {

            excel.when(() -> ExcelExporter.exportTableToExcel(any(JTable.class)))
                    .thenThrow(new IOException("erro"));

            jop.when(() -> JOptionPane.showMessageDialog(any(), anyString()))
                    .thenAnswer(inv -> null);

            Method m = GerenciaProfessores.class.getDeclaredMethod("exportarExcel");
            m.setAccessible(true);

            assertDoesNotThrow(() -> m.invoke(tela));
        }
    }
}
