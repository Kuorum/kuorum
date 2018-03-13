import grails.util.Environment

grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    //development : [test: false,run: false],
    development : false,
    // configure settings for the test-app JVM, uses the daemon by default
    //test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    test:false ,
    // configure settings for the run-app JVM
    //run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    run: false,
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

def seleniumVersion = "2.35.0"
def gebVersion = "0.9.2"

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()

        //Repository for kuorum.springSecurity
        mavenRepo "http://repo.spring.io/milestone/"
        mavenRepo "https://repository.jboss.org/maven2/"

        //Repository for google spring social template
//        mavenRepo "http://gabiaxel.github.io/maven/"


        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        // runtime 'mysql:mysql-connector-java:5.1.27'
        // runtime 'org.postgresql:postgresql:9.3-1100-jdbc41'

        //compile('org.springframework.security:org.springframework.security.core:3.0.2.RELEASE')
        //compile('org.springframework.security:org.springframework.security.web:3.0.2.RELEASE')
        //compile 'org.springframework.social:spring-social-core:1.0.1.RELEASE'
//        compile 'org.springframework.social:spring-social-core:1.0.3.RELEASE'
//        compile ('org.springframework.social:spring-social-google:1.0.0.M4')
//        compile ('org.springframework.social:spring-social-facebook:1.0.3.RELEASE'){
        compile ('org.springframework.social:spring-social-facebook:2.0.3.RELEASE'){
            //CONFLICT WITH grails json parsers
//            excludes 'org.codehaus.jackson:jackson-mapper-asl'
//            excludes 'com.fasterxml.jackson.core:jackson-databind'
        }
        // https://mvnrepository.com/artifact/com.google.api.client/google-api-client-googleapis
//        compile group: 'com.google.api.client', name: 'google-api-client-googleapis', version: '1.4.1-beta'
//        compile group: 'com.google.api.client', name: 'google-api-client-googleapis-auth-oauth', version: '1.2.3-alpha'
        compile 'com.google.api-client:google-api-client:1.22.0'
//        compile 'com.google.oauth-client:google-oauth-client-jetty:1.22.0'
        compile 'com.google.apis:google-api-services-people:v1-rev4-1.22.0'
        compile group: 'com.google.gdata', name: 'core', version: '1.47.1'
//        compile 'com.google.apis:google-api-services-plus:v1-rev461-1.22.0'
        compile group: 'com.google.apis', name: 'google-api-services-plusDomains', version: 'v1-rev7-1.17.0-rc'

        // The same version as spring-social:facebook
        compile group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.6.2'

        compile 'com.mandrillapp.wrapper.lutung:lutung:0.0.4'
//        compile 'org.apache.solr:solr-solrj:4.9.0'
//        test ('org.apache.solr:solr-core:4.9.0'){
//            excludes "org.slf4j:slf4j-log4j12"
//        }
	    compile "net.sf.ehcache:ehcache-core:2.4.6" // Para eliminar :cache:1.1.1 que da un problema de dependencia al quitar hibernate

        compile 'com.xlson.groovycsv:groovycsv:1.0' //Para poder leer CSV
        compile ('com.amazonaws:aws-java-sdk:1.10.20'){ //Amazon S3
            excludes "org.apache.httpcomponents:httpclient"
            excludes "joda-time:joda-time"
        }
        compile 'joda-time:joda-time:2.9.1' // Actualizacion de libreria para amazon
        compile 'org.apache.httpcomponents:httpclient:4.5.2' //Actualizacion librer�a para Amazon
        compile 'org.apache.httpcomponents:httpcore:4.4.4' //Actualizacion librer�a para Amazon

        compile ('org.codehaus.groovy:groovy-xmlrpc:jar:0.7'){
            excludes "jivesoftware:smack"
        }
        compile 'org.igniterealtime.smack:smack:3.1.0'
        compile 'com.ecwid:ecwid-mailchimp:jar:2.0.0.1'

        compile 'kuorumServices:kuorumRestModel:jar:1.0-SNAPSHOT'

        //For burning-image plugin
