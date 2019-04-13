#!/bin/bash
mvn clean install
docker-compose stop
docker-compose rm -f
docker system prune -a -f
docker-compose --compatibility up --build -d
