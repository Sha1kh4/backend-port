
// src/main/java/com/portfolio/portfoliotracker/service/StockService.java
package com.Portfolio.portfoliotracker.service;

import com.Portfolio.portfoliotracker.dto.PortfolioSummaryDTO;
import com.Portfolio.portfoliotracker.dto.StockDTO;
import com.Portfolio.portfoliotracker.model.Stock;
import com.Portfolio.portfoliotracker.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final AlphaVantageService alphaVantageService;
    
    @Autowired
    public StockService(StockRepository stockRepository, AlphaVantageService alphaVantageService) {
        this.stockRepository = stockRepository;
        this.alphaVantageService = alphaVantageService;
    }
    
    public List<Stock> getAllStocks() {
        return stockRepository.findAll().stream()
            .map(this::enrichStockWithCurrentPrice)
            .collect(Collectors.toList());
    }
    
    public Stock addStock(StockDTO stockDTO) {
        Stock stock = new Stock();
        stock.setName(stockDTO.getName());
        stock.setTicker(stockDTO.getTicker());
        stock.setQuantity(stockDTO.getQuantity());
        stock.setBuyPrice(stockDTO.getBuyPrice());
        
        return enrichStockWithCurrentPrice(stockRepository.save(stock));
    }
    
    public Stock updateStock(Long id, StockDTO stockDTO) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found"));
            
        stock.setName(stockDTO.getName());
        stock.setTicker(stockDTO.getTicker());
        stock.setQuantity(stockDTO.getQuantity());
        stock.setBuyPrice(stockDTO.getBuyPrice());
        
        return enrichStockWithCurrentPrice(stockRepository.save(stock));
    }
    
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
    
    public PortfolioSummaryDTO getPortfolioSummary() {
        List<Stock> stocks = getAllStocks();
        
        PortfolioSummaryDTO summary = new PortfolioSummaryDTO();
        summary.setTotalValue(calculateTotalValue(stocks));
        summary.setTotalInvestment(calculateTotalInvestment(stocks));
        summary.setTotalGainLoss(summary.getTotalValue().subtract(summary.getTotalInvestment()));
        summary.setGainLossPercentage(calculateGainLossPercentage(summary.getTotalGainLoss(), summary.getTotalInvestment()));
        summary.setTopPerformer(findTopPerformer(stocks));
        
        return summary;
    }
    
    private Stock enrichStockWithCurrentPrice(Stock stock) {
        BigDecimal currentPrice = alphaVantageService.getCurrentPrice(stock.getTicker());
        stock.setCurrentPrice(currentPrice);
        stock.setTotalValue(currentPrice.multiply(BigDecimal.valueOf(stock.getQuantity())));
        return stock;
    }
    
    private BigDecimal calculateTotalValue(List<Stock> stocks) {
        return stocks.stream()
            .map(Stock::getTotalValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateTotalInvestment(List<Stock> stocks) {
        return stocks.stream()
            .map(stock -> stock.getBuyPrice().multiply(BigDecimal.valueOf(stock.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateGainLossPercentage(BigDecimal gainLoss, BigDecimal investment) {
        if (investment.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return gainLoss.multiply(BigDecimal.valueOf(100))
            .divide(investment, 2, RoundingMode.HALF_UP);
    }
    
    private PortfolioSummaryDTO.TopPerformer findTopPerformer(List<Stock> stocks) {
        Stock topStock = stocks.stream()
            .max((s1, s2) -> {
                BigDecimal gain1 = calculateGainPercentage(s1);
                BigDecimal gain2 = calculateGainPercentage(s2);
                return gain1.compareTo(gain2);
            })
            .orElse(null);
            
        if (topStock == null) {
            return null;
        }
        
        PortfolioSummaryDTO.TopPerformer topPerformer = new PortfolioSummaryDTO.TopPerformer();
        topPerformer.setTicker(topStock.getTicker());
        topPerformer.setGainPercentage(calculateGainPercentage(topStock));
        return topPerformer;
    }
    
    private BigDecimal calculateGainPercentage(Stock stock) {
        return stock.getCurrentPrice()
            .subtract(stock.getBuyPrice())
            .multiply(BigDecimal.valueOf(100))
            .divide(stock.getBuyPrice(), 2, RoundingMode.HALF_UP);
    }
}
