FROM openjdk:8-jdk-alpine
VOLUME /app
WORKDIR /app
EXPOSE 9090
ARG JAR_FILE=tidder-user-1.0-SNAPSHOT.jar
ADD ./target/$JAR_FILE /app
#RUN echo "#!/bin/bash \n java -jar ${JAR_FILE}" > ./entrypoint.sh
ENTRYPOINT ["java", "-jar", "tidder-user-1.0-SNAPSHOT.jar"]
