
// src/main/java/com/portfolio/portfoliotracker/service/AlphaVantageService.java
package com.Portfolio.portfoliotracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AlphaVantageService {
    @Value("${alphavantage.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    private final ConcurrentMap<String, CachedPrice> priceCache = new ConcurrentHashMap<>();
    
    public AlphaVantageService() {
        this.restTemplate = new RestTemplate();
    }
    
    public BigDecimal getCurrentPrice(String ticker) {
        CachedPrice cachedPrice = priceCache.get(ticker);
        if (cachedPrice != null && !cachedPrice.isExpired()) {
            return cachedPrice.getPrice();
        }
        
        // In a real implementation, you would call the Alpha Vantage API here
        // For demo purposes, returning a mock price
        BigDecimal price = BigDecimal.valueOf(Math.random() * 1000);
        priceCache.put(ticker, new CachedPrice(price));
        return price;
    }
    
    private static class CachedPrice {
        private final BigDecimal price;
        private final long timestamp;
        private static final long CACHE_DURATION = 60000; // 1 minute
        
        public CachedPrice(BigDecimal price) {
            this.price = price;
            this.timestamp = System.currentTimeMillis();
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION;
        }
    }
}
