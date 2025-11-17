package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe utilitária responsável por exportar o conteúdo de um {@link JTable}
 * para um arquivo Excel (.xls) utilizando a biblioteca Apache POI.
 * <p>
 * Todos os métodos são estáticos e a classe não deve ser instanciada.
 * </p>
 */
public class ExcelExporter {

	/**
     * Construtor privado para impedir instanciação.
     */
    private ExcelExporter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Método auxiliar usado para testes mockados. Recebe diretamente o caminho
     * onde o arquivo deve ser salvo, sem abrir o seletor de arquivos.
     *
     * @param table    tabela que será exportada.
     * @param filePath caminho completo do arquivo de saída.
     * @throws IOException se ocorrer erro ao criar ou escrever no arquivo.
     */
    static void exportTableToExcelMocked(JTable table, Path filePath) throws IOException {
        createExcelFile(table, filePath);
    }

    /**
     * Exporta uma tabela do Swing ({@link JTable}) para um arquivo Excel (.xls).
     * <p>
     * Um {@link JFileChooser} é exibido para que o usuário escolha onde salvar o
     * arquivo. Caso o usuário cancele a ação, nada é feito.
     * </p>
     *
     * @param table tabela cujos dados serão exportados.
     * @throws IOException se ocorrer falha ao criar ou escrever o arquivo.
     */
    public static void exportTableToExcel(JTable table) throws IOException {
        Path filePath = selectExportFile();
        if (filePath != null) {
            createExcelFile(table, filePath);
        }
    }

    /**
     * Abre um diálogo para permitir ao usuário selecionar o local e nome do arquivo
     * Excel a ser gerado.
     *
     * @return caminho do arquivo escolhido, ou {@code null} se o usuário cancelar.
     */
    private static Path selectExportFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos Excel", "xls");

        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Salvar arquivo");
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().toString();
            if (!path.endsWith(".xls")) {
                path = path.concat(".xls");
            }
            return Paths.get(path);
        }
        return null;
    }

    /**
     * Cria efetivamente o arquivo Excel com os dados da tabela.
     *
     * @param table    tabela de origem.
     * @param filePath caminho onde o arquivo será salvo.
     * @throws IOException caso ocorra erro ao criar ou escrever o arquivo.
     */
    private static void createExcelFile(JTable table, Path filePath) throws IOException {
        prepareFile(filePath);

        try (Workbook book = new HSSFWorkbook(); FileOutputStream file = new FileOutputStream(filePath.toFile())) {

            Sheet sheet = book.createSheet("Dados");
            sheet.setDisplayGridlines(true);

            writeTableToSheet(table, sheet);
            book.write(file);
        }
    }

    /**
     * Prepara o arquivo no sistema operacional:
     * <ul>
     *   <li>Se já existir, remove o arquivo antigo</li>
     *   <li>Cria um novo arquivo vazio</li>
     * </ul>
     *
     * @param filePath caminho do arquivo.
     * @throws IOException se ocorrer falha ao manipular o arquivo.
     */
    private static void prepareFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        Files.createFile(filePath);
    }

    /**
     * Escreve o conteúdo da tabela na planilha fornecida.
     *
     * @param table tabela de origem.
     * @param sheet planilha Excel onde as informações serão inseridas.
     */
    private static void writeTableToSheet(JTable table, Sheet sheet) {
        createHeaderRow(table, sheet);
        createDataRows(table, sheet);
    }

    /**
     * Cria a primeira linha da planilha, contendo os nomes das colunas da tabela.
     *
     * @param table tabela cujos cabeçalhos serão exportados.
     * @param sheet planilha de destino.
     */
    private static void createHeaderRow(JTable table, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int j = 0; j < table.getColumnCount(); j++) {
            Cell cell = headerRow.createCell(j);
            cell.setCellValue(table.getColumnName(j));
        }
    }

    /**
     * Escreve todas as linhas da tabela (dados) na planilha.
     *
     * @param table tabela de origem.
     * @param sheet planilha de destino.
     */
    private static void createDataRows(JTable table, Sheet sheet) {
        for (int linha = 0; linha < table.getRowCount(); linha++) {
            Row row = sheet.createRow(linha + 1);
            populateRow(table, row, linha);
        }
    }

    /**
     * Preenche uma linha da planilha com os valores de uma linha da JTable.
     *
     * @param table    tabela de origem.
     * @param row      linha da planilha a preencher.
     * @param rowIndex índice da linha na JTable.
     */
    private static void populateRow(JTable table, Row row, int rowIndex) {
        for (int coluna = 0; coluna < table.getColumnCount(); coluna++) {
            Cell cell = row.createCell(coluna);
            Object value = table.getValueAt(rowIndex, coluna);
            setCellValue(cell, value);
        }
    }

    /**
     * Define o valor de uma célula Excel de acordo com o tipo do objeto fornecido.
     *
     * @param cell  célula a ser preenchida.
     * @param value valor a inserir.
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value instanceof Double d) {
            cell.setCellValue(d);
        } else if (value instanceof Float f) {
            cell.setCellValue(f);
        } else if (value instanceof Integer i) {
            cell.setCellValue((i));
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
