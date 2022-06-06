package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.InfoDto;
import com.smarttech.agribot.nlp.IntentTrainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class NLPAnswerResolver implements AnswerResolver{

  private IntentTrainer nlp;

  private CityService cityService;

  @Autowired
  public NLPAnswerResolver(IntentTrainer nlp, CityService cityService){
    this.nlp = nlp;
    this.cityService = cityService;
  }
  @Override
  public String getAnswer(String question) {
    log.info(String.format("running NLP resolver for question %s",question));
    InfoDto info = nlp.getIntentAndEntities(question);
    log.info(String.format("got intent %s and entities %s",info.getIntent(),info.getEntities().toString()));
    if(info.getIntent().equalsIgnoreCase("todays-egg-rate")){
      Optional<String> cityName =
        info.getEntities().stream().filter(entity -> entity.toLowerCase().contains("Bengaluru".toLowerCase())).findFirst();
      log.info(String.format("city name %s",cityName.isPresent() ? cityName.get() : ""));
      if(cityName.isPresent()) {
        return String.format("today's egg rate in %s is 540", cityName.get());
      }
    }
    return "ERR";
  }
}
