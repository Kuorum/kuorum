FROM davidcaste/alpine-tomcat:jdk8tomcat8
RUN rm -rf /opt/tomcat/webapps/*
ARG VERSION=V-8.4.0
ENV STATICS_VERSION_PATH=/$VERSION
COPY ./ROOT.war /opt/tomcat/webapps/ROOT.war
COPY ./docker/context.xml /opt/tomcat/conf/context.xml
COPY ./docker/server.xml /opt/tomcat/conf/server.xml
COPY ./docker/setenv.sh /opt/tomcat/bin/setenv.sh
COPY ./docker/tomcatLib/* /opt/tomcat/lib/
CMD ["/opt/tomcat/bin/catalina.sh","run"]
