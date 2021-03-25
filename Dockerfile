FROM davidcaste/alpine-tomcat:jdk8tomcat8
RUN rm -rf /opt/tomcat/webapps/*
RUN echo "export JAVA_OPTS=\"-Dfile.encoding=UTF-8 -Xms128m -Xmx2048m -XX:PermSize=64m -XX:MaxPermSize=2048m -Djava.security.egd=file:/dev/./urandom\"" > /opt/tomcat/bin/setenv.sh
ARG VERSION=V-X.X.X
ENV STATICS_VERSION_PATH=/$VERSION
COPY ./ROOT.war /opt/tomcat/webapps/ROOT.war
CMD ["/opt/tomcat/bin/catalina.sh","run"]
