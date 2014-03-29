import com.mongodb.BasicDBObject
import kuorum.core.annotations.MongoUpdatable
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.users.KuorumUser
import kuorum.users.RoleUser

class BootStrap {

    def grailsApplication
    def fixtureLoader

    def init = { servletContext ->

        //TODO: Think where this initialization could be called instead of bootstrap
        grailsApplication.domainClasses.each { domainClass ->
            if (MongoUpdatable in domainClass.clazz.declaredAnnotations*.annotationType()){
                // Class has de annotation MongoUpdate
                domainClass.metaClass.mongoUpdate = {->
                    def obj = delegate
                    log.info("updating $obj")
                    if (obj.validate()){
                        //Class is valid, so it's allowed to save it
                        log.info("Actualizando la ley $obj")
                        //Getting fields which are going to be setted on mongodb
                        def fields = obj.class.declaredFields.grep{kuorum.core.annotations.Updatable in it.annotations*.annotationType()}.collect{it.name}
                        BasicDBObject lawProperties =new BasicDBObject()
                        fields.each{field->
//                            def dbObject = delegate.properties.find{it.key in ['dbo']}.value[it]
                            def dbObject
                            if (obj.class.embedded.contains(field)){
                                //Complex object
                                dbObject = obj."$field".properties.dbo
                            }else if (obj."$field" instanceof List){
                                dbObject = obj."$field".collect{it.toString()}
                            }else if (obj."$field".hasProperty('id')){
                                dbObject = obj."$field".id
                            }else{
                                dbObject = obj."$field".toString()
                            }
                            lawProperties.append(field,dbObject)
                        }
                        obj.class.collection.update([_id:obj.id],['$set':lawProperties])
                        obj.refresh()
                    }else{
                        log.error(obj.errors)
                        throw KuorumExceptionUtil.createExceptionFromValidatable(delegate, "Datos erroneos actualizando ${obj.class.name}: $obj")
                    }

                }
            }

        }
        environments {
            development {
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
            }
            test{
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
            }
            production{
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
            }
        }
        if (RoleUser.count() == 0){
            //EMPTY DB
            fixtureLoader.load("basicData")
        }
    }
    def destroy = {
    }
}
