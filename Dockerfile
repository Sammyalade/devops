FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests


FROM openjdk:17

# Set build arguments
ARG DATABASE_HOST
ARG DATABASE_USER
ARG DATABASE_PASSWORD

# Set environment variables
ENV DATABASE_HOST=${DATABASE_HOST}
ENV DATABASE_USER=${DATABASE_USER}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}

COPY --from=build target/*.jar devops.jar

ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "devops.jar"]