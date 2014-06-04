// log4j configuration

grails.logging.jul.usebridge = false
grails.serverURL = "http://kuorum.org"

log4j = {
    appenders {
        console name:'stdout',
                layout:pattern(conversionPattern: '%d{yyyy MM dd HH:mm:ss,SSS} [%c] # %-5p %m  %n' )

        rollingFile name:'file',
                maxFileSize:10240,
                layout:pattern(conversionPattern: '%d{yyyy MM dd HH:mm:ss,SSS} [%c] # %-5p %m  %n' ),
                file:"${logFilePath}/kuorum.log"
    }

    info    'grails.app'

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'
    root {
        info 'file'
        additivity:true
    }
}
