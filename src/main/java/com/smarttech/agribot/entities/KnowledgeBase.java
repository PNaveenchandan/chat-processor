package com.smarttech.agribot.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "knowledge_base")
@Getter
@Setter
@ToString
public class KnowledgeBase {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name="intent")
  private String intent;
  @Column(name="entity")
  private String entity;
  @Column(name="answer")
  private String answer;
}

