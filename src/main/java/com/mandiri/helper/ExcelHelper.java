package com.mandiri.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import com.mandiri.entity.Report;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


public class ExcelHelper {
    public static String[] TYPE = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"};
    static String[] HEADER = { "id", "fullName", "birthDate", "birthPlace", "address", "phoneNumber", "gender" };
    static String SHEET = "Report";
  
    public static boolean hasExcelFormat(MultipartFile file) {
        return Arrays.asList(TYPE).contains(file.getContentType());
    }

    public static ByteArrayInputStream reportsToExcel(List<Report> reports) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            // Temporary file compression, lower storage used, but longer response time
            /* workbook.setCompressTempFiles(true); */

            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADER.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER[col]);
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
            workbook.close();
            workbook.dispose();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Report> excelToReports(InputStream is) {
        try {
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(50)     // number of rows to keep in memory (defaults to 10)
                    .bufferSize(2048)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is);            // InputStream or File for XLSX file (required)
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
                    report.setId(UUID.randomUUID().toString());
                    switch (cellIdx) {
                        case 0:
                            report.setFullName(currentCell.getStringCellValue());
                            break;

                        case 1:
                            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(currentCell.getStringCellValue());
                            report.setBirthDate(date);
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