//        compile 'org.im4java:im4java:1.4.0'
        //        compile 'javax.media:jai_core:1.1.2_01'

        compile 'org.grails:grails-datastore-gorm:3.1.0.RELEASE'
        compile 'org.grails:grails-datastore-core:3.1.0.RELEASE'
        test 'org.grails:grails-datastore-simple:3.1.0.RELEASE'
       compile 'org.codehaus.groovy.modules.http-builder:http-builder:0.7.1'

        // Library for detect the charset of the uploaded file
        compile group: 'com.googlecode.juniversalchardet', name: 'juniversalchardet', version: '1.0.3'

        // Library for clean html
        compile group: 'org.jsoup', name: 'jsoup', version: '1.10.1'

        // Email validator need new TLDs (i.e: .city)
        compile group: 'commons-validator', name: 'commons-validator', version: '1.5.1'

//        compile group: 'org.graylog2', name: 'gelfj', version: '1.1.13'



//        test "org.gebish:geb-spock:$gebVersion"
//        test("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
//        test("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
//        test("org.seleniumhq.selenium:selenium-support:$seleniumVersion") {
//            exclude "xml-apis"
//        }
    }

    plugins {
        // plugins for the build system only
        build ':tomcat:7.0.52.1'
//        compile ":grails-melody:1.56.0"

        // plugins for the compile step
        compile ':scaffolding:2.1.0'
        //compile ':cache:1.1.1'

//        compile ":mongodb:1.3.3"
        compile ":mongodb:3.0.1"
        // plugins needed at runtime but not for compilation
        //runtime ":hibernate:3.6.10.7" // or ":hibernate4:4.1.11.6"
        // runtime ":database-migration:1.3.8"

        compile ':cookie:1.2'
        compile ":spring-security-core:2.0-RC4"
        compile ':spring-security-oauth:2.1.0-RC4'
        compile ':spring-security-oauth-google:0.2'
        compile ':spring-security-oauth-facebook:0.2'

        //compile ":spring-security-acl:2.0-RC1"
        compile "org.grails.plugins:spring-security-acl:2.0.1" //Para las partes que tienen seguridad custom
        compile ":spring-security-ui:1.0-RC3"
        //compile ":spring-security-core:1.2.7.3"

//        compile ":rest-client-builder:2.0.1"
        //Para las peticiones a la API de mandrillapp
        compile ":rest:0.8"
//        compile ":restrpc:0.9"

//        compile ":executor:0.3"

        //Images and uploading files
        compile (":ajax-uploader:1.3")
        compile ":burning-image:0.5.2"


        compile "org.grails.plugins:cache:1.1.8"
//        runtime ":jquery:1.11.1"
        compile ":cache-headers:1.1.7"
//        compile "org.grails.plugins:asset-pipeline:2.11.0"
        runtime ":resources:1.2.14"
        // Uncomment these (or add new ones) to enable additional resources capabilities
        runtime ":zipped-resources:1.0"
        runtime ":cached-resources:1.1"

        //test ':build-test-data:2.1.1'
//        compile ':fixtures:1.2'
//        test ":geb:$gebVersion"
//        test ":code-coverage:1.2.7"
//        test ":codenarc:0.20"

//        compile ":quartz:1.0.2"
    }
}

//coverage { xml=true }

codenarc.reports = {

    MyXmlReport('xml') { // The report name "MyXmlReport" is user-defined; Report type is 'xml'
        outputFile = 'test-reports/CodeNarcReport.xml' // Set the 'outputFile' property of the (XML) Report
        title = 'XmlReport' // Set the 'title' property of the (XML) Report
    }
    MyHtmlReport('html') { // Report type is 'html'
        outputFile = 'test-reports/CodeNarcReport.html'
        title = 'HtmlReport'
    }
}

grails.war.resources = { stagingDir, args ->
//    copy(file: ".ebextensions/HTTPtoHTTPS.config",
//            tofile: "${stagingDir}/.ebextensions/HTTPtoHTTPS.config")
    copy(file: ".ebextensions/installJava8.config",
            tofile: "${stagingDir}/.ebextensions/installJava8.config")

    // HTTP Auth
    if (System.getProperty("grails.env") == "preproduction") {
        copy(file: ".ebextensions/HttpAuth.config",
                tofile: "${stagingDir}/.ebextensions/HttpAuth.config")
        copy(file: ".ebextensions/tomcat-users.xml",
                tofile: "${stagingDir}/.ebextensions/tomcat-users.xml")
        copy(file: ".ebextensions/web.xml",
                tofile: "${stagingDir}/.ebextensions/web.xml")
    }

    delete { fileset(dir: "${stagingDir}", includes: '*.html') }
}
