package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JTable;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ExcelExporterTest {

    private JTable tabelaExemplo;
    private Path arquivoTemp;

    @BeforeEach
    void setup() throws IOException {
        tabelaExemplo = new JTable(
                new Object[][]{
                    {1, "Amanda", 24},
                    {2, "Matheus", 30}
                },
                new String[]{"ID", "Nome", "Idade"}
        );

        arquivoTemp = Files.createTempFile("teste_excel_exporter", ".xls");
    }

    @AfterEach
    void cleanup() throws IOException {
        if (Files.exists(arquivoTemp)) {
            Files.delete(arquivoTemp);
        }
    }

    @Test
    void testCreateExcelFileNaoLancaExcecao() {
        assertDoesNotThrow(() -> {
            ExcelExporter.exportTableToExcelMocked(tabelaExemplo, arquivoTemp);
        });
    }

    @Test
    void testArquivoExcelCriadoComSucesso() throws IOException {
        ExcelExporter.exportTableToExcelMocked(tabelaExemplo, arquivoTemp);

        assertTrue(Files.exists(arquivoTemp), "O arquivo Excel deveria ter sido criado.");
        assertTrue(Files.size(arquivoTemp) > 0, "O arquivo Excel deve conter dados.");
    }

    @Test
    void testConteudoExcelCorreto() throws Exception {
        ExcelExporter.exportTableToExcelMocked(tabelaExemplo, arquivoTemp);

        try (Workbook workbook = new HSSFWorkbook(Files.newInputStream(arquivoTemp))) {

            Sheet sheet = workbook.getSheetAt(0);

            // Header
            Row header = sheet.getRow(0);
            assertEquals("ID", header.getCell(0).getStringCellValue());
            assertEquals("Nome", header.getCell(1).getStringCellValue());
            assertEquals("Idade", header.getCell(2).getStringCellValue());

            // Linha 1
            Row row1 = sheet.getRow(1);
            assertEquals(1, (int) row1.getCell(0).getNumericCellValue());
            assertEquals("Amanda", row1.getCell(1).getStringCellValue());
            assertEquals(24, (int) row1.getCell(2).getNumericCellValue());

            // Linha 2
            Row row2 = sheet.getRow(2);
            assertEquals(2, (int) row2.getCell(0).getNumericCellValue());
            assertEquals("Matheus", row2.getCell(1).getStringCellValue());
            assertEquals(30, (int) row2.getCell(2).getNumericCellValue());
        }
    }

    @Test
    void testErroAoCriarArquivoInvalido() {
        Path invalidPath = Paths.get("/caminho/invalido/arquivo.xls");

        Throwable thrown = assertThrows(IOException.class,
                () -> ExcelExporter.exportTableToExcelMocked(tabelaExemplo, invalidPath));

        assertNotNull(thrown);
    }

    @Test
    @DisplayName("setCellValue deve escrever corretamente valores Float no arquivo Excel")
    void testSetCellValueFloat() throws Exception {
        JTable tabela = new JTable(
                new Object[][]{
                    {10.5f}
                },
                new String[]{"Valor"}
        );

        Path arquivoTemp = Files.createTempFile("excel_float_test", ".xls");

        // usa o método mockado para evitar JFileChooser
        ExcelExporter.exportTableToExcelMocked(tabela, arquivoTemp);

        try (Workbook wb = new HSSFWorkbook(Files.newInputStream(arquivoTemp))) {
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(1);
            Cell cell = row.getCell(0);

            assertEquals(10.5f, (float) cell.getNumericCellValue(), 0.0001);
        }

        Files.deleteIfExists(arquivoTemp);
    }

    @Test
    @DisplayName("Construtor privado deve lançar UnsupportedOperationException quando instanciado via reflexão")
    void testConstructorPrivate() throws Exception {
        Constructor<ExcelExporter> cons = ExcelExporter.class.getDeclaredConstructor();
        cons.setAccessible(true);

        InvocationTargetException excecao
                = assertThrows(InvocationTargetException.class, cons::newInstance);

        assertTrue(excecao.getTargetException() instanceof UnsupportedOperationException);
    }

}
