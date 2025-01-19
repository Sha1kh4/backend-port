
// src/main/java/com/portfolio/portfoliotracker/dto/PortfolioSummaryDTO.java
package com.Portfolio.portfoliotracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PortfolioSummaryDTO {
    private BigDecimal totalValue;
    private BigDecimal totalInvestment;
    private BigDecimal totalGainLoss;
    private BigDecimal gainLossPercentage;
    private TopPerformer topPerformer;
    
    @Data
    public static class TopPerformer {
        private String ticker;
        private BigDecimal gainPercentage;
    }
}
