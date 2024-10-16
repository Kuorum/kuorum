import kuorum.util.log.GelfOVHAppender
import org.apache.log4j.net.SyslogAppender
import org.graylog2.GelfSender

//import grails.plugin.springsecurity.SpringSecurityUtils

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

grails.config.locations = [
//        "classpath:${grails.util.Environment.current.name}_config.properties"
        "classpath:Config_${grails.util.Environment.current.name}.groovy"
//        "file:/home/iduetxe/kuorum/kuorum/grails-app/conf/${grails.util.Environment.current.name}_milestones.groovy"
//        Environment.current.name
//        "classpath:development_config.properties"
]

// log4j configuration

grails.logging.jul.usebridge = false
grails.serverURL = "http://local.kuorum.org:8080/kuorum"

log4j = {
    appenders {
        appender new org.apache.log4j.ConsoleAppender(name:'stdout', layout:pattern(conversionPattern: '%d{yyyy MM dd HH:mm:ss,SSS} [%-5p][%X{userEmail}::%X{domain}][%c{1}] # %m  %n' ))
//        appender new GelfOVHAppender(
//                name: 'gelfAppender',
//                graylogHost: 'tcp:gra3.logs.ovh.com',
//                graylogPort: 2202,
//                extractStacktrace: true,
//                includeLocation: true,
//                addExtendedInformation: true,
//                facility:"TOMCAT",
//                additionalFields:"{'X-OVH-TOKEN':'56b1b7a7-0f5d-44cb-aeba-262701f9e460', 'component':'WEB', 'environment':'${grails.util.Environment.current.name}'}"
//        )
    }
    root{
        info 'stdout'
//        info 'stdout', 'gelfAppender'
    }
//    info gelfAppender: 'grails.app','org.kuorum', 'kuorum'
    info stdout: 'grails.app','org.kuorum', 'kuorum'
    info    'grails.app','org.kuorum', 'kuorum'

    debug   'kuorum.core.navigation.cache',
            'kuorum.core.customDomain.filter'

    error   'groovyx.net.http.RESTClient',        // Exception parsing WARN not visible
            'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate',
            'grails.app.taglib.org.grails.plugin.resource.ResourceTagLib',
            'grails.app.services.org.grails.plugin.resource',
            'grails.app.taglib.org.grails.plugin.resource',
            'grails.app.resourceMappers.org.grails.plugin.resource'
    off     'grails.app.services.grails.plugin.cookie.CookieService'
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
//grails.resources.mappers.baseurl.enabled=false
grails.resources.mappers.multidomain.enabled = false
grails.resources.mappers.multidomain.protocol = "https://"
grails.resources.mappers.multidomain.suffixDomain = "static-kuorum.org/kuorum/static"
grails.resources.mappers.multidomain.numberDomains = 5

grails.resources.mappers.amazoncdn.enabled = true
grails.resources.mappers.amazoncdn.host = "https://static.kuorum.org"
grails.resources.mappers.amazoncdn.accessKey = "NO_ACCESS_KEY"
grails.resources.mappers.amazoncdn.secretKey = "NO_SECRET_KEY"
grails.resources.mappers.amazoncdn.bucket = "static.kuorum.org"
grails.resources.mappers.amazoncdn.path = "/local/development"
grails.resources.mappers.baseurl.default = "${grails.resources.mappers.amazoncdn.host}${grails.resources.mappers.amazoncdn.path}"

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
//grails.plugin.springsecurity.userLookup.usernamePropertyName='email'
//grails.plugin.springsecurity.userLookup.userDomainClassName = 'kuorum.users.KuorumUser'
//grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'kuorum.users.KuorumUserRoleUser'
//grails.plugin.springsecurity.authority.className = 'kuorum.users.RoleUser'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/faviconKuorum.ico':                ['permitAll']
]

grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.password.hash.iterations = 1
//grails.plugin.springsecurity.dao.reflectionSaltSourceProperty='id'

grails.plugin.springsecurity.ui.encodePassword=true
grails.plugin.springsecurity.ui.password.minLength=4
grails.plugin.springsecurity.ui.password.maxLength=64
//grails.plugin.springsecurity.ui.password.validationRegex="^.*(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$"
grails.plugin.springsecurity.ui.password.validationRegex='^.*$'

