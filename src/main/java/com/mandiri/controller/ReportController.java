package com.mandiri.controller;

import com.mandiri.constant.ResponseMessage;
import com.mandiri.entity.Report;
import com.mandiri.helper.ExcelHelper;
import com.mandiri.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> exportExcel(@RequestParam(name = "file") MultipartFile file) throws IOException {
        String message = "";
        Path tempDir = Files.createTempDirectory("");
        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                reportService.save(tempFile);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllTutorials() {
        try {
            List<Report> reports = reportService.getAllTutorials();
            if (reports.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
