import kuorum.core.model.gamification.GamificationElement

//import grails.plugin.springsecurity.SpringSecurityUtils

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

grails.config.locations = [
        "classpath:${grails.util.Environment.current.name}_config.properties"
//        "file:/home/iduetxe/kuorum/kuorum/grails-app/conf/${grails.util.Environment.current.name}_milestones.groovy"
//        Environment.current.name
//        "classpath:development_config.properties"
]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

grails.gorm.default.constraints = {
//    '*'(nullable: true)
//    myShared(nullable: false, blank: false)
    birthdayDay()
}

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
 
grails.converters.encoding = "UTF-8"
grails.converters.default.circular.reference.behaviour="INSERT_NULL"
grails.converters.json.default.deep=true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.serverURL = "http://127.0.0.1:8080/kuorum"
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://kuorum.org" // This property is overwritten by production_config.properties
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
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


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.usernamePropertyName='email'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'kuorum.users.KuorumUser'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'kuorum.users.KuorumUserRoleUser'
grails.plugin.springsecurity.authority.className = 'kuorum.users.RoleUser'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll']
]

grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.password.hash.iterations = 1
grails.plugin.springsecurity.dao.reflectionSaltSourceProperty='id'

grails.plugin.springsecurity.ui.encodePassword=true
grails.plugin.springsecurity.ui.password.minLength=4
grails.plugin.springsecurity.ui.password.maxLength=64
//grails.plugin.springsecurity.ui.password.validationRegex="^.*(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$"
grails.plugin.springsecurity.ui.password.validationRegex='^.*$'
//FACEBOOK
grails.plugin.springsecurity.facebook.domain.classname='kuorum.users.FacebookUser'
grails.plugin.springsecurity.facebook.domain.appUserConnectionPropertyName='user'
grails.plugin.springsecurity.facebook.appId='OVERWRITE_FROM_EXTERNAL_CONFIG'
grails.plugin.springsecurity.facebook.secret='OVERWRITE_FROM_EXTERNAL_CONFIG'
grails.plugin.springsecurity.facebook.permissions='email,user_about_me,user_birthday,user_education_history'
grails.plugin.springsecurity.facebook.autoCreate.roles='ROLE_USER'
grails.plugin.springsecurity.facebook.filter.type='redirect'
//grails.plugins.springsecurity.facebook.filter.redirect.failureHandler = 'facebookHandler'
grails.plugin.springsecurity.rememberMe.alwaysRemember=true
grails.plugin.springsecurity.rememberMe.tokenValiditySeconds=60*60*24*365 //(one year)
grails.plugin.springsecurity.rememberMe.key='kuorumRememberMe'
grails.plugin.springsecurity.rememberMe.cookieName='kuorumSecurity_rememberMe'
//grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName=''
// Added by the Spring Security OAuth plugin:
grails.plugin.springsecurity.oauth.domainClass = 'kuorum.users.OAuthID'
oauth {
    // ...
    providers {
        // for Google OAuth 2.0
        google {
            api = org.grails.plugin.springsecurity.oauth.GoogleApi20
            key = 'GOOGLE KEY'
            secret = 'GOOGLE SECRET'
            successUri = '/oauth/google/success'
            failureUri = '/oauth/google/failure'
            callback = "http://localhost:8080/kuorum/oauth/google/callback" // Is overwritten with properties file
            scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
        }
        // ...
    }
}

// Added by the Restrpc plugin:
restrpc.apiName = 'api'
restrpc.apiVersion = '1.0'

 /*
grails {
    kuorum.mail {
        host = "smtp.mandrillapp.com"
        port = 587
        username = "inaki.dominguez@kuorum.org"
        password = "XXXXXXXXXXXxxxx"
        props = ["kuorum.mail.smtp.starttls.enable":"true",
                "kuorum.mail.smtp.port":"587"]
    }
}

*/


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


kuorum {
    contact{
        email="info@kuorum.org"
    }
    milestones{
        kuorum = 100
        postVotes{
            ranges=[0..<5].withEagerDefault{index ->
                [5,10,25][(index-1)%3]*(10**((new Double((index-1)/3)).trunc())) ..<[5,10,25][(index)%3]*(10**((new Double(index/3)).trunc()))
            }//[[0..5),[5..10),[10..25),[25..50),[50..10),[100..250),[250..500),....]
            publicVotes=10 // Número de impulsos necesarios para que sea un post destacado + pluma + no editable
        }
    }
    upload{
        serverPath = "OVERWRITTEN BY PROPERTIES"
        relativeUrlPath = "/uploadedImages"
    }
    promotion{
        mailPrice = 0.15
    }
    gamification{
        voteLaw=[(GamificationElement.EGG):1]
        newPost=[(GamificationElement.PLUME):1]
        votePost=[(GamificationElement.CORN):1]
    }

    post{
        titleSize = 140
        promotionPrizes = [5,15,30] //Default euros amount promoting a post
    }

    seo{
        maxElements = 1000 //El número de elementos que se van a mostrar en el mapa web para el scrapper
    }
}

