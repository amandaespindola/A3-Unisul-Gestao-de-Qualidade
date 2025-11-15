package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.*;

import javax.swing.JTable;
import java.io.IOException;
import java.nio.file.*;

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

}
