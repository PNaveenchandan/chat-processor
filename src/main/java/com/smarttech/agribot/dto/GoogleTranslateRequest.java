package com.smarttech.agribot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleTranslateRequest {
  private List<String> q;
  private String target;
}
