package com.mandiri.service;

import com.mandiri.entity.Report;
import com.mandiri.helper.ExcelHelper;
import com.mandiri.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            int batchSize = 1000;
            int totalObjects = reports.size();
            for (int i = 0; i < totalObjects; i = i + batchSize) {
                if( i+ batchSize > totalObjects){
                    List<Report> reports1 = reports.subList(i, totalObjects-1);
                    reportRepository.saveAll(reports1);
                    break;
                }
                List<Report> reports1 = reports.subList(i, i + batchSize);
                reportRepository.saveAll(reports1);
                System.out.println("Success insert from "+i+" to "+(i+batchSize));
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<Report> reports = reportRepository.findAll();

        ByteArrayInputStream in = ExcelHelper.reportsToExcel(reports);
        return in;
    }

    public List<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable).getContent();
    }

}
