package com.smarttech.agribot.service;

import com.smarttech.agribot.dto.ChatResponse;

public interface AnswerResolver {
  public ChatResponse getAnswer(String question);
}
