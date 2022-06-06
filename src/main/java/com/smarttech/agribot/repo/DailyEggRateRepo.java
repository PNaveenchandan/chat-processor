package com.smarttech.agribot.repo;

import com.smarttech.agribot.entities.DailyEggRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DailyEggRateRepo extends JpaRepository<DailyEggRate, Integer> {
  List<DailyEggRate> findByDate(Date date);
  List<DailyEggRate> findByDateAndCityId(Date date, String cityId);
}
