package com.smarttech.agribot.controllers;

import com.smarttech.agribot.dto.ChatResponse;
import com.smarttech.agribot.service.NLPAnswerResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class NLPTestController {

  public NLPTestController(NLPAnswerResolver nlp) {
    this.nlp = nlp;
  }

  @Value("${management.endpoints.enabled}")
  private String management;

  private NLPAnswerResolver nlp;

  @PostMapping("/nlp")
  public ChatResponse getIntentEntities(@RequestBody String question){
    return nlp.getAnswer(question);
  }

  @GetMapping("/actenabled")
  public String getActEnabled(){
    return this.management;
  }
}
