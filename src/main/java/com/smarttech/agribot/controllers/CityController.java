package com.smarttech.agribot.controllers;

import com.smarttech.agribot.entities.City;
import com.smarttech.agribot.repo.CityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CityController {

  private CityRepo repo;

  @Autowired
  public CityController(CityRepo repo){
    this.repo = repo;
  }
  @GetMapping("/cities")
  public List<City> getAllCities(){
    return repo.findAll();
  }
}
