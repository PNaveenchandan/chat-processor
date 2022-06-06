package com.smarttech.agribot.repo;

import com.smarttech.agribot.entities.KnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeBankRepo extends JpaRepository<KnowledgeBase, Integer> {
}