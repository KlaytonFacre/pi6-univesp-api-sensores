#!/usr/bin/env sh
set -eu

: "${DATABASE_HOST:?DATABASE_HOST não definida}"
: "${DATABASE_NAME:?DATABASE_NAME não definida}"
: "${DATABASE_USER:?DATABASE_USER não definida}"
: "${DATABASE_PASSWORD:?DATABASE_PASSWORD não definida}"

exec java -jar /app/app.jar