FROM amazoncorretto:21-alpine-jdk

COPY /target/bookstore-api-java.jar ./bookstore-api-java.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/bookstore-api-java.jar"]