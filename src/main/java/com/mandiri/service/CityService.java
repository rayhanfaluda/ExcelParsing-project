package com.mandiri.service;

import com.mandiri.entity.City;
import com.mandiri.entity.Report;
import com.mandiri.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    CityRepository cityRepository;

    public boolean findCity(String city){
        City currentCity = cityRepository.findByCity(city);
        return currentCity != null;
    }
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }
}
