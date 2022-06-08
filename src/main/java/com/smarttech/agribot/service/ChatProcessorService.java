package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.ChatResponse;
import com.smarttech.agribot.nlp.IntentTrainer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ChatProcessorService {

  private IntentTrainer nlp;

  @Setter
  @Autowired
  private RiveAnswerResolver riveAnswerResolver;

  @Setter
  @Autowired
  private NLPAnswerResolver nlpAnswerResolver;

  private List<AnswerResolver> resolvers = new LinkedList<>();

  @PostConstruct
  public void init(){
    resolvers.add(riveAnswerResolver);
    resolvers.add(nlpAnswerResolver);
  }

  public ChatResponse answer(String question){
    log.info(String.format("there are %d resolvers ", resolvers.size()));
    for (AnswerResolver resolver : resolvers) {
      ChatResponse response = resolver.getAnswer(question);
      if(isValidReply(response.getAnswer())){
        return response;
      }
    }
    return new ChatResponse("ERR",false, null);
  }

  private boolean isValidReply(String reply) {
    return reply != null && !reply.isEmpty() && !reply.contains("ERR");
  };
}
