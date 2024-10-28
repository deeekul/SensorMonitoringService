FROM openjdk:17

WORKDIR /opt/app

ARG JAR_FILE=SensorMonitoringService-0.1.0.jar

COPY ./build/libs/${JAR_FILE} /opt/app/${JAR_FILE}

ENTRYPOINT ["java", "-jar", "/opt/app/${JAR_FILE}"]

EXPOSE 8080