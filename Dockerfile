FROM eclipse-temurin:17.0.8_7-jdk

EXPOSE 8080

COPY ./ ./

RUN ./gradlew bootJar && cp build/libs/tokyo-opendata-chatgpt-plugin.jar ./app.jar

ENTRYPOINT ["java","-jar","/app.jar"]