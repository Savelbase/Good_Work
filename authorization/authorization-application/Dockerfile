FROM openjdk:11-jre

RUN adduser --system --home /var/cache/bootapp --shell /sbin/nologin bootapp;

COPY /target/authorization.jar authorization.jar

USER bootapp

ENTRYPOINT java -jar \
                       authorization.jar