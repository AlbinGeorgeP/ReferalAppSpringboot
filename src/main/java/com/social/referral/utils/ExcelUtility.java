package com.social.referral.utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelUtility {

    private XSSFWorkbook workbook =new XSSFWorkbook();
    private XSSFSheet sheet=workbook.createSheet();

    public XSSFWorkbook writeHeader(List<String> headers,String sheetName) {
        workbook.removeSheetAt(0);
        sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        int columnCount=0;
        for (String header:headers)
        {
            createCell(row,columnCount++,header,style);
        }
        return workbook;
    }

    public XSSFWorkbook write(List<String> values,XSSFWorkbook workbook,Integer rowCount) {
        this.workbook=workbook;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        style.setFont(font);
        for (String value : values) {
            createCell(row, columnCount++, value, style);
        }
        return workbook;
    }

    private void createCell(Row row, int columnCount, String value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }


}
