package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelExporter {

    // Private constructor to prevent instantiation
    private ExcelExporter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void exportTableToExcel(JTable table) throws IOException {
        Path filePath = selectExportFile();
        if (filePath != null) {
            createExcelFile(table, filePath);
        }
    }

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

    private static void createExcelFile(JTable table, Path filePath) throws IOException {
        prepareFile(filePath);

        try (Workbook book = new HSSFWorkbook(); FileOutputStream file = new FileOutputStream(filePath.toFile())) {

            Sheet sheet = book.createSheet("Dados");
            sheet.setDisplayGridlines(true);

            writeTableToSheet(table, sheet);
            book.write(file);
        }
    }

    private static void prepareFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        Files.createFile(filePath);
    }

    private static void writeTableToSheet(JTable table, Sheet sheet) {
        createHeaderRow(table, sheet);
        createDataRows(table, sheet);
    }

    private static void createHeaderRow(JTable table, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int j = 0; j < table.getColumnCount(); j++) {
            Cell cell = headerRow.createCell(j);
            cell.setCellValue(table.getColumnName(j));
        }
    }

    private static void createDataRows(JTable table, Sheet sheet) {
        for (int linha = 0; linha < table.getRowCount(); linha++) {
            Row row = sheet.createRow(linha + 1);
            populateRow(table, row, linha);
        }
    }

    private static void populateRow(JTable table, Row row, int rowIndex) {
        for (int coluna = 0; coluna < table.getColumnCount(); coluna++) {
            Cell cell = row.createCell(coluna);
            Object value = table.getValueAt(rowIndex, coluna);
            setCellValue(cell, value);
        }
    }

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
