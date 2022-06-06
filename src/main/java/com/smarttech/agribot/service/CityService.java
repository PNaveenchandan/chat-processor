package com.smarttech.agribot.service;

import com.smarttech.agribot.entities.City;
import com.smarttech.agribot.repo.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

  private CityRepo cityRepo;

  @Autowired
  public CityService(CityRepo cityRepo) {
    this.cityRepo = cityRepo;
  }

  public List<String> getAllCityNames(){
    return cityRepo.findAll().stream().map(city->city.getCityName()).collect(Collectors.toList());
  }

  public City getCityByName(String cityName){
    return cityRepo.findByCityName(cityName);
  }
}
