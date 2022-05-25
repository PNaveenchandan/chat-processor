package com.smarttech.agribot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smarttech.agribot.config.GoogleConfig;
import com.smarttech.agribot.dto.ChatRequest;
import com.smarttech.agribot.dto.ChatResponse;
import com.smarttech.agribot.util.GoogleTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatProcessorController {

  private GoogleConfig config;

  @Autowired
  public ChatProcessorController(GoogleConfig config) {
    this.config = config;
  }

  @PostMapping("/chat")
  public ChatResponse getAnswer(@RequestBody ChatRequest request){

    if(!request.getSrcLanguage().equalsIgnoreCase("en")){
      try {
        final String translatedQuestion = GoogleTranslator.translateToEnglish(config, request);
        ChatResponse response = new ChatResponse();
        response.setResponse(translatedQuestion);
        return response;
      } catch (JsonProcessingException e) {
        throw new RuntimeException("failed to translate requested query to target language please try again");
      }
    }
    ChatResponse response = new ChatResponse();
    response.setResponse("processed");
    return response;
  }
}
