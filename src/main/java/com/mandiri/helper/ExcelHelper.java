package com.mandiri.helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mandiri.entity.Report;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "id", "fullName", "birthDate", "birthPlace", "address", "phoneNumber", "gender" };
    static String SHEET = "Report";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static ByteArrayInputStream reportsToExcel(List<Report> reports) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Report report : reports) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getFullName());
                row.createCell(2).setCellValue(
                        report.getBirthDate() != null ? report.getBirthDate().toString() : "");
                row.createCell(3).setCellValue(report.getBirthPlace());
                row.createCell(4).setCellValue(report.getAddress());
                row.createCell(5).setCellValue(report.getPhoneNumber());
                row.createCell(6).setCellValue(report.getGender());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Report> excelToReports(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

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

                    switch (cellIdx) {
                        case 0:
                            report.setFullName(currentCell.getStringCellValue());
                            break;

                        case 1:
                            report.setBirthDate(currentCell.getDateCellValue());
                            break;

                        case 2:
                            report.setBirthPlace(currentCell.getStringCellValue());
                            break;

                        case 3:
                            report.setAddress(currentCell.getStringCellValue());
                            break;

                        case 4:
                            report.setPhoneNumber(currentCell.getStringCellValue());
                            break;

                        case 5:
                            report.setGender(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }
                reports.add(report);
            }
            workbook.close();
            return reports;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
