
// src/main/java/com/portfolio/portfoliotracker/repository/StockRepository.java
package com.Portfolio.portfoliotracker.repository;

import com.Portfolio.portfoliotracker.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
