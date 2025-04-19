FROM openjdk:23-slim
LABEL authors="stefan"

COPY build/libs/BookManager.jar /app.jar

EXPOSE 8080/tcp

CMD [ "java", "-jar", "/app.jar" ]