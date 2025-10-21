# Estimator API

Estimator API is a Java-based backend service for property price estimation, built using Spring Boot. It provides endpoints for estimating property prices based on historical sales data and other relevant parameters. The API is designed for integration with real estate platforms, analytics tools, or any application requiring automated property price estimation.

## Features
- RESTful endpoints for property price estimation
- DTOs for structured data transfer
- Service layer for business logic and sales history analysis
- Modular architecture for easy extension
- Environment-specific configuration support (dev, prod)
- Caching support via Ehcache

## Project Structure
```
src/
  main/
    java/dk/bolig/
      Application.java                # Main Spring Boot application
      controller/EstimateController.java  # REST API controller
      dto/EstimateDTO.java            # Data Transfer Object
      service/SalesHistoryService.java    # Business logic for sales history
      tools/PriceEstimator.java       # Price estimation logic
    resources/
      application.properties          # Main config
      application-dev.properties      # Dev config
      application-prod.properties     # Prod config
      ehcache-dev.xml                 # Dev cache config
      ehcache-prod.xml                # Prod cache config
  test/
    java/dk/bolig/
      controller/EstimateControllerTest.java # Controller tests
      dto/EstimateDTOTest.java              # DTO tests
      tools/AppTest.java                    # General tests
```

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven

### Build & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/pdharris100/estimator-api.git
   cd estimator-api
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Configuration
- Default configuration is in `src/main/resources/application.properties`.
- For development, use `application-dev.properties`.
- For production, use `application-prod.properties`.
- Ehcache configuration files are also provided for both environments.

## API Endpoints
- `/estimate` - Estimate property price (see `EstimateController`)

## Testing
Run all tests with:
```bash
mvn test
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.

## Contact
For questions or support, contact [Paul Harris](mailto:paul@yourdomain.com).
