// log4j configuration

grails.logging.jul.usebridge = false
grails.serverURL = "https://kuorum.org"

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


grails {
    mail {
        host = "bo51.e-goi.com"
        port = 1587
        username = "XXXXXXXXXXX"
        password = "XXXXXXXX"
        props =["kuorum.mail.smtp.auth":"true",
                "kuorum.mail.smtp.debug":"true",
                "kuorum.mail.smtp.starttls.enable":"true",
                "kuorum.mail.smtp.socketFactory.port":"1587",
                "kuorum.mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
//                "kuorum.mail.smtp.starttls.required" : 'true',
                //"kuorum.mail.smtp.socketFactory.fallback":"false"
        ]

        //["kuorum.mail.smtp.starttls.enable":"true",
        //"kuorum.mail.smtp.port":"1587"]
    }
}
