package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import dao.AlunoDAO;
import model.Aluno;
import utils.ExcelExporter;

class GerenciaAlunosTest {

    private <T> T buscar(Component comp, Class<T> tipo) {
        if (tipo.isInstance(comp)) {
            return tipo.cast(comp);
        }
        if (comp instanceof Container c) {
            for (Component f : c.getComponents()) {
                T achado = buscar(f, tipo);
                if (achado != null) {
                    return achado;
                }
            }
        }
        return null;
    }

    @Test
    @DisplayName("Janela deve inicializar sem crash")
    void testInicializacao() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();

        assertEquals("Gerência de Alunos", tela.getTitle());
        assertNotNull(tela.getContentPane());
    }

    @Test
    @DisplayName("Painel superior deve possuir botões")
    void testPainelSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JButton botao = buscar(tela.getContentPane(), JButton.class);

        assertNotNull(botao, "Painel superior deve ter ao menos 1 botão");
    }

    @Test
    @DisplayName("Menu superior deve existir")
    void testMenuSuperior() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JMenuBar menu = tela.getJMenuBar();

        assertNotNull(menu);
        assertEquals(1, menu.getMenuCount());
    }

    @Test
    @DisplayName("Tabela deve existir e ter colunas corretas")
    void testTabela() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JTable tabela = buscar(tela.getContentPane(), JTable.class);

        assertNotNull(tabela);

        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        assertEquals(5, model.getColumnCount());
        assertEquals("ID", model.getColumnName(0));
        assertEquals("Nome", model.getColumnName(1));
    }

    @Test
    @DisplayName("ScrollPane deve existir")
    void testScrollPane() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JScrollPane scroll = buscar(tela.getContentPane(), JScrollPane.class);

        assertNotNull(scroll);
        assertNotNull(buscar(scroll, JTable.class));
    }

    @Test
    @DisplayName("carregarTabela() deve rodar sem explodir")
    void testCarregarTabela() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();

        Method m = GerenciaAlunos.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);

        assertDoesNotThrow(() -> m.invoke(tela));

        JTable tabela = buscar(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);
        assertTrue(tabela.getRowCount() >= 0);
    }

    @Test
    @DisplayName("carregarTabela() deve popular tabela com mock")
    void testCarregarTabelaMock() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();

        // DAO mockado
        AlunoDAO daoMock = Mockito.mock(AlunoDAO.class);

        Aluno a1 = Mockito.mock(Aluno.class);
        Mockito.when(a1.getId()).thenReturn(1);
        Mockito.when(a1.getNome()).thenReturn("Ana");
        Mockito.when(a1.getIdade()).thenReturn(20);
        Mockito.when(a1.getCurso()).thenReturn("ADS");
        Mockito.when(a1.getFase()).thenReturn(3);

        Aluno a2 = Mockito.mock(Aluno.class);
        Mockito.when(a2.getId()).thenReturn(2);
        Mockito.when(a2.getNome()).thenReturn("João");
        Mockito.when(a2.getIdade()).thenReturn(22);
        Mockito.when(a2.getCurso()).thenReturn("SI");
        Mockito.when(a2.getFase()).thenReturn(5);

        Mockito.when(daoMock.getMinhaLista()).thenReturn(Arrays.asList(a1, a2));

        // injeta mock
        Field f = GerenciaAlunos.class.getDeclaredField("alunoDAO");
        f.setAccessible(true);
        f.set(tela, daoMock);

        Method m = GerenciaAlunos.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);
        m.invoke(tela);

        JTable tabela = buscar(tela.getContentPane(), JTable.class);
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();

        assertEquals(2, model.getRowCount());
        assertEquals("Ana", model.getValueAt(0, 1));
    }

    @Test
    @DisplayName("exportarExcel sucesso - mock seguro")
    void testExportarExcelSucesso() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JTable tabela = buscar(tela.getContentPane(), JTable.class);

        try (MockedStatic<ExcelExporter> excel = Mockito.mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> msg = Mockito.mockStatic(JOptionPane.class)) {

            excel.when(() -> ExcelExporter.exportTableToExcel(Mockito.any())).thenAnswer(inv -> null);

            Method m = GerenciaAlunos.class.getDeclaredMethod("exportarExcel");
            m.setAccessible(true);
            m.invoke(tela);

            excel.verify(() -> ExcelExporter.exportTableToExcel(tabela));
            msg.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("sucesso")
            ));
        }
    }

    @Test
    @DisplayName("exportarExcel erro - mock IOException")
    void testExportarExcelErro() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();
        JTable tabela = buscar(tela.getContentPane(), JTable.class);

        try (MockedStatic<ExcelExporter> excel = Mockito.mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> msg = Mockito.mockStatic(JOptionPane.class)) {

            excel.when(() -> ExcelExporter.exportTableToExcel(Mockito.any()))
                    .thenThrow(new IOException("falha simulada"));

            Method m = GerenciaAlunos.class.getDeclaredMethod("exportarExcel");
            m.setAccessible(true);
            m.invoke(tela);

            msg.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("Erro ao exportar")
            ));
        }
    }

    @Test
    @DisplayName("deletar com linha selecionada e confirmação Sim deve chamar dao.delete")
    void testDeletarComSucesso() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();

        JTable tabela = buscar(tela.getContentPane(), JTable.class);
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.addRow(new Object[]{1, "Aluno", 20, "ADS", "1ª"});
        tabela.setRowSelectionInterval(0, 0);

        AlunoDAO daoMock = Mockito.mock(AlunoDAO.class);
        Mockito.when(daoMock.delete(1)).thenReturn(true);
        Mockito.when(daoMock.getMinhaLista()).thenReturn(Collections.emptyList());

        Field f = GerenciaAlunos.class.getDeclaredField("alunoDAO");
        f.setAccessible(true);
        f.set(tela, daoMock);

        try (MockedStatic<JOptionPane> msg = Mockito.mockStatic(JOptionPane.class)) {
            msg.when(() -> JOptionPane.showOptionDialog(
                    Mockito.any(), Mockito.anyString(), Mockito.anyString(),
                    Mockito.anyInt(), Mockito.anyInt(),
                    Mockito.isNull(), Mockito.<Object[]>any(), Mockito.any()
            )).thenReturn(0);

            msg.when(() -> JOptionPane.showMessageDialog(Mockito.any(), Mockito.any()))
                    .thenAnswer(inv -> null);

            Method m = GerenciaAlunos.class.getDeclaredMethod("deletar");
            m.setAccessible(true);
            m.invoke(tela);

            Mockito.verify(daoMock).delete(1);
        }
    }

    @Test
    @DisplayName("deletar sem seleção deve mostrar aviso")
    void testDeletarSemSelecao() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        GerenciaAlunos tela = new GerenciaAlunos();

        AlunoDAO daoMock = Mockito.mock(AlunoDAO.class);
        Mockito.when(daoMock.getMinhaLista()).thenReturn(Collections.emptyList());

        Field f = GerenciaAlunos.class.getDeclaredField("alunoDAO");
        f.setAccessible(true);
        f.set(tela, daoMock);

        try (MockedStatic<JOptionPane> msg = Mockito.mockStatic(JOptionPane.class)) {

            msg.when(() -> JOptionPane.showMessageDialog(Mockito.any(), Mockito.anyString()))
                    .thenAnswer(inv -> null);

            Method m = GerenciaAlunos.class.getDeclaredMethod("deletar");
            m.setAccessible(true);
            m.invoke(tela);

            msg.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("Selecione")
            ));
        }
    }

    @Test
    @DisplayName("abrirCadastro deve instanciar CadastroAluno")
    void testAbrirCadastro() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        try (MockedConstruction<CadastroAluno> cons
                = Mockito.mockConstruction(CadastroAluno.class)) {

            GerenciaAlunos tela = new GerenciaAlunos();

            Method m = GerenciaAlunos.class.getDeclaredMethod("abrirCadastro");
            m.setAccessible(true);
            m.invoke(tela);

            assertEquals(1, cons.constructed().size());
        }
    }

    @Test
    @DisplayName("abrirProfessores deve instanciar GerenciaProfessores")
    void testAbrirProfessores() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado no CI");

        try (MockedConstruction<GerenciaProfessores> cons
                = Mockito.mockConstruction(GerenciaProfessores.class)) {

            GerenciaAlunos tela = new GerenciaAlunos();

            Method m = GerenciaAlunos.class.getDeclaredMethod("abrirProfessores");
            m.setAccessible(true);
            m.invoke(tela);

            assertEquals(1, cons.constructed().size());
        }
    }
}