grails.plugin.springsecurity.filterChain.filterNames = ['customDomainSpringFilter','evictResponseSpringFilter','securityContextPersistenceFilter','logoutFilter','authenticationProcessingFilter','rememberMeAuthenticationFilter','anonymousAuthenticationFilter','exceptionTranslationFilter','filterInvocationInterceptor','logoutFilter','authenticationProcessingFilter','switchFilter','switchUserProcessingFilter','kuorumLogSpringFilter', 'cacheResponseSpringFilter']
//SWITCH USER
grails.plugin.springsecurity.useSwitchUserFilter = true
grails.plugin.springsecurity.controllerAnnotations.staticRules=[
        '/j_spring_security_switch_user': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_FULLY']
]
grails.plugin.springsecurity.filterChain.chainMap = [  //LOS FILTROS SIN ESPACIOS
        '/error-page/**':'none',
        '/ping':'pingSpringFilter',
        '/ping/**':'pingSpringFilter',
        '/j_spring_security_switch_user/**':'JOINED_FILTERS',
        '/j_spring_security_exit_user/**':'JOINED_FILTERS',
        '/**':         'JOINED_FILTERS,-switchFilter,-switchUserProcessingFilter'
]
//FACEBOOK
grails.plugin.springsecurity.rememberMe.alwaysRemember=true
grails.plugin.springsecurity.rememberMe.tokenValiditySeconds=60*60*24*365 //(one year)
grails.plugin.springsecurity.rememberMe.key='kuorumRememberMe'
grails.plugin.springsecurity.rememberMe.cookieName='kuorumSecurity_rememberMe'
grails.plugin.springsecurity.rememberMe.persistent=true
grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName='kuorum.web.users.PersistentLoginToken'
//grails.plugin.springsecurity.rememberMe.useSecureCookie=true

// Added by the Spring Security OAuth plugin:
//grails.plugin.springsecurity.oauth.domainClass = 'kuorum.users.OAuthID'
//grails.plugin.springsecurity.successHandler.defaultTargetUrl='/dashboard'
//grails.plugin.springsecurity.rememberMe.domain = ".kuorum.org"
//grails.plugin.springsecurity.loginDomain = "https://kuorum.org"
//grails.plugin.cookiesession.domain=".kuorum.org"
//grails.plugin.cookiesession.springsecuritycompatibility=true

// Next 10 lines are for force grails to use always SSL
//grails.plugin.springsecurity.secureChannel.useHeaderCheckChannelSecurity = true
//grails.plugin.springsecurity.portMapper.httpPort = 80
//grails.plugin.springsecurity.portMapper.httpsPort = 443
//grails.plugin.springsecurity.secureChannel.secureHeaderName = 'X-Forwarded-Proto'
//grails.plugin.springsecurity.secureChannel.secureHeaderValue = 'http'
//grails.plugin.springsecurity.secureChannel.insecureHeaderName = 'X-Forwarded-Proto'
//grails.plugin.springsecurity.secureChannel.insecureHeaderValue = 'https'
//grails.plugin.springsecurity.auth.forceHttps = true


// CONFIG FOR SESSION IN MEMCACHE
grails.plugin.standalone.tomcat.memcached.enabled = true
grails.plugin.standalone.tomcat.memcached.enableStatistics = true
grails.plugin.standalone.tomcat.memcached.sessionBackupAsync = false
grails.plugin.standalone.tomcat.memcached.sticky = false
//grails.plugin.standalone.tomcat.memcached.memcachedProtocol = "binary"
grails.plugin.standalone.tomcat.memcached.sessionBackupTimeout = 100
grails.plugin.standalone.tomcat.memcached.operationTimeout = 1000
//grails.plugin.standalone.tomcat.memcached.backupThreadCount = 10
//grails.plugin.standalone.tomcat.memcached.copyCollectionsForSerialization = false
//grails.plugin.standalone.tomcat.memcached.username = user
//grails.plugin.standalone.tomcat.memcached.password = pass
//grails.plugin.standalone.tomcat.memcached.failoverNodes = pass
//grails.plugin.standalone.tomcat.memcached.lockingMode = false
//grails.plugin.standalone.tomcat.memcached.requestUriIgnorePattern = ????
//grails.plugin.standalone.tomcat.memcached.sessionAttributeFilter = ????
//grails.plugin.standalone.tomcat.memcached.transcoderFactoryClass = ????
//grails.plugin.standalone.tomcat.memcached.customConverter = ????
grails.plugin.standalone.tomcat.memcached.memcachedNodes = "127.0.0.1:11211"


