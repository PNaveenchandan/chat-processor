package com.smarttech.agribot.repo;

import com.smarttech.agribot.entities.DailyEggRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DailyEggRateRepo extends JpaRepository<DailyEggRate, Integer> {
  List<DailyEggRate> findByDate(Date date);
  List<DailyEggRate> findByDateAndCityId(Date date, Integer cityId);
  List<DailyEggRate> findByCityIdAndDateGreaterThanEqual(Integer cityId, Date date);


//  @Query("SELECT * FROM egg_price_daily e1\n" +
//    "WHERE e1.price = (SELECT max(e2.price) FROM egg_price_daily e2 where e2.date = :date) and e1.date = " +
//    ":date")
//  DailyEggRateRepo findTodaysMaxPriceCity(@Param("date") Date date);

}
