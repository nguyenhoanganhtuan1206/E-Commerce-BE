FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew .

COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x gradlew 
RUN ./gradlew clean build

EXPOSE 80

CMD ["./gradlew", "bootRun"]
