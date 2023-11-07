FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew .

COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x gradlew 
RUN ./gradlew clean build

COPY /build/libs/e-commerce-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 80

#CMD ["./gradlew", "bootRun"]
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=DEV"]