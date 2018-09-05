FROM gcr.io/distroless/java
VOLUME /tmp
ADD build/libs/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","app.jar"]

