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
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

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

import dao.ProfessorDAO;
import model.Professor;
import utils.ExcelExporter;

class GerenciaProfessoresTest {

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
    @DisplayName("Menu superior deve existir e conter item Arquivo")
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
    @DisplayName("carregarTabela() deve executar sem lançar exceções (DAO real)")
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

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{1, "Professor Teste", 40, "Campus X",
            "123.456.789-00", "(48) 9 9999-9999", "Mestre", "R$ 1.000,00"});

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

    @Test
    @DisplayName("carregarTabela() deve popular a tabela usando ProfessorDAO mockado")
    void testCarregarTabelaComMock() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        // mock do DAO
        ProfessorDAO daoMock = Mockito.mock(ProfessorDAO.class);

        Professor p1 = Mockito.mock(Professor.class);
        Mockito.when(p1.getId()).thenReturn(1);
        Mockito.when(p1.getNome()).thenReturn("Amanda");
        Mockito.when(p1.getIdade()).thenReturn(30);
        Mockito.when(p1.getCampus()).thenReturn("Campus X");
        Mockito.when(p1.getCpf()).thenReturn("123.456.789-00");
        Mockito.when(p1.getContato()).thenReturn("(48) 9 9999-9999");
        Mockito.when(p1.getTitulo()).thenReturn("Mestre");
        Mockito.when(p1.getSalario()).thenReturn(2000.0);

        Professor p2 = Mockito.mock(Professor.class);
        Mockito.when(p2.getId()).thenReturn(2);
        Mockito.when(p2.getNome()).thenReturn("Laura");
        Mockito.when(p2.getIdade()).thenReturn(28);
        Mockito.when(p2.getCampus()).thenReturn("Campus Y");
        Mockito.when(p2.getCpf()).thenReturn("987.654.321-00");
        Mockito.when(p2.getContato()).thenReturn("(48) 9 8888-7777");
        Mockito.when(p2.getTitulo()).thenReturn("Doutora");
        Mockito.when(p2.getSalario()).thenReturn(3500.0);

        Mockito.when(daoMock.getMinhaLista()).thenReturn(Arrays.asList(p1, p2));

        // injeta o mock no campo professorDAO
        Field fDao = GerenciaProfessores.class.getDeclaredField("professorDAO");
        fDao.setAccessible(true);
        fDao.set(tela, daoMock);

        // chama carregarTabela novamente usando o mock
        Method m = GerenciaProfessores.class.getDeclaredMethod("carregarTabela");
        m.setAccessible(true);
        m.invoke(tela);

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        assertEquals(2, modelo.getRowCount(), "Tabela deve conter 2 linhas após carregarTabela com mock");

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        assertEquals(nf.format(2000.0), modelo.getValueAt(0, 7));
        assertEquals(nf.format(3500.0), modelo.getValueAt(1, 7));
    }

    @Test
    @DisplayName("exportarExcel sucesso - deve chamar ExcelExporter e mostrar mensagem (mockado)")
    void testExportarExcelSucessoMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        try (MockedStatic<ExcelExporter> excelMock = Mockito.mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> optionMock = Mockito.mockStatic(JOptionPane.class)) {

            // não faz nada de verdade, apenas registra a chamada
            excelMock.when(() -> ExcelExporter.exportTableToExcel(Mockito.any(JTable.class)))
                    .thenAnswer(inv -> null);

            Method m = GerenciaProfessores.class.getDeclaredMethod("exportarExcel");
            m.setAccessible(true);
            m.invoke(tela);

            excelMock.verify(() -> ExcelExporter.exportTableToExcel(Mockito.eq(tabela)));
            optionMock.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("sucesso")
            ));
        }
    }

    @Test
    @DisplayName("exportarExcel erro - IOException deve mostrar mensagem de erro (mockado)")
    void testExportarExcelFalhaMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        try (MockedStatic<ExcelExporter> excelMock = Mockito.mockStatic(ExcelExporter.class); MockedStatic<JOptionPane> optionMock = Mockito.mockStatic(JOptionPane.class)) {

            excelMock.when(() -> ExcelExporter.exportTableToExcel(Mockito.any(JTable.class)))
                    .thenThrow(new IOException("falha simulada"));

            Method m = GerenciaProfessores.class.getDeclaredMethod("exportarExcel");
            m.setAccessible(true);
            m.invoke(tela);

            optionMock.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("Erro ao exportar arquivo")
            ));
        }
    }

    @Test
    @DisplayName("deletar com linha selecionada e confirmação Sim deve chamar ProfessorDAO.delete")
    void testDeletarComConfirmacaoSucesso() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        JTable tabela = buscarComponente(tela.getContentPane(), JTable.class);
        assertNotNull(tabela);

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.addRow(new Object[]{1, "Prof Teste", 40, "Campus X", "123", "contato", "Mestre", "R$ 1.000,00"});
        tabela.setRowSelectionInterval(0, 0);

        ProfessorDAO daoMock = Mockito.mock(ProfessorDAO.class);
        Mockito.when(daoMock.delete(1)).thenReturn(true);
        Mockito.when(daoMock.getMinhaLista()).thenReturn(Collections.emptyList());

        Field fDao = GerenciaProfessores.class.getDeclaredField("professorDAO");
        fDao.setAccessible(true);
        fDao.set(tela, daoMock);

        try (MockedStatic<JOptionPane> optionMock = Mockito.mockStatic(JOptionPane.class)) {
            optionMock.when(() -> JOptionPane.showOptionDialog(
                    Mockito.any(),
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyInt(),
                    Mockito.anyInt(),
                    Mockito.isNull(),
                    Mockito.<Object[]>any(),
                    Mockito.any()
            )).thenReturn(0); // Sim

            optionMock.when(() -> JOptionPane.showMessageDialog(Mockito.any(), Mockito.anyString()))
                    .thenAnswer(inv -> null);

            Method m = GerenciaProfessores.class.getDeclaredMethod("deletar");
            m.setAccessible(true);
            m.invoke(tela);

            Mockito.verify(daoMock).delete(1);
        }
    }

    @Test
    @DisplayName("deletar sem linha selecionada deve exibir mensagem de validação (mockado)")
    void testDeletarSemSelecaoMostraMensagem() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        GerenciaProfessores tela = new GerenciaProfessores();

        ProfessorDAO daoMock = Mockito.mock(ProfessorDAO.class);
        Mockito.when(daoMock.getMinhaLista()).thenReturn(Collections.emptyList());

        Field fDao = GerenciaProfessores.class.getDeclaredField("professorDAO");
        fDao.setAccessible(true);
        fDao.set(tela, daoMock);

        try (MockedStatic<JOptionPane> optionMock = Mockito.mockStatic(JOptionPane.class)) {
            optionMock.when(() -> JOptionPane.showMessageDialog(Mockito.any(), Mockito.anyString()))
                    .thenAnswer(inv -> null);

            Method m = GerenciaProfessores.class.getDeclaredMethod("deletar");
            m.setAccessible(true);
            m.invoke(tela);

            optionMock.verify(() -> JOptionPane.showMessageDialog(
                    Mockito.eq(tela),
                    Mockito.contains("Selecione um cadastro")
            ));
        }
    }

    @Test
    @DisplayName("abrirCadastro deve instanciar CadastroProfessor (mockConstruction)")
    void testAbrirCadastroMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        try (MockedConstruction<CadastroProfessor> mockConstr
                = Mockito.mockConstruction(CadastroProfessor.class)) {

            GerenciaProfessores tela = new GerenciaProfessores();

            Method m = GerenciaProfessores.class.getDeclaredMethod("abrirCadastro");
            m.setAccessible(true);

            assertDoesNotThrow(() -> {
                try {
                    m.invoke(tela);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            assertEquals(1, mockConstr.constructed().size(),
                    "Deve ter sido construída uma instância de CadastroProfessor");
        }
    }

    @Test
    @DisplayName("abrirAlunos deve instanciar GerenciaAlunos (mockConstruction)")
    void testAbrirAlunosMockado() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Ignorado em ambiente sem suporte gráfico.");

        try (MockedConstruction<GerenciaAlunos> mockConstr
                = Mockito.mockConstruction(GerenciaAlunos.class)) {

            GerenciaProfessores tela = new GerenciaProfessores();

            Method m = GerenciaProfessores.class.getDeclaredMethod("abrirAlunos");
            m.setAccessible(true);

            assertDoesNotThrow(() -> {
                try {
                    m.invoke(tela);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            assertEquals(1, mockConstr.constructed().size(),
                    "Deve ter sido construída uma instância de GerenciaAlunos");
        }
    }
}
