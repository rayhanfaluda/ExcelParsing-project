package com.mandiri.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mandiri.entity.Report;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "fullName", "birthDate", "birthPlace", "address", "phoneNumber", "gender" };
    static String SHEET = "data";
    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
    public static List<Report> excelToReport(InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Report> reports = new ArrayList<Report>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Report report = new Report();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    System.out.println(currentCell.getStringCellValue());
                    report.setId(String.valueOf(UUID.randomUUID()));
                    switch (cellIdx) {
                        case 1:
                            report.setFullName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            Date date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(currentCell.getStringCellValue());
                            report.setBirthDate(date);
                            break;
                        case 3:
                            report.setBirthPlace(currentCell.getStringCellValue());
                            break;
                        case 4:
                            report.setAddress(currentCell.getStringCellValue());
                            break;
                        case 5:
                            report.setPhoneNumber(currentCell.getStringCellValue());
                            break;
                        case 6:
                            report.setGender(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                System.out.println(report);
                reports.add(report);
            }
            workbook.close();
            return reports;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
