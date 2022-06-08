package com.smarttech.agribot.service;

import com.smarttech.agribot.entities.City;
import com.smarttech.agribot.repo.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class CityService {

  private CityRepo cityRepo;

  private Map<String, City> cityNameMap = new HashMap<>();
  private Map<Integer, City> cityIdMap = new HashMap<>();

  @PostConstruct
  public void init(){
    cityRepo.findAll().forEach(city->{
      cityNameMap.putIfAbsent(city.getCityName().toLowerCase(),city);
      cityIdMap.putIfAbsent(city.getId(),city);
      if(city.getCityName().toLowerCase().equalsIgnoreCase("bengaluru")){
        cityNameMap.putIfAbsent("bangalore",city);
      }
      if(city.getCityName().toLowerCase().equalsIgnoreCase("mysuru")){
        cityNameMap.putIfAbsent("mysore",city);
      }
    });
  }
  @Autowired
  public CityService(CityRepo cityRepo) {
    this.cityRepo = cityRepo;
  }

  public City getCityByName(String cityName){
    return cityNameMap.get(cityName);
  }

  public City getCityById(Integer id){
    return cityIdMap.get(id);
  }
}
