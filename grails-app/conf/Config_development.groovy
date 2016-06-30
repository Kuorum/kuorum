// log4j configuration

grails.logging.jul.usebridge = true
grails.serverURL = "http://127.0.0.1:8080/kuorum"

log4j = {
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d{yyyy MM dd HH:mm:ss,SSS} [%c] # %-5p %m  %n' )
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

grails.plugin.cookiesession.domain="127.0.0.1"
