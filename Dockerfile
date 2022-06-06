FROM openjdk:11
#RUN rm -rf /usr/local/tomcat/webapps/*
VOLUME /tmp
COPY ./src/main/resources /tmp
COPY ./target/agribot-0.0.1-SNAPSHOT.jar agribot.jar
ENTRYPOINT ["java","-jar","/agribot.jar"]