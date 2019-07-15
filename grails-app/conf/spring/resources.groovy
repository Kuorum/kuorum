import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.util.Environment
import kuorum.core.customDomain.filter.CustomDomainSpringFilter
import kuorum.core.springSecurity.handlers.SuccessAuthenticationHandler
import kuorum.files.AmazonFileService
import kuorum.register.MongoUserDetailsService
import kuorum.security.permission.KuorumPermissionEvaluator
import kuorum.security.rememberMe.RememberMeTokenRepository

// Place your Spring DSL code here
beans = {
    xmlns aop:"http://www.springframework.org/schema/aop"
    aspectBean(kuorum.core.aop.LanguageInjectorAOP)
    aop.config("proxy-target-class":true) {
    }

    formattedDoubleConverter kuorum.web.binder.FormattedDoubleConverter

   userDetailsService(MongoUserDetailsService){
       kuorumUserService = ref("kuorumUserService")
   }


    localeResolver(org.springframework.web.servlet.i18n.CookieLocaleResolver){}

    localeChangeInterceptor(kuorum.web.interceptors.CustomLocaleInterceptor) {
        paramName = "lang"
    }

    tokenRepository(RememberMeTokenRepository){
        grailsApplication=ref("grailsApplication")
    }

    switch(Environment.current) {
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
            fileService(AmazonFileService){
                grailsApplication=ref("grailsApplication")
                burningImageService=ref("burningImageService")
                restKuorumApiService=ref("restKuorumApiService")
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
        springSecurityService = ref ('springSecurityService')
    }

    facebookSuccessHandler(SuccessAuthenticationHandler){
        kuorumUserService = ref('kuorumUserService')
    }


    // ACL
    permissionEvaluator(KuorumPermissionEvaluator) {
        grailsApplication     = ref('grailsApplication')
        springSecurityService = ref('springSecurityService')
    }


    // DOMAIN
    customDomainSpringFilter(CustomDomainSpringFilter){
        domainService = ref('domainService')
    }
}
