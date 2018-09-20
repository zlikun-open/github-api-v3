FROM java:8

MAINTAINER zlikun "zlikun-dev@hotmail.com"

ADD zlikun-open-github.jar /opt/app.jar

WORKDIR /opt

EXPOSE 40000

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=40000"]