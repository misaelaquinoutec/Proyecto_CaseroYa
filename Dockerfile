FROM eclipse-temurin:26-jdk-noble AS build
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/

RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B -ntp -DskipTests package \
    && cp target/caseroya-*.jar /build/app.jar

FROM eclipse-temurin:26-jre-noble
WORKDIR /app
COPY --from=build /build/app.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=60.0"
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
