
// src/main/java/com/portfolio/portfoliotracker/controller/StockController.java
package com.Portfolio.portfoliotracker.controller;

import com.Portfolio.portfoliotracker.dto.PortfolioSummaryDTO;
import com.Portfolio.portfoliotracker.dto.StockDTO;
import com.Portfolio.portfoliotracker.model.Stock;
import com.Portfolio.portfoliotracker.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
                       RequestMethod.DELETE, RequestMethod.OPTIONS},
             allowCredentials = "true")
public class StockController {
    private final StockService stockService;
    
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }
    
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody StockDTO stockDTO) {
        return new ResponseEntity<>(stockService.addStock(stockDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        return ResponseEntity.ok(stockService.updateStock(id, stockDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/portfolio-summary")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary() {
        return ResponseEntity.ok(stockService.getPortfolioSummary());
    }
}
