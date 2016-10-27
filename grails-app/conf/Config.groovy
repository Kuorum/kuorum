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
//        "classpath:Config_${grails.util.Environment.current.name}.groovy"
//        "file:/home/iduetxe/kuorum/kuorum/grails-app/conf/${grails.util.Environment.current.name}_milestones.groovy"
//        Environment.current.name
//        "classpath:development_config.properties"
]

// log4j configuration

grails.logging.jul.usebridge = false
grails.serverURL = "http://127.0.0.1:8080/kuorum"

log4j = {
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d{yyyy MM dd HH:mm:ss,SSS} [%c] # %-5p %m  %n' )
        rollingFile name:'stacktrace', maxFileSize:"5MB", maxBackupIndex: 10, file:"${System.getProperty('catalina.home')}/logs/kuorum_stacktrace.log", 'append':true, threshold:org.apache.log4j.Level.ALL
    }

    info    'grails.app','org.kuorum', 'kuorum'

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
grails.resources.adhoc.patterns = ['/fonts/*', '/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/fonts/**', '/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.resourceLocatorEnabled=true
grails.resources.uriToUrlCacheTimeout = 30000

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

//SWITCH USER
grails.plugin.springsecurity.useSwitchUserFilter = true
grails.plugin.springsecurity.controllerAnnotations.staticRules=[
        '/j_spring_security_switch_user': ['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY']
]
grails.plugin.springsecurity.filterChain.chainMap = [  //LOS FILTROS SIN ESPACIOS
        '/j_spring_security_switch_user/**':'securityContextPersistenceFilter,logoutFilter,authenticationProcessingFilter,rememberMeAuthenticationFilter,anonymousAuthenticationFilter,exceptionTranslationFilter,filterInvocationInterceptor,logoutFilter,authenticationProcessingFilter,switchFilter,switchUserProcessingFilter',
        '/**':         'JOINED_FILTERS'
]
//FACEBOOK
grails.plugin.springsecurity.facebook.domain.classname='kuorum.users.FacebookUser'
grails.plugin.springsecurity.facebook.domain.appUserConnectionPropertyName='user'
grails.plugin.springsecurity.facebook.appId='OVERWRITE_FROM_EXTERNAL_CONFIG'
grails.plugin.springsecurity.facebook.secret='OVERWRITE_FROM_EXTERNAL_CONFIG'
//grails.plugin.springsecurity.facebook.permissions='email,user_about_me,user_birthday,user_education_history'
grails.plugin.springsecurity.facebook.permissions='email,user_about_me'
grails.plugin.springsecurity.facebook.autoCreate.roles='ROLE_USER'
grails.plugin.springsecurity.facebook.filter.type='redirect'
grails.plugin.springsecurity.facebook.filter.redirect.successHandler='facebookSuccessHandler'
//grails.plugins.springsecurity.facebook.filter.redirect.failureHandler = 'facebookHandler'
grails.plugin.springsecurity.rememberMe.alwaysRemember=true
grails.plugin.springsecurity.rememberMe.tokenValiditySeconds=60*60*24*365 //(one year)
grails.plugin.springsecurity.rememberMe.key='kuorumRememberMe'
grails.plugin.springsecurity.rememberMe.cookieName='kuorumSecurity_rememberMe'
grails.plugin.springsecurity.rememberMe.persistent=true
grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName='kuorum.web.users.PersistentLoginToken'
// Added by the Spring Security OAuth plugin:
grails.plugin.springsecurity.oauth.domainClass = 'kuorum.users.OAuthID'

grails.plugin.springsecurity.rememberMe.domain = ".kuorum.org"
grails.plugin.springsecurity.loginDomain = "https://kuorum.org"
grails.plugin.cookiesession.domain=".kuorum.org"
grails.plugin.cookiesession.springsecuritycompatibility=true
oauth {
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
        outlook {
            api = kuorum.payment.contact.outlook.oauth.OutlookApi
            key = 'OUTLOOK KEY'
            secret = 'OUTLOOK SECRET'
            successUri = '/oauth/outlook/success'
            failureUri = '/oauth/outlook/failure'
            callback = "http://localhost:8080/kuorum/oauth/outlook/callback"
            scope = 'openid offline_access profile https://outlook.office.com/contacts.read'
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

//Configuration of burning-image plugin
//bi.renderingEngine = RenderingEngine.IMAGE_MAGICK
//bi.imageMagickQuality = 100 //0 -100
//bi.imageMagickCompression= 50 //0 -100

kuorum {
    register{
        notAllowedTemporalDomainEmails=["@drdrb.net", "@sharklasers.com", "@guerrillamailblock.com", "@sharklasers.com", "@guerrillamail.net", "@guerrillamail.org", "@guerrillamail.biz", "@spam4.me", "@grr.la", "@guerrillamail.de", "@mailismagic.com", "@monumentmail.com", "@tradermail.info", "@mailtothis.com", "@reallymymail.com", "@devnullmail.com", "@mailinater.com", "@veryrealemail.com", "@letthemeatspam.com", "@fuck.trillianpro.com", "@must.resist.ratemysketa.com", "@cry-its.trillianpro.com", "@suck-ass.trillianpro.com", "@killspammers.cu.cc", "@go.trillianpro.com", "@unpentant.trillianpro.com", "@eyepaste.com", "@deadaddress.com", "@cuvox.de", "@armyspy.com","@cuvox.de","@dayrep.com", "@einrot.com", "@fleckens.hu", "@gustr.com", "@jourrapide.com", "@rhyta.com", "@superrito.com", "@teleworm.us", "@filzmail.com", "@incognitomail.org", "@mailcatch.com", "@mailme24.com", "@dunflimblag.mailexpire.com", "@mailnull.com", "@ce.mintemail.com", "@no-spam.ws", "@nowmymail.com", "@onewaymail.com", "@shitmail.org", "@crapmail.org", "@sofimail.com", "@uroid.com", "@kurzepost.de", "@objectmail.com", "@proxymail.eu", "@rcpt.at", "@trash-mail.at", "@trashmail.at", "@trashmail.com", "@trashmail.me", "@trashmail.net", "@wegwerfmail.de", "@wegwerfmail.net", "@wegwerfmail.org", "@yopmail.com", "@yopmail.fr", "@yopmail.net", "@jetable.fr.nf", "@nospam.ze.tc","@nomail.xl.cx", "@mega.zik.dj", "@speed.1s.fr", "@cool.fr.nf", "@courriel.fr.nf", "@moncourrier.fr.nf", "@monemail.fr.nf", "@monmail.fr.nf", "@mt2014.com", "@mt2015.com", "@mail.ru", "@taidar.ru", "@yandex.com","@sina.com"]
    }
    contact{
        email="info@kuorum.org"
        feedback="info@kuorum.org"
        workWithUs="empleo@kuorum.org"
    }
    purchase{
        email="info@kuorum.org"
        userName="Compra"
    }
    milestones{
        kuorum = 1000
        postVotes{
            ranges=[0..<5].withEagerDefault{index ->
                [5,10,25][(index-1)%3]*(10**((new Double((index-1)/3)).trunc())) ..<[5,10,25][(index)%3]*(10**((new Double(index/3)).trunc()))
            }//[[0..5),[5..10),[10..25),[25..50),[50..10),[100..250),[250..500),....]
            publicVotes=10 // Número de impulsos necesarios para que sea un post destacado + pluma + no editable
        }
    }
    upload{
        serverPath = "OVERWRITTEN_PROPERTIES"
        relativeUrlPath = "/uploadedImages"
    }
    promotion{
        mailPrice = 0.15
    }
    gamification{
        voteProject=[(GamificationElement.EGG):1]
        newPost=[(GamificationElement.PLUME):1]
        votePost=[(GamificationElement.CORN):1]
    }

    post{
        titleSize = 140
        promotionPrizes = [5,15,30] //Default euros amount promoting a post
        limitVotesToBeEditable = 10
        limitVotesToBeDeletable = 10
    }

    seo{
        maxElements = 1000 //El número de elementos que se van a mostrar en el mapa web para el scrapper
    }

    recommendedUser{
        regionValue = 150I
        organizationValue = 50I
        politicianValue = 50I
        debateValue = 100I
        victoryValue = 50I
    }

    amazon{
        accessKey = "XXXXX"
        secretKey = "XXXXX"
        bucketName = "kuorum.org"
    }
    rest{
        url = "http://api.test.kuorum.org"
        apiPath = "/api"
//        url = "http://localhost:8181"
//        apiPath = "/kuorumRest/api"
        apiKey = "XXXX"
    }
}

//Added for date bindings
grails.databinding.dateFormats = ["dd/MM/yyyy"]

