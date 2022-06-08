package com.smarttech.agribot.dto;

import lombok.Value;

import java.util.Date;

@Value
public class GraphPoint {

  private Date date;
  private Double price;

  @Override
  public String toString(){
    return String.format("{date=%s,price=%.2f}",date.toString(), price);
  }
}
