FROM eclipse-temurin:17-jdk-focal as builder

COPY gradle/ gradle/
COPY gradlew .

COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x gradlew 
RUN ./gradlew clean build

FROM  eclipse-temurin:17-jdk-focal
WORKDIR /app

COPY --from=builder /build/libs/e-commerce-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 80

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=DEV"]