import com.mongodb.BasicDBObject
import grails.async.Promise
import grails.util.Holders
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.domain.DomainService
import kuorum.files.LessCompilerService
import org.kuorum.rest.model.domain.DomainRSDTO
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value

import java.security.KeyStore

class BootStrap {

    def grailsApplication
    def fixtureLoader
    def indexSolrService
    LessCompilerService lessCompilerService
    DomainService domainService
    def init = { servletContext ->
//        System.setProperty("https.protocols","TLSv1.2")

//        javax.servlet.http.HttpServletRequest.metaClass.getSiteUrl = {
////            return (delegate.scheme + "://" + delegate.serverName + ":" + delegate.serverPort + delegate.getContextPath())
//            return (delegate.scheme + "://" + delegate.serverName + ":" + delegate.serverPort)
//        }

//        if (System.getenv("KUORUM_BUILD_CUSTOM_CSS_DOMAINS")){
//            System.out.println("Builidng CSS")
        buildCustomDomainCssForAllDomains()
//        }


//        KeyStore ks = KeyStore.getInstance("JKS");
//        FileInputStream certFile = new FileInputStream("/usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts");
//        ks.load(certFile, "changeit".toCharArray());

//
//        environments {
//            development {
////                RoleUser.collection.getDB().dropDatabase()
////                fixtureLoader.load("testData")
////                indexSolrService.fullIndex()
//            }
//            test{
////                KuorumUser.collection.getDB().dropDatabase()
////                fixtureLoader.load("testData")
//            }
//            production{
////                KuorumUser.collection.getDB().dropDatabase()
////                fixtureLoader.load("testData")
////                indexSolrService.fullIndex()
//            }
//        }
    }
    def destroy = {
    }

    private void buildCustomDomainCssForAllDomains(){
        URL url = new URL("https://kuorum.org/kuorum")
        CustomDomainResolver.setUrl(url, "")
        List<DomainRSDTO> domains = domainService.findAllDomains()
        List<Promise> asyncUpdateConfig = []
        domains.each { domainRSDTO ->
            asyncUpdateConfig << grails.async.Promises.task {
                URL urlThread = new URL("https://${domainRSDTO.domain}/kuorum")
                CustomDomainResolver.setUrl(urlThread, "")
                if (domainRSDTO.domain == "kuorum.org"){
                    log.debug("Ingoring domain configuration of kuorum.org")
                }else if (domainRSDTO.version != 0){
                    MDC.put("domain", domainRSDTO.domain)
                    domainService.updateConfig(domainRSDTO)
                    MDC.clear()
                }else{
                    log.info("Ingoring domain cofiguration because its version is 0 and it was not configured")
                }
                // Used to update the version number and force browsers to download again the css and uploads the new css
                // lessCompilerService.compileCssForDomain(it)
            }
        }
        grails.async.Promises.waitAll(asyncUpdateConfig)
        CustomDomainResolver.clear()
    }
}
