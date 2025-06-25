FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /usr/src/app
COPY . .

CMD ["mvn", "clean", "test"]
