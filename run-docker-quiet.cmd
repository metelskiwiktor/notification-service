CALL mvn clean package
CALL docker-compose build --no-cache
CALL docker-compose up -d
