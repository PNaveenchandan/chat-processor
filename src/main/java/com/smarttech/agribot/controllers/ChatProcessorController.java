package com.smarttech.agribot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smarttech.agribot.config.GoogleConfig;
import com.smarttech.agribot.dto.ChatRequest;
import com.smarttech.agribot.dto.ChatResponse;
import com.smarttech.agribot.service.ChatProcessorService;
import com.smarttech.agribot.util.GoogleTranslator;
import com.smarttech.agribot.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatProcessorController {

  private GoogleConfig config;

  private ChatProcessorService service;

  @Autowired
  public ChatProcessorController(GoogleConfig config, ChatProcessorService service) {
    this.config = config;
    this.service = service;
  }

  private String translate(String text, String targetLang){
    try {
      return GoogleTranslator.translate(config, text,targetLang);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("failed to translate requested query to target language please try again");
    }
  }
  @PostMapping("/chat")
  public ChatResponse getAnswer(@RequestBody ChatRequest request){
    String question = request.getQuestion();
    if(!request.getSrcLanguage().equalsIgnoreCase("en")){
     question = translate(question,"en");
    }

    String answer = service.answer(question);

    if(!ResponseUtil.isValidReply(answer)){
      answer = "Couldn't understand your question. Please try again !";
    }
    if(!request.getSrcLanguage().equalsIgnoreCase("en")){
      answer = translate(answer,request.getSrcLanguage());
    }
    ChatResponse response = new ChatResponse();
    response.setResponse(answer);
    return response;
  }
}
