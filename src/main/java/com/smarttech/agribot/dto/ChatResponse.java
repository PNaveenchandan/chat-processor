package com.smarttech.agribot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ChatResponse {

  public ChatResponse(String answer, boolean isGraphData, List<GraphPoint> graphData) {
    this.answer = answer;
    this.isGraphData = isGraphData;
    this.graphData = graphData;
  }

  private String answer;
  private Boolean isGraphData;
  private List<GraphPoint> graphData;
}
