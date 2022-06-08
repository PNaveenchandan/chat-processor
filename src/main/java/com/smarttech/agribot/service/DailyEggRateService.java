package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.GraphPoint;
import com.smarttech.agribot.entities.DailyEggRate;
import com.smarttech.agribot.repo.DailyEggRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DailyEggRateService {

  private DailyEggRateRepo eggRateRepo;

  @Autowired
  public DailyEggRateService(DailyEggRateRepo eggRateRepo) {
    this.eggRateRepo = eggRateRepo;
  }

  public DailyEggRate getByDateAndCityId(Date date, Integer cityId){
      List<DailyEggRate> response = eggRateRepo.findByDateAndCityId(date, cityId);
      if(!CollectionUtils.isEmpty(response)){
        return response.get(0);
      }
      return null;
  }

  public DailyEggRate getTodaysMaximumPricedCity(Date date){
    Optional<DailyEggRate> max =
      eggRateRepo.findByDate(date).stream().max((DailyEggRate e1, DailyEggRate e2) -> Double.compare(e1.getPrice(),
        e2.getPrice()));
    if(max.isPresent()){
      return max.get();
    }
    return null;
  }

  public List<GraphPoint> getOneMonthData(Date date, Integer cityId){
    List<DailyEggRate> dailyEggRates =
      eggRateRepo.findByCityIdAndDateGreaterThanEqual(cityId, date);
    if(!CollectionUtils.isEmpty(dailyEggRates)){
      return dailyEggRates.stream().map(dailyEggRate->{return new GraphPoint(dailyEggRate.getDate(),dailyEggRate.getPrice());}).collect(Collectors.toList());
    }
    return Collections.EMPTY_LIST;
  }
}
