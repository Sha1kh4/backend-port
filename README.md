# Portfolio Tracker API

A Spring Boot application for tracking stock portfolios with real-time price updates.

## Features

- Track multiple stocks in your portfolio
- Real-time stock price updates (using Alpha Vantage API)
- Calculate portfolio performance metrics
- RESTful API endpoints for CRUD operations
- CORS enabled for frontend integration
- Automatic price updates and caching
- Portfolio summary with gain/loss calculations

## Prerequisites

- Java 21
- Maven
- MySQL Database
- Alpha Vantage API key (get it from [Alpha Vantage](https://www.alphavantage.co/support/#api-key))

## Setup

1. Clone the repository
```bash
git clone https://github.com/Sha1kh4/backend-port.git
cd backend-port
```

2. Configure application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Alpha Vantage Configuration
alphavantage.api.key=your_api_key

# Server Configuration
server.port=8080
```

3. Create the database
```sql
CREATE DATABASE portfolio_tracker;
```

4. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

## Database Schema

```sql
CREATE TABLE stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    buy_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## API Testing Guide

You can test the API using the following curl commands:

### 1. Add Stocks

Add Apple stock:
```bash
curl -X POST http://localhost:8080/api/stocks \
-H "Content-Type: application/json" \
-d '{
    "name": "Apple Inc.",
    "ticker": "AAPL",
    "quantity": 10,
    "buyPrice": 150.50
}'
```

Add Microsoft stock:
```bash
curl -X POST http://localhost:8080/api/stocks \
-H "Content-Type: application/json" \
-d '{
    "name": "Microsoft Corporation",
    "ticker": "MSFT",
    "quantity": 5,
    "buyPrice": 280.75
}'
```

Add Tesla stock:
```bash
curl -X POST http://localhost:8080/api/stocks \
-H "Content-Type: application/json" \
-d '{
    "name": "Tesla Inc.",
    "ticker": "TSLA",
    "quantity": 8,
    "buyPrice": 190.25
}'
```

### 2. Get All Stocks
```bash
curl http://localhost:8080/api/stocks
```

### 3. Get Portfolio Summary
```bash
curl http://localhost:8080/api/stocks/portfolio-summary
```

### 4. Update Stock
Replace `{id}` with the actual stock ID:
```bash
curl -X PUT http://localhost:8080/api/stocks/{id} \
-H "Content-Type: application/json" \
-d '{
    "name": "Apple Inc.",
    "ticker": "AAPL",
    "quantity": 15,
    "buyPrice": 150.50
}'
```

### 5. Delete Stock
Replace `{id}` with the actual stock ID:
```bash
curl -X DELETE http://localhost:8080/api/stocks/{id}
```

### Windows PowerShell Users
Use this format instead:
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/stocks" `
-Headers @{"Content-Type"="application/json"} `
-Body '{
    "name": "Apple Inc.",
    "ticker": "AAPL",
    "quantity": 10,
    "buyPrice": 150.50
}'
```

### Windows CMD Users
Use this format (all in one line):
```cmd
curl -X POST http://localhost:8080/api/stocks -H "Content-Type: application/json" -d "{\"name\":\"Apple Inc.\",\"ticker\":\"AAPL\",\"quantity\":10,\"buyPrice\":150.50}"
```

## API Response Examples

### Stock Response
```json
{
    "id": 1,
    "name": "Apple Inc.",
    "ticker": "AAPL",
    "quantity": 10,
    "buyPrice": 150.50,
    "createdAt": "2024-03-14T10:30:00",
    "updatedAt": "2024-03-14T10:30:00",
    "currentPrice": 160.75,
    "totalValue": 1607.50
}
```

### Portfolio Summary Response
```json
{
    "totalValue": 1607.50,
    "totalInvestment": 1505.00,
    "totalGainLoss": 102.50,
    "gainLossPercentage": 6.81,
    "topPerformer": {
        "ticker": "AAPL",
        "gainPercentage": 6.81
    }
}
```

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- 200: Success
- 201: Created
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

Error Response Format:
```json
{
    "timestamp": "2024-03-14T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Stock with ID 1 not found"
}
```

## Rate Limiting

- Alpha Vantage API has a rate limit of 5 calls per minute for free tier
- The application implements caching to minimize API calls
- Stock prices are cached for 1 minute

## Notes

- In the demo version, current prices are simulated using random numbers
- In production, the application uses real market data from the Alpha Vantage API
- Make sure to replace the Alpha Vantage API key with your own in application.properties
- The application uses CORS configuration to allow requests from localhost:3000 and localhost:5173