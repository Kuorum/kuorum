import com.mongodb.BasicDBObject
import grails.async.Promise
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.domain.DomainService
import kuorum.files.LessCompilerService
import org.kuorum.rest.model.domain.DomainRSDTO

import java.security.KeyStore

class BootStrap {

    def grailsApplication
    def fixtureLoader
    def indexSolrService
    LessCompilerService lessCompilerService
    DomainService domainService
    def init = { servletContext ->

//        javax.servlet.http.HttpServletRequest.metaClass.getSiteUrl = {
////            return (delegate.scheme + "://" + delegate.serverName + ":" + delegate.serverPort + delegate.getContextPath())
//            return (delegate.scheme + "://" + delegate.serverName + ":" + delegate.serverPort)
//        }
        URL url = new URL("https://kuorum.org/kuorum")
        CustomDomainResolver.setUrl(url, "")
        List<DomainRSDTO> domains = domainService.findAllDomains()
        List<Promise> asyncUpdateConfig = []
            domains.each { domainRSDTO ->
                asyncUpdateConfig << grails.async.Promises.task {
                    URL urlThread = new URL("https://kuorum.org/kuorum")
                    CustomDomainResolver.setUrl(urlThread, "")
                    domainService.updateConfig(domainRSDTO)
                    // Used to update the version number and force browsers to download again the css and uploads the new css
                    // lessCompilerService.compileCssForDomain(it)
                }
        }
        grails.async.Promises.waitAll(asyncUpdateConfig)
        CustomDomainResolver.clear()


//        KeyStore ks = KeyStore.getInstance("JKS");
//        FileInputStream certFile = new FileInputStream("/usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts");
//        ks.load(certFile, "changeit".toCharArray());

        //TODO: Think where this initialization could be called instead of bootstrap
        grailsApplication.domainClasses.each { domainClass ->
            if (MongoUpdatable in domainClass.clazz.declaredAnnotations*.annotationType()){
                // Class has de annotation MongoUpºdate
                domainClass.metaClass.mongoUpdate = {->
                    def obj = delegate
                    log.info("updating $obj")
                    if (obj.validate()){
                        //Class is valid, so it's allowed to save it
                        log.info("Actualizando la ley $obj")
                        //Getting fields which are going to be setted on mongodb
                        def fields = obj.class.declaredFields.grep{kuorum.core.annotations.Updatable in it.annotations*.annotationType()}.collect{it.name}
                        BasicDBObject objProperties =new BasicDBObject()
                        BasicDBObject objUnsetProperties =new BasicDBObject()
                        fields.each{field->
//                            def dbObject = delegate.properties.find{it.key in ['dbo']}.value[it]
                            def dbObject
                            if (obj.class.embedded.contains(field)){
                                //Complex object
                                dbObject = obj."$field"?.properties?.dbo
                            }else if (obj."$field" instanceof List){
                                dbObject = obj."$field".collect{it.toString()}
                            }else if (obj."$field" instanceof Integer){
                                dbObject = obj."$field"
                            }else if (obj."$field" instanceof Date){
                                dbObject = obj."$field"
                            }else if (obj."$field".hasProperty('id')){
                                dbObject = obj."$field".id
                            }else if (obj."$field" != null) {
                                dbObject = obj."$field".toString()
                            }
                            if (dbObject)
                                objProperties.append(field,dbObject)
                            else
                                objUnsetProperties.append(field,"")
                        }
                        if (objProperties)
                            obj.class.collection.update([_id:obj.id],['$set':objProperties])
                        if (objUnsetProperties)
                            obj.class.collection.update([_id:obj.id],['$unset':objUnsetProperties])
                        obj.refresh()
                    }else{
                        log.error(obj.errors)
                        throw KuorumExceptionUtil.createExceptionFromValidatable(delegate, "Datos erroneos actualizando ${obj.class.name}: $obj")
                    }

                }
            }

        }

        List.metaClass.reverseFind = { Closure c ->
            delegate.iterator().reverse().find c
        }

        environments {
            development {
//                RoleUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
//                indexSolrService.fullIndex()
            }
            test{
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
            }
            production{
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
//                indexSolrService.fullIndex()
            }
        }
//        if (RoleUser.count() == 0){
//            //EMPTY DB
//            RoleUser.collection.getDB().dropDatabase()
//            log.info("Empty Database: loading basicData")
//            fixtureLoader.load("basicData")
//        }
    }
    def destroy = {
    }
}
