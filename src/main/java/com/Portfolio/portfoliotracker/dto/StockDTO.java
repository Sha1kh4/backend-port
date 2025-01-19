
// src/main/java/com/portfolio/portfoliotracker/dto/StockDTO.java
package com.Portfolio.portfoliotracker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockDTO {
    private String name;
    private String ticker;
    private Integer quantity;
    private BigDecimal buyPrice;
}
