FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests


FROM openjdk:17

# Set build arguments
ARG DATABASE_HOST

# Set environment variables
ENV DATABASE_HOST=${DATABASE_HOST}

COPY --from=build target/*.jar devops.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "devops.jar"]