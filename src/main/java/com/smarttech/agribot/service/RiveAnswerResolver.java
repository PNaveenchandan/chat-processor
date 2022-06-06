package com.smarttech.agribot.service;

import com.smarttech.agribot.cache.KnowledgeBankCache;
import com.smarttech.agribot.cache.RiveCache;
import com.smarttech.agribot.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RiveAnswerResolver implements AnswerResolver{

  private KnowledgeBankCache kb;

  @Autowired
  public RiveAnswerResolver(KnowledgeBankCache kb){
    this.kb = kb;
  }

  @Override
  public String getAnswer(String question) {
    log.info(String.format("running rive resolver for question %s",question));
    String reply =  RiveCache.riveCache.get("BOT").reply("Agribot",question);
    log.info(String.format("reply from rive %s",reply));
    if(ResponseUtil.isValidReply(reply)){
      return kb.getAnswer(reply.trim());
    }
    return "ERR";
  }
}
