package com.smarttech.agribot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class InfoDto {

  private String intent;

  private List<String> entities;
}

