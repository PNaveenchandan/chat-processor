package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.GraphPoint;
import com.smarttech.agribot.entities.MonthlyEggRate;
import com.smarttech.agribot.repo.MonthlyEggRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyEggRateService {
  private MonthlyEggRateRepo monthlyEggRateRepo;

  @Autowired
  public MonthlyEggRateService(MonthlyEggRateRepo monthlyEggRateRepo) {
    this.monthlyEggRateRepo = monthlyEggRateRepo;
  }

  public List<GraphPoint> getMonthlyAverage(Date date, Integer cityId){
    List<MonthlyEggRate> monthlyRate =
      monthlyEggRateRepo.findByCityIdAndDateGreaterThanEqual(cityId, date);
    if(!CollectionUtils.isEmpty(monthlyRate)){
        return monthlyRate.stream().map(monthly->{return new GraphPoint(monthly.getDate(),monthly.getPrice());}).collect(Collectors.toList());
    }
    return Collections.EMPTY_LIST;
  }
}
