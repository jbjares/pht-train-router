FROM pht/parent-spring:latest

ARG JAR_FILE
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["dockerize", "-timeout", "5m", "-wait", "http://neo4j:7474/browser",  "-wait", "http://config-server:8100/actuator/health", "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]
EXPOSE 6004

