package com.smarttech.agribot.repo;

import com.smarttech.agribot.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City, Integer> {
  City findByCityName(String name);
}