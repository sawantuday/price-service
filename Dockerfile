FROM java:8

COPY target/*.jar /app

RUN chown 99:99 /app && chmod 644 /app

WORKDIR /app

USER 99

EXPOSE 8080

CMD ["java", "-jar" "tickerService-1.0-SNAPSHOT.jar"]