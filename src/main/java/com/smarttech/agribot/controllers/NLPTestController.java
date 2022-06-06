package com.smarttech.agribot.controllers;

import com.smarttech.agribot.service.NLPAnswerResolver;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NLPTestController {

  public NLPTestController(NLPAnswerResolver nlp) {
    this.nlp = nlp;
  }

  private NLPAnswerResolver nlp;

  @PostMapping("/nlp")
  public String getIntentEntities(@RequestBody String question){
    return nlp.getAnswer(question);
  }
}
