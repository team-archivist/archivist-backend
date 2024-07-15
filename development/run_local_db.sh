#!/bin/bash

docker rm -f archivist-db || true
docker rm -f archivist-redis || true

docker volume create archivist-db
docker volume create archivist-redis

docker run \
  --name archivist-db \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -v archivist-db:/var/lib/mysql \
  -p 3306:3306 \
  -d mysql:8.4.0

docker run \
  --name archivist-redis \
  -v archivist-redis:/data \
  -p 6379:6379 \
  -d redis:7.2.5

sleep 1;

mysql -h 127.0.0.1 -u root -p1234 -e "use archivist"
if [[ $? -ne 0 ]]; then
  mysql -h 127.0.0.1 -u root -p1234 -e "CREATE DATABASE archivist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
fi

mysql -h 127.0.0.1 -u root -p1234 -e "use archivist; select 1"
