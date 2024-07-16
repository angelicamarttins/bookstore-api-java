FROM amazoncorretto:21.0.2-alpine

RUN ./mvnw clean package -DskipTests

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app.jar"]
