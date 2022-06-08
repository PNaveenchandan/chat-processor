package com.smarttech.agribot.repo;

import com.smarttech.agribot.entities.MonthlyEggRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MonthlyEggRateRepo extends JpaRepository<MonthlyEggRate, Integer> {
  List<MonthlyEggRate> findByDate(Date date);
  List<MonthlyEggRate> findByDateAndCityId(Date date, Integer cityId);
  List<MonthlyEggRate> findByCityIdAndDateGreaterThanEqual(Integer cityId, Date date);
}
