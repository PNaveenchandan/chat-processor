package com.smarttech.agribot.cache;

import com.smarttech.agribot.entities.KnowledgeBase;
import com.smarttech.agribot.repo.KnowledgeBankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KnowledgeBankCache {

  private Map<String, String> kb = new HashMap<>();

  private KnowledgeBankRepo repo;

  @Autowired
  public KnowledgeBankCache(KnowledgeBankRepo repo) {
    this.repo = repo;
  }

  @PostConstruct
  public void init(){
    List<KnowledgeBase> records = repo.findAll();
    records.stream().forEach((record)-> {
        String key = String.format("%s:%s", record.getIntent().trim(), record.getEntity().trim());
        kb.putIfAbsent(key,record.getAnswer().trim());
      }
    );
  }

  public String getAnswer(String key){
    return kb.getOrDefault(key,"ERR");
  }
}
