package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.ChatResponse;
import com.smarttech.agribot.dto.GraphPoint;
import com.smarttech.agribot.dto.InfoDto;
import com.smarttech.agribot.entities.City;
import com.smarttech.agribot.entities.DailyEggRate;
import com.smarttech.agribot.nlp.IntentTrainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NLPAnswerResolver implements AnswerResolver {

  private IntentTrainer nlp;

  private CityService cityService;

  private DailyEggRateService dailyEggRateService;

  private MonthlyEggRateService monthlyEggRateService;

  @Autowired
  public NLPAnswerResolver(IntentTrainer nlp, CityService cityService, DailyEggRateService dailyEggRateService,
                           MonthlyEggRateService monthlyEggRateService) {
    this.nlp = nlp;
    this.cityService = cityService;
    this.dailyEggRateService = dailyEggRateService;
    this.monthlyEggRateService = monthlyEggRateService;
  }

  @Override
  public ChatResponse getAnswer(String question) {
    log.info(String.format("running NLP resolver for question %s", question));
    String answer = "ERR";
    boolean isGraphData = false;
    List<GraphPoint> graphData = null;
    InfoDto info = nlp.getIntentAndEntities(question);
    log.info(String.format("got intent %s and entities %s", info.getIntent(), info.getEntities().toString()));
    if (List.of("todays-egg-rate", "yesterdays-egg-rate").contains(info.getIntent()) && isEggQuery(info.getEntities())) {
      Optional<String> cityName =
        info.getEntities().stream().filter(entity -> null != cityService.getCityByName(entity.toLowerCase().trim())).findFirst();
      if (cityName.isPresent()) {
        City city = cityService.getCityByName(cityName.get());
        Instant date = info.getIntent().equals("todays-egg-rate") ? Instant.now() : Instant.now().minus(1,
          ChronoUnit.DAYS);
        Date eggRateDate = Date.from(date.atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        log.info("fetching egg rate for city and date" + eggRateDate + " city " + city.getId());
        DailyEggRate eggRate = dailyEggRateService.getByDateAndCityId(eggRateDate, city.getId());
        if (null != eggRate) {
          answer = String.format("%s egg price in %s is %.2f INR", info.getIntent().equals("todays-egg-rate") ?
            "Today" +
              "'s" : "Yesterday's", city.getCityName(), eggRate.getPrice());
        } else {
          answer = String.format("egg price is not yet published for the city %s", city.getCityName());
        }
      } else {
        answer = "I don't recognize this city yet, we will try to add this to our list next time!";
      }
    } else if (info.getIntent().equals("max-egg-rate") && isEggQuery(info.getEntities())) {
      DailyEggRate todaysMaximumPricedCity =
        dailyEggRateService.getTodaysMaximumPricedCity(Date.from(Instant.now().atZone(ZoneId.of("Asia/Kolkata")).toInstant()));
      if (null != todaysMaximumPricedCity) {
        answer = String.format("%s offers maximum price for eggs today and price is %.2f INR",
          cityService.getCityById(todaysMaximumPricedCity.getCityId()).getCityName(),
          todaysMaximumPricedCity.getPrice());
      }
    } else if (info.getIntent().equals("average-egg-price") && isEggQuery(info.getEntities())) {
      Optional<String> cityName =
        info.getEntities().stream().filter(entity -> null != cityService.getCityByName(entity.toLowerCase().trim())).findFirst();
      if (cityName.isPresent()) {
        City city = cityService.getCityByName(cityName.get());
        Date fromDate =
          Date.from(Instant.now().minus(365, ChronoUnit.DAYS).atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        graphData = monthlyEggRateService.getMonthlyAverage(fromDate, city.getId());
        answer = String.format("Here is the average egg price of city %s in last 12 months", cityName.get());
        isGraphData = true;
      } else {
        answer = "I don't recognize this city yet, we will try to add this to our list next time!";
      }
    } else if (info.getIntent().equals("this-month-egg-rate") && isEggQuery(info.getEntities())) {
      Optional<String> cityName =
        info.getEntities().stream().filter(entity -> null != cityService.getCityByName(entity.toLowerCase().trim())).findFirst();
      if (cityName.isPresent()) {
        City city = cityService.getCityByName(cityName.get());
        Date fromDate =
          Date.from(Instant.now().minus(30, ChronoUnit.DAYS).atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        graphData = dailyEggRateService.getOneMonthData(fromDate, city.getId());
        answer = String.format("Here is the egg price for city %s in last 30 days", cityName.get());
        isGraphData = true;
      } else {
        answer = "I don't recognize this city yet, we will try to add this to our list next time!";
      }
    }
    return new ChatResponse(answer, isGraphData, graphData);
  }

  private boolean isEggQuery(List<String> entities) {
    return entities.stream().filter(entity -> entity.contains("egg")).findAny().isPresent();
  }
}
