FROM openjdk:8-jdk-alpine
VOLUME /app
WORKDIR /app
EXPOSE 9090
ARG JAR_FILE=tidder-post-0.0.1-SNAPSHOT.jar
ADD ./target/$JAR_FILE /app
#RUN echo "#!/bin/bash \n java -jar ${JAR_FILE}" > ./entrypoint.sh
ENTRYPOINT ["java", "-jar", "tidder-post-0.0.1-SNAPSHOT.jar"]
