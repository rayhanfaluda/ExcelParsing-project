package com.mandiri.service;

import com.mandiri.entity.Report;
import com.mandiri.helper.ExcelHelper;
import com.mandiri.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public void save(MultipartFile file) throws IOException {
        List<Report> reports = ExcelHelper.excelToReport(file.getInputStream());
        reportRepository.saveAll(reports);
    }

    public List<Report> getAllTutorials() {
        return reportRepository.findAll();
    }

}
