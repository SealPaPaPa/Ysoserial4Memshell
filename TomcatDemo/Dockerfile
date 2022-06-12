FROM tomcat:9.0.60-jre8-openjdk-slim-buster

RUN mv webapps.dist/* webapps/

ADD lib/commons-beanutils-1.9.2.jar /usr/local/tomcat/lib/
ADD lib/commons-collections-3.1.jar /usr/local/tomcat/lib/
ADD lib/commons-collections4-4.0.jar /usr/local/tomcat/lib/
ADD lib/commons-logging-1.2.jar /usr/local/tomcat/lib/
ADD inject.jsp /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080

CMD ["catalina.sh", "run"]

