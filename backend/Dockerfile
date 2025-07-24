# Use official JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# setting working dir inside container so that it can be mangaed
WORKDIR /app

# copy maven wrapper files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# copy source code
COPY src ./src

# building proj without test
RUN ./mvnw clean package -DskipTests

#running the jar file
CMD ["java", "-jar", "target/prowrite.jar"]

