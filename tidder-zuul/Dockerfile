FROM maven:3.3-jdk-8
VOLUME /app
RUN mvnw clean package
COPY ./target/tidder-zuul-0.0.1-SNAPSHOT.jar /app
RUN rm -rf ./target/
ENTRYPOINT ["java", "-jar", "tidder-zuul-0.0.1-SNAPSHOT.jar"]
