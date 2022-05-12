package com.mandiri.service;

import com.mandiri.entity.City;
import com.mandiri.entity.ErrorEntity;
import com.mandiri.entity.Report;
import com.mandiri.helper.ExcelHelper;
import com.mandiri.repository.ErrorEntityRepository;
import com.mandiri.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ErrorEntityRepository errorEntityRepository;

    @Autowired
    CityService cityService;
    public void save(MultipartFile file) {
        try {
            List<City> cityList = cityService.getAllCity();
            List<String> cityNames =new ArrayList<>();
            for (City city : cityList) {
                cityNames.add(city.getCity());
            }
            ArrayList<Object> result = ExcelHelper.excelToReports(file.getInputStream(), cityNames);
            List<Report> reports =(List<Report>) result.get(0);
            List<ErrorEntity> error =(List<ErrorEntity>) result.get(1);
            saveBatchReport(reports);
            saveBatchError(error);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    private void saveBatchReport(List<Report> reports) {
        int batchSize = 1000;
        int totalObjects = reports.size();
        for (int i = 0; i < totalObjects; i = i + batchSize) {
            if( i+ batchSize > totalObjects){
                List<Report> reports1 = reports.subList(i, totalObjects);
                reportRepository.saveAll(reports1);
                break;
            }
            List<Report> reports1 = reports.subList(i, i + batchSize);
            reportRepository.saveAll(reports1);
            System.out.println("Success insert from "+i+" to "+(i+batchSize));
        }
    }

    private void saveBatchError(List<ErrorEntity> reports) {
        int batchSize = 1000;
        int totalObjects = reports.size();
        for (int i = 0; i < totalObjects; i = i + batchSize) {
            if( i+ batchSize > totalObjects){
                List<ErrorEntity> reports1 = reports.subList(i, totalObjects-1);
                errorEntityRepository.saveAll(reports1);
                break;
            }
            List<ErrorEntity> reports1 = reports.subList(i, i + batchSize);
            errorEntityRepository.saveAll(reports1);
            System.out.println("Success insert from "+i+" to "+(i+batchSize));
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
