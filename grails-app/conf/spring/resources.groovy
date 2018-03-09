import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.spring.BeanBuilder
import grails.util.Environment
import kuorum.core.security.passwordEncoders.PasswordFixingDaoAuthenticationProvider
import kuorum.core.security.passwordEncoders.Sha256ToBCryptPasswordEncoder
import kuorum.core.springSecurity.handlers.SuccessAuthenticationHandler
import kuorum.files.AmazonFileService
import kuorum.register.MongoUserDetailsService
import kuorum.security.permission.KuorumPermissionEvaluator
import kuorum.security.rememberMe.RememberMeTokenRepository
import kuorum.solr.IndexSolrService
import kuorum.solr.SearchSolrService
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.core.CoreContainer
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder

// Place your Spring DSL code here
beans = {
    xmlns aop:"http://www.springframework.org/schema/aop"
    aspectBean(kuorum.core.aop.LanguageInjectorAOP)
    aop.config("proxy-target-class":true) {
    }

    formattedDoubleConverter kuorum.web.binder.FormattedDoubleConverter

   userDetailsService(MongoUserDetailsService)


    localeResolver(org.springframework.web.servlet.i18n.CookieLocaleResolver){}

    localeChangeInterceptor(kuorum.web.interceptors.CustomLocaleInterceptor) {
        paramName = "lang"
    }

    tokenRepository(RememberMeTokenRepository){
        grailsApplication=ref("grailsApplication")
    }

    switch(Environment.current) {
        case Environment.PRODUCTION:
            fileService(AmazonFileService){
                grailsApplication=ref("grailsApplication")
                burningImageService=ref("burningImageService")
            }
            break

        default:
//            fileService(LocalFileService) {
//                grailsApplication=grailsApplication
//                burningImageService=burningImageService
//            }
            fileService(AmazonFileService){
                grailsApplication=ref("grailsApplication")
                burningImageService=ref("burningImageService")
            }
            break
    }

    def bb = new BeanBuilder()
    bb.beans{
        if (Boolean.parseBoolean(application.config.solr.embedded)){
//            System.setProperty("solr.solr.home", application.config.solr.solrHome);
            String solrDir = application.config.solr.solrHome;
            File solrConfig = new File("$solrDir/solr.xml")
            CoreContainer container = CoreContainer.createAndLoad(solrDir,solrConfig);

//            container.load();
//            EmbeddedSolrServer server = new EmbeddedSolrServer( container, "core name as defined in solr.xml" );
            solrServer(EmbeddedSolrServer,container,container.allCoreNames[0])
        }else{
            def env = System.getenv()
            String solrUrl = application.config.solr.solrUrl
            solrServer(HttpSolrServer,solrUrl)
        }
    }
//   def solrUrl
   //def solrServer = new HttpSolrServer("http://localhost:9080/solr/kuorumUsers")

    indexSolrService(IndexSolrService){
        server = solrServer
        grailsApplication = ref('grailsApplication')
    }
   searchSolrService(SearchSolrService){
       server = solrServer
       indexSolrService = indexSolrService
       restKuorumApiService = ref('restKuorumApiService')
       springSecurityService = ref ('springSecurityService')
   }


//    xmlns task:"http://www.springframework.org/schema/task"
//
//    task.'annotation-driven'('proxy-target-class':true, 'mode':'proxy')



    /* Passwords recovered from kuorum (pilot) ar in SHA2 and now we are using bcrypt.
    * It is necesary to pass all pass to bcrypt
    */
    def conf = SpringSecurityUtils.securityConfig

    bcryptPasswordEncoder(BCryptPasswordEncoder, conf.password.bcrypt.logrounds) // 10

    sha256PasswordEncoder(MessageDigestPasswordEncoder, conf.password.algorithm) {
        encodeHashAsBase64 = conf.password.encodeHashAsBase64 // false
        iterations = conf.password.hash.iterations // 10000
    }

    passwordEncoder(Sha256ToBCryptPasswordEncoder) {
        bcryptPasswordEncoder = ref('bcryptPasswordEncoder')
        sha256PasswordEncoder = ref('sha256PasswordEncoder')
    }

    daoAuthenticationProvider(PasswordFixingDaoAuthenticationProvider) {
        userDetailsService = ref('userDetailsService')
        passwordEncoder = ref('passwordEncoder')
        userCache = ref('userCache')
        saltSource = ref('saltSource')
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        authoritiesMapper = ref('authoritiesMapper')
        hideUserNotFoundExceptions = conf.dao.hideUserNotFoundExceptions // true
        grailsApplication = ref('grailsApplication')
    }


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
}
