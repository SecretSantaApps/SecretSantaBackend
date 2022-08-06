docker-compose down
./gradlew shadowJar
docker-compose up -d --build
