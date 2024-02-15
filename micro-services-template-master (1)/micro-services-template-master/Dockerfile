FROM registry.docker.nat.bt.com/im-microservices/alpine-java:jdk8
MAINTAINER Suman Mandal <suman.mandal@bt.com>
CMD mkdir  -p /apps/micro-services-template
ARG JAR_FILE
COPY target/${JAR_FILE} micro-services-template.jar
CMD  java $JAVA_OPTS -jar micro-services-template.jar
EXPOSE 9086


