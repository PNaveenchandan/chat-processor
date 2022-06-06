package com.smarttech.agribot.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "egg_price_daily")
@Getter
@Setter
@ToString
public class DailyEggRate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name="date")
  private Date date;
  @Column(name="price")
  private Double price;
  @Column(name = "city_id")
  private Integer cityId;
}
