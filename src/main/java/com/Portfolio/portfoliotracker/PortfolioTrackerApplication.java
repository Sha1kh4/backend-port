// src/main/java/com/portfolio/portfoliotracker/PortfolioTrackerApplication.java
package com.Portfolio.portfoliotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.portfolio.portfoliotracker.model")
@EnableJpaRepositories("com.portfolio.portfoliotracker.repository")
public class PortfolioTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortfolioTrackerApplication.class, args);
    }
}