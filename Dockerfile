FROM davidcaste/alpine-tomcat:jdk8tomcat8
RUN rm -rf /opt/tomcat/webapps/*
ARG VERSION=V-8.4.0
ENV STATICS_VERSION_PATH=/$VERSION
COPY ./ROOT.war /opt/tomcat/webapps/ROOT.war
RUN mkdir /opt/tomcat/keystore
RUN keytool  -genkey -noprompt -trustcacerts -keyalg RSA -alias tomcat -dname  "CN=Inaki, OU=Kuorum, O=Kuorum, L=Madrid, ST=Madrid, C=IN" -keypass changeme -keystore "$CATALINA_HOME/keystore/my_keystore" -storepass changeme

COPY ./docker/context.xml /opt/tomcat/conf/context.xml
COPY ./docker/server.xml /opt/tomcat/conf/server.xml
COPY ./docker/setenv.sh /opt/tomcat/bin/setenv.sh
COPY ./docker/tomcatLib/* /opt/tomcat/lib/
CMD ["/opt/tomcat/bin/catalina.sh","run"]
