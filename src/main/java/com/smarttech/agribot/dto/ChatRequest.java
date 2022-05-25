package com.smarttech.agribot.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
public class ChatRequest {
  @NonNull
  //TODO restrict this to specific supported languages only
  private String srcLanguage;
  @NonNull
  //TODO restrict to non empty
  private String question;
}
