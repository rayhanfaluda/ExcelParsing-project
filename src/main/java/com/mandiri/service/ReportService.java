package com.mandiri.service;

import com.mandiri.entity.Report;
import com.mandiri.helper.ExcelHelper;
import com.mandiri.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public void save(MultipartFile file) {
        try {
            List<Report> reports = ExcelHelper.excelToReports(file.getInputStream());
            reportRepository.saveAll(reports);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<Report> tutorials = reportRepository.findAll();

        ByteArrayInputStream in = ExcelHelper.reportsToExcel(tutorials);
        return in;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

}
