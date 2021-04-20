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
}
