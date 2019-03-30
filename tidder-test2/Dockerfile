FROM maven:3.3-jdk-8
VOLUME /app
RUN mvnw clean package
COPY ./target/tidder-test2-0.0.1-SNAPSHOT.jar /app
RUN rm -rf ./target/
ENTRYPOINT ["java", "-jar", "tidder-test2-0.0.1-SNAPSHOT.jar"]
