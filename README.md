# mail-grub
An application for small businesses who deliver baked goods and foods by mail

Environment variables to run project locally:

PostgreSQL database settings
- POSTGRES_DB=mail-grub
- POSTGRES_USER=admin
- POSTGRES_PASSWORD=admin

Optional: override the default port if needed
- POSTGRES_PORT=5432

CORS settings for your frontend
- APP_CORS_ALLOWED_ORIGINS=http://localhost:5173

Spring Boot active profile
- SPRING_PROFILES_ACTIVE=postgres ./mvnw spring-boot:run

You must have Docker running in order for application to correctly create DB

For Open Api/Swagger Documentation locally, when app is running visit:

http://localhost:8080/swagger-ui/index.html#/

Example Curl Calls for testing:

curl -s "http://localhost:8080/ingredients?page=0&size=10"

curl -X POST http://localhost:8080/ingredients/add \
  -H "Content-Type: application/json" \
  -d '{"name": "Flour", "purchaseSize": 80, "averageCost": 12.99, "measurementType": "OZ"}'

curl -s "http://localhost:8080/recipes?page=0&size=10"

curl -X POST http://localhost:8080/recipes \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Chocolate Chip Cookies",
        "itemsMade": 24,
        "recipeIngredients": [
        {
            "ingredient": { "id": 1 },
            "amount": 8.0,
            "overrideMeasurementType": "OZ"
          },
          {
            "ingredient": { "id": 2 },
            "amount": 4.0
          }
        ]
      }'
