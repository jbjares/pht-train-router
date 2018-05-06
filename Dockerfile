FROM pht/parent-spring:latest

ARG JAR_FILE
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["dockerize", "-timeout", "5m", "-wait", "http://193.196.20.91:7474/browser",  "-wait", "http://193.196.20.86:6003/actuator/health", "-wait", "http://station-office:8763/actuator/health", "-wait", "http://train-office:8762/actuator/health", "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar", "--spring.profiles.active=denbi"]
EXPOSE 8764



