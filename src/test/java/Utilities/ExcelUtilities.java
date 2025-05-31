package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtilities {
    public static Map<String, Object> excelData = new HashMap<>();
    public static Map<String, Integer> headerMap = new HashMap<>();


    public static void fetchData(String testCaseName, String sheetName) throws IOException {
        String excelFilePath = ConfigReader.getValue("excelFilePath");
        String searchName = testCaseName.toLowerCase();

        // Open Excel file
        FileInputStream file = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheet(sheetName);

        // Read header row and map column names
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            workbook.close();
            throw new IllegalArgumentException("Header row is missing or empty.");
        }

        int numColumns = headerRow.getPhysicalNumberOfCells();

        for (int i = 0; i < numColumns; i++) {
            String headerName = headerRow.getCell(i).getStringCellValue().trim();
            headerMap.put(headerName, i);
        }

        if (!headerMap.containsKey("TestCase")) {
            workbook.close();
            throw new IllegalArgumentException("Column 'TestCase' does not exist in the Excel header.");
        }

        // Iterate through data rows
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue; // Skip empty rows

            String nameValue = (String) getCellValue(row.getCell(headerMap.get("TestCase")));

            if (nameValue != null && nameValue.equalsIgnoreCase(searchName)) {
                excelData = new HashMap<>();
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    String headerName = entry.getKey();
                    int columnIndex = entry.getValue();
                    Object cellValue = getCellValue(row.getCell(columnIndex)); // Returns Object
                    excelData.put(headerName, cellValue);
                }
                System.out.println("Found data for name: " + nameValue);
                break;
            }
        }

        workbook.close();
        file.close();
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null; // Null represents empty cells
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue(); // Return String
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue(); // Return Date
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue % 1 == 0) {
                        return (int) numericValue; // Return Integer if whole number
                    } else {
                        return numericValue; // Return Double for fractional values
                    }
                }
            case BOOLEAN:
                return cell.getBooleanCellValue(); // Return Boolean
            case FORMULA:
                // Return the result of a formula cell
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                return evaluator.evaluate(cell).formatAsString();
            case BLANK:
                return "";
            default:
                return null;
        }
    }

}
