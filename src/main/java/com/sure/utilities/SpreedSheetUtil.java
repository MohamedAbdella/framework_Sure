package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.sure.utilities.FilesDirectories.USER_DIR;

@Log4j2
public class SpreedSheetUtil {

    private static final String PATH = USER_DIR + "/Files/Excel/";
    private final File spreedSheet;
    private final ArrayList<String> columns;
    private Sheet currentSheet;

    public SpreedSheetUtil(String fileNameWithExtension) {
        this.spreedSheet = new File(PATH + fileNameWithExtension);
        this.columns = new ArrayList<>();
    }

    public SpreedSheetUtil(String dirPath, String fileNameWithExtension) {
        this.spreedSheet = new File(dirPath + fileNameWithExtension);
        this.columns = new ArrayList<>();
    }

    public void switchToSheet(String sheetName) {
        try (var workBooks = WorkbookFactory.create(spreedSheet)) {
            currentSheet = workBooks.getSheet(sheetName);
            currentSheet.getRow(0).forEach(cell -> {
                columns.add(cell.getStringCellValue());
            });
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * A method to get the data in certain cell.
     *
     * @param row    starts from 1 which is the header of the excel sheet.
     * @param column starts from 1
     * @return the cell value as string.
     */
    public String getCellData(int row, int column) {
        var dataRow = currentSheet.getRow(row - 1);
        return getCellDataAsString(dataRow.getCell(column - 1));
    }

    private String getCellDataAsString(Cell cell) {

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return null;
        }
    }

    public int getTotalNumberOfColumns() {
        return columns.size();
    }

    public int getTotalNumberOfRows() {
        return currentSheet.getLastRowNum() + 1;
    }

    public void setCellData(int row, int column, String value) throws IOException {

        var inputStreamer = new FileInputStream(spreedSheet.getAbsolutePath());
        var workbook = WorkbookFactory.create(inputStreamer);
        var sheet = workbook.getSheet(currentSheet.getSheetName());
        var newRow = sheet.createRow(row);
        newRow.createCell(column).setCellValue(value);
        inputStreamer.close();
        var outputStream = new FileOutputStream(spreedSheet.getAbsolutePath());
        workbook.write(outputStream);
        outputStream.close();
    }
}
