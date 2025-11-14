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
 * Classe utilitária para exportar dados de um {@link JTable}
 * para um arquivo Excel no formato <code>.xls</code>, utilizando Apache POI.
 * 
 * <p>Esta classe não pode ser instanciada.</p>
 */
public class ExcelExporter {

	/**
        * Construtor privado para evitar instanciação.
        */
	private ExcelExporter() {
		throw new UnsupportedOperationException("Utility class");
	}

        /**
        * Abre uma janela de diálogo para o usuário selecionar onde salvar o arquivo
        * e exporta os dados da tabela para um arquivo Excel <code>.xls</code>.
        *
        * @param table JTable contendo os dados a serem exportados.
        * @throws IOException caso ocorra erro ao criar ou escrever o arquivo.
        */
	public static void exportTableToExcel(JTable table) throws IOException {
		Path filePath = selectExportFile();
		if (filePath != null) {
			createExcelFile(table, filePath);
		}
	}

        /**
        * Exibe um {@link JFileChooser} para escolher o local de salvamento
        * e garante que a extensão <code>.xls</code> seja aplicada.
        *
        * @return Caminho do arquivo selecionado ou {@code null} caso o usuário cancele.
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
        * Cria o arquivo Excel, gera a planilha e escreve os dados do JTable.
        *
        * @param table    Tabela cujos dados serão exportados.
        * @param filePath Caminho completo do arquivo Excel a ser gerado.
        * @throws IOException caso ocorra falha na criação ou gravação do arquivo.
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
        * Remove arquivo existente (se houver) e cria um arquivo novo no caminho especificado.
        *
        * @param filePath Caminho do arquivo.
        */
	private static void prepareFile(Path filePath) throws IOException {
		if (Files.exists(filePath)) {
			Files.delete(filePath);
		}
		Files.createFile(filePath);
	}

        /**
        * Escreve os dados do JTable na planilha, incluindo cabeçalhos e linhas.
        *
        * @param table JTable de origem.
        * @param sheet Planilha alvo.
        */
	private static void writeTableToSheet(JTable table, Sheet sheet) {
		createHeaderRow(table, sheet);
		createDataRows(table, sheet);
	}

        /**
        * Cria a linha de cabeçalho da planilha com os nomes das colunas do JTable.
        */
	private static void createHeaderRow(JTable table, Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		for (int j = 0; j < table.getColumnCount(); j++) {
			Cell cell = headerRow.createCell(j);
			cell.setCellValue(table.getColumnName(j));
		}
	}

        /**
        * Cria todas as linhas de dados da planilha com base no JTable.
        */
	private static void createDataRows(JTable table, Sheet sheet) {
		for (int linha = 0; linha < table.getRowCount(); linha++) {
			Row row = sheet.createRow(linha + 1);
			populateRow(table, row, linha);
		}
	}

        /**
        * Preenche uma linha da planilha com os valores de uma linha do JTable.
        */
	private static void populateRow(JTable table, Row row, int rowIndex) {
		for (int coluna = 0; coluna < table.getColumnCount(); coluna++) {
			Cell cell = row.createCell(coluna);
			Object value = table.getValueAt(rowIndex, coluna);
			setCellValue(cell, value);
		}
	}

        /**
        * Define o valor de uma célula, convertendo para o tipo adequado no Excel.
        *
        * @param cell  Célula alvo.
        * @param value Valor obtido do JTable.
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
