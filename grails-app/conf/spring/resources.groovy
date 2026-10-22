import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.util.Environment
import kuorum.core.customDomain.filter.*
import kuorum.core.navigation.cache.MemcachedRequestResponseCacheImpl
import kuorum.core.springSecurity.handlers.SuccessAuthenticationHandler
import kuorum.files.AmazonFileService
import kuorum.register.MongoUserDetailsService
import kuorum.security.permission.KuorumPermissionEvaluator
import kuorum.security.rememberMe.GracePeriodRememberMeServices
import kuorum.security.rememberMe.RememberMeTokenRepository
import kuorum.web.constants.WebConstants
import net.spy.memcached.AddrUtil
import net.spy.memcached.ConnectionFactoryBuilder
import net.spy.memcached.MemcachedClient
import net.spy.memcached.transcoders.SerializingTranscoder

// Place your Spring DSL code here
beans = {
    xmlns aop: "http://www.springframework.org/schema/aop"
    languajeAspectBean(kuorum.core.aop.LanguageInjectorAOP)
    funnelLoginSesionBean(kuorum.core.aop.FunnelLoginSessionAOP){
        cookieUUIDService = ref("cookieUUIDService")
        springSecurityService = ref("springSecurityService")
    }

    Boolean cdnActive =
            application.config.grails.resources.mappers.amazoncdn.enabled instanceof Boolean ? application.config.grails.resources.mappers.amazoncdn.enabled :
                    application.config.grails.resources.mappers.amazoncdn.enabled instanceof String ? Boolean.parseBoolean(application.config.grails.resources.mappers.amazoncdn.enabled) : false
    if (cdnActive) {
        cdnAspectBean(kuorum.core.aop.CDNBaseUrlInjectorAOP)
    }

    aop.config("proxy-target-class": true) {
    }

    formattedDoubleConverter kuorum.web.binder.FormattedDoubleConverter

    userDetailsService(MongoUserDetailsService) {
        kuorumUserService = ref("kuorumUserService")
    }


    localeResolver(org.springframework.web.servlet.i18n.CookieLocaleResolver) {}

    localeChangeInterceptor(kuorum.web.interceptors.CustomLocaleInterceptor) {
        paramName = WebConstants.WEB_PARAM_LANG
    }

    tokenRepository(RememberMeTokenRepository) {
        grailsApplication = ref("grailsApplication")
    }

    // Overrides the rememberMeServices bean that the spring-security-core plugin registers
    // automatically (PersistentTokenBasedRememberMeServices, because of
    // rememberMe.persistent=true in Config.groovy) with a version that tolerates a single
    // reuse of the token immediately preceding the last rotation, within a short grace
    // period -- see GracePeriodRememberMeServices and RememberMeTokenRepository for the
    // details and reasons (race condition with several ECS tasks behind the ALB).
    // The properties set here mirror the ones the plugin sets on its own bean.
    def secConf = SpringSecurityUtils.securityConfig
    rememberMeServices(GracePeriodRememberMeServices) {
        rememberMeTokenRepository = ref('tokenRepository')
        tokenRepository = ref('tokenRepository')
        userDetailsService = ref('userDetailsService')
        key = secConf.rememberMe.key
        cookieName = secConf.rememberMe.cookieName
        alwaysRemember = secConf.rememberMe.alwaysRemember
        tokenValiditySeconds = secConf.rememberMe.tokenValiditySeconds
        parameter = secConf.rememberMe.parameter
        // Same as the plugin: only set when defined; otherwise Spring uses request.isSecure()
        if (secConf.rememberMe.useSecureCookie instanceof Boolean) {
            useSecureCookie = secConf.rememberMe.useSecureCookie
        }
        authenticationDetailsSource = ref('authenticationDetailsSource')
        userDetailsChecker = ref('userDetailsChecker')
        authoritiesMapper = ref('authoritiesMapper')
        seriesLength = secConf.rememberMe.persistentToken.seriesLength
        tokenLength = secConf.rememberMe.persistentToken.tokenLength
    }

    switch (Environment.current) {
//        case Environment.PRODUCTION:
//            fileService(AmazonFileService){
//                grailsApplication=ref("grailsApplication")
//                burningImageService=ref("burningImageService")
//                restKuorumApiService=ref("restKuorumApiService")
//            }
//            break

        default:
//            fileService(LocalFileService) {
//                grailsApplication=grailsApplication
//                burningImageService=burningImageService
//            }
            fileService(AmazonFileService) {
                grailsApplication = ref("grailsApplication")
                burningImageService = ref("burningImageService")
                restKuorumApiService = ref("restKuorumApiService")
                springSecurityService = ref("springSecurityService")
            }
            break
    }

    // ENCODER PASSWORD WITH BCRYPT
    def conf = SpringSecurityUtils.securityConfig
    passwordEncoder(BCryptPasswordEncoder, conf.password.bcrypt.logrounds)


    //SWICH USER

    switchFilter(kuorum.web.filters.KuorumSecuritySwitchFilter) {
        authenticationManager = ref('authenticationManager')
//        interchangeAuthenticationProvider = ref('interchangeAuthenticationProvider')
        securityMetadataSource = ref('objectDefinitionSource')
        springSecurityService = ref('springSecurityService')
    }

    facebookSuccessHandler(SuccessAuthenticationHandler) {
        kuorumUserService = ref('kuorumUserService')
    }


    // ACL
    permissionEvaluator(KuorumPermissionEvaluator) {
        grailsApplication = ref('grailsApplication')
        springSecurityService = ref('springSecurityService')
    }

    //Ping
    pingSpringFilter(PingSpringFilter) {

    }

    //CACHE
    servletResponseCache(MemcachedRequestResponseCacheImpl) {
        memcachedClient = new MemcachedClient(new ConnectionFactoryBuilder()
                .setTranscoder(new SerializingTranscoder())
                .setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                .build(), AddrUtil.getAddresses(application.config.kuorum.memcache.host))
    }

    //CACHE FILTER
    cacheResponseSpringFilter(CacheResponseSpringFilter) {
        urlMappingsHolder = ref('grailsUrlMappingsHolder')
        servletResponseCache = ref('servletResponseCache')
        springSecurityService = ref('springSecurityService')
    }

    evictResponseSpringFilter(EvictResponseSpringFilter) {
        urlMappingsHolder = ref('grailsUrlMappingsHolder')
        servletResponseCache = ref('servletResponseCache')
    }

    // DOMAIN
    customDomainSpringFilter(CustomDomainSpringFilter) {
        domainService = ref('domainService')
    }
    // MDC LOGS
    kuorumLogSpringFilter(KuorumLogSpringFilter) {
        springSecurityService = ref('springSecurityService')
    }
}
