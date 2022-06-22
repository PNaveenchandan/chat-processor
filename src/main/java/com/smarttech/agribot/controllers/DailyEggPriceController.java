package com.smarttech.agribot.controllers;


import com.smarttech.agribot.entities.DailyEggRate;
import com.smarttech.agribot.repo.DailyEggRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class DailyEggPriceController {
  private DailyEggRateRepo repo;

  @Autowired
  public DailyEggPriceController(DailyEggRateRepo repo){
    this.repo = repo;
  }
  @GetMapping("/dailyeggrate")
  public List<DailyEggRate> getAllCities(){
    return repo.findByDate(new Date());
  }
}