cache.headers.presets = [
        authed_page: false, // No caching for logged in user
        landings: [shared:true, validFor: 3600 *24 ], // 1day on landings
        blog: [shared: true, validUntil:new Date()+1],
        search_results: [validFor: 60, shared: true]
]
oauth {
    debug = true
    providers {
        kuorumApi {
            api = kuorum.payment.contact.kuorumApi.oauth.KuorumApiOauth2
            key = 'KUORUM KEY'
            secret = 'KUORUM SECRET'
            successUri = '/oauth/api/success'
            failureUri = '/oauth/api/failure'
            callback = "http://localhost:8080/kuorum/oauth/api/callback" // Is overwritten with properties file
            scope = 'SCOPES'
        }
//        facebook {
//            api = kuorum.payment.contact.facebook.oauth.FacebookApiOauth2
//            key = 'FACEBOOK KEY'
//            secret = 'FACEBOOK SECRET'
//            successUri = '/oauth/facebook/success'
//            failureUri = '/oauth/facebook/failure'
//            callback = "http://localhost:8080/kuorum/oauth/facebook/callback" // Is overwritten with properties file
//            scope = 'email public_profile user_friends user_about_me'
//        }
        // for Google OAuth 2.0
//        google {
//            api = org.grails.plugin.springsecurity.oauth.GoogleApi20
//            key = 'GOOGLE KEY'
//            secret = 'GOOGLE SECRET'
//            successUri = '/oauth/google/success'
//            failureUri = '/oauth/google/failure'
//            callback = "http://localhost:8080/kuorum/oauth/google/callback" // Is overwritten with properties file
////            scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
//            scope = 'https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.profiles.read https://www.googleapis.com/auth/plus.circles.read'
//            jsKey = 'GOOGLE JS KEY'
//        }
//        outlook {
//            api = kuorum.payment.contact.outlook.oauth.OutlookApi
//            key = 'OUTLOOK KEY'
//            secret = 'OUTLOOK SECRET'
//            successUri = '/account/contacts/oauth/outlook/success'
//            failureUri = '/account/contacts/oauth/outlook/success'
//            callback = "http://localhost:8080/kuorum/oauth/outlook/callback"
//            contactCallback = "http://local.kuorum.org/kuorum/googleContacts/loadContactsFromGoogle"
//            scope = 'openid offline_access profile https://outlook.office.com/contacts.read'
//        }
//        yahoo {
//            api = kuorum.payment.contact.yahoo.oauth.YahooApi
////            api = org.scribe.builder.api.YahooApi
//            key = 'YAHOO KEY'
//            secret = 'YAHOO SECRET'
//            successUri = '/account/contacts/oauth/yahoo/success'
//            failureUri = '/account/contacts/oauth/yahoo/success'
//            callback = "http://local.kuorum.org:8080/kuorum/account/contacts/oauth/yahoo/success" // Is overwritten with properties file
//            scope = 'mail-r sdct-r' // Mail y contactos
//        }
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
        notAllowedTemporalDomainEmails=["@drdrb.net", "@sharklasers.com", "@guerrillamailblock.com", "@sharklasers.com", "@guerrillamail.net", "@guerrillamail.org", "@guerrillamail.biz", "@spam4.me", "@grr.la", "@guerrillamail.de", "@mailismagic.com", "@monumentmail.com", "@tradermail.info", "@mailtothis.com", "@reallymymail.com", "@devnullmail.com", "@mailinater.com", "@veryrealemail.com", "@letthemeatspam.com", "@fuck.trillianpro.com", "@must.resist.ratemysketa.com", "@cry-its.trillianpro.com", "@suck-ass.trillianpro.com", "@killspammers.cu.cc", "@go.trillianpro.com", "@unpentant.trillianpro.com", "@eyepaste.com", "@deadaddress.com", "@cuvox.de", "@armyspy.com","@cuvox.de","@dayrep.com", "@einrot.com", "@fleckens.hu", "@gustr.com", "@jourrapide.com", "@rhyta.com", "@superrito.com", "@teleworm.us", "@filzmail.com", "@incognitomail.org", "@mailcatch.com", "@mailme24.com", "@dunflimblag.mailexpire.com", "@mailnull.com", "@ce.mintemail.com", "@no-spam.ws", "@nowmymail.com", "@onewaymail.com", "@shitmail.org", "@crapmail.org", "@sofimail.com", "@uroid.com", "@kurzepost.de", "@objectmail.com", "@proxymail.eu", "@rcpt.at", "@trash-mail.at", "@trashmail.at", "@trashmail.com", "@trashmail.me", "@trashmail.net", "@wegwerfmail.de", "@wegwerfmail.net", "@wegwerfmail.org", "@yopmail.com", "@yopmail.fr", "@yopmail.net", "@jetable.fr.nf", "@nospam.ze.tc","@nomail.xl.cx", "@mega.zik.dj", "@speed.1s.fr", "@cool.fr.nf", "@courriel.fr.nf", "@moncourrier.fr.nf", "@monemail.fr.nf", "@monmail.fr.nf", "@mt2014.com", "@mt2015.com", "@mail.ru", "@taidar.ru", "@yandex.com","@mohmal.net","@mohmal.com","@mohmal.es","@mohmal.im", "@mailbox92.biz", "@mailbox80.biz", "@yopmail.com", "@zetmail.com", "@mailinator.com", "@spambooger.com","@notmailinator.com", "@spamherelots.com", "@devnullmail.com", "@veryrealemail.com", "@bobmail.info", "@safetymail.info", "@spamthisplease.com", "@sogetthis.com", "@suremail.info", "@mailinater.com", "@spamherelots.com", "@zippymail.info", "@letthemeatspam.com", "@mailinator2.com", "@binkmail.com", "@spamhereplease.com","@mailinator.net","@mailinater.net", "@streetwisemail.com","@chammy.info", "@reconmail.com", "@yroid.com", "@throwam.com","@web-mail.pp.ua","@loh.pp.ua","@ip4.pp.ua","@add3000.pp.ua","@mox.pp.ua","@get.pp.ua","@fake-email.pp.ua","@jetable.pp.ua","@eml.pp.ua","@ass.pp.ua","@yopmail.pp.ua","@stop-my-spam.pp.ua","@ip6.pp.ua","@bgtmail.com","@ggg.pp.ua","@1.fackme.gq","@four.fackme.gq","@bugmenot.ml","@pig.pp.ua","@yop.ze.cx","@pulpmail.us","@bbhost.us","@googelmail.ml","@boatmail.us","@pulsa-gratis-100.ml","@holl.ga","@hash.pp.ua","@emailfake.ml","@yopmail.biz.st","@googlemial.ml","@xost.us","@tool.pp.ua","@emo06.tk","@fakeinbox.com","@zain.site","@maileme101.com","@9me.site","@dr69.site","@zainmax.net","@stromox.com"]
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
    upload{
        serverPath = "OVERWRITTEN_PROPERTIES"
        relativeUrlPath = "/uploadedImages"
    }
    promotion{
        mailPrice = 0.15
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
        accessKey = "NO_ACCESS_KEY"
        secretKey = "NO_SECRET_KEY"
        bucketName = "kuorum.org"
        bucketRegion = "eu-west-1"
    }
    rest{
        url = "http://api.test.kuorum.org"
        apiPath = "/api"
//        url = "http://localhost:8181"
//        apiPath = "/kuorumRest/api"
        apiKey = "XXXX"
    }

    keys{
        google{
            api{
                js ="xxxxx"
            }
        }
        favicon = "XXXXXXX"
    }
}

//Added for date bindings
grails.databinding.dateFormats = ["dd/MM/yyyy"]

// TODO: Do correct cache [WARN WITH VIEWER - UUID]
grails.cache.enabled = true
grails.cache.config = {
//    cacheManager 'GrailsConcurrentLinkedMapCacheManager'
    cache {
        name 'reputation'
        maxCapacity = 5000
        eternal false
        overflowToDisk false
        maxElementsInMemory 1000
//        maxElementsOnDisk 10000000
    }
    cache {
        name 'debate'
        maxCapacity = 100
        eternal false
        overflowToDisk false
        maxElementsInMemory 100
    }

    cache {
        name 'proposals'
        maxCapacity = 100
        eternal false
        overflowToDisk false
        maxElementsInMemory 100
    }
}