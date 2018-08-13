FROM jboss/base-jdk:8
VOLUME /tmp
ADD kafka-ui-server/target/kafka-ui-server-1.0-*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]