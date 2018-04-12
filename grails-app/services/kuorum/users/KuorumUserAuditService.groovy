package kuorum.users

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.codehaus.groovy.grails.commons.GrailsApplication

@Transactional
class KuorumUserAuditService {

    SpringSecurityService springSecurityService
    GrailsApplication grailsApplication

    void auditEditUser(KuorumUser auditUser) {
        try{
            KuorumUserAudit kuorumUserAudit = new KuorumUserAudit()
            if (springSecurityService.isLoggedIn()){
                kuorumUserAudit.editor = KuorumUser.get(springSecurityService.principal.id)
            }
            if (auditUser != kuorumUserAudit.editor) {

                kuorumUserAudit.user = auditUser
                //            kuorumUserAudit.snapshotUser = auditUser
                kuorumUserAudit.dateCreated = new Date()
                Map<String, String> res = getPropertyValues(auditUser, listPropertyNames(auditUser), "")
                def obj = auditUser
                KuorumUser.embedded.each { fieldName ->
                    if (obj."${fieldName}" instanceof java.util.List || obj."${fieldName}" instanceof java.util.Set) {
                        obj."${fieldName}".eachWithIndex { listObj, idx ->
                            res.putAll(getPropertyValues(listObj, listPropertyNames(listObj), "${fieldName}[$idx]."))
                        }
                    } else if (obj."${fieldName}" != null) {
                        res.putAll(getPropertyValues(obj."${fieldName}", listPropertyNames(obj."${fieldName}"), "${fieldName}."))
                    }
                }
                kuorumUserAudit.snapshot = res
                        .inject([:]) {
                    map, v -> map << [(v.key.toString().replaceAll("\\.", "_")): v.value?.toString()]
                }.findAll {
                    it.key != "lastUpdated"
                }
                def audits = KuorumUserAudit.findAllByUser(auditUser, [max: 1, sort: "dateCreated", order: "desc"])
                if (audits) {
                    //Filter not edited fields
                    kuorumUserAudit.updates = [:]
                    Map prevSnapshot = audits.first().snapshot
                    kuorumUserAudit.snapshot.each { k, v ->
                        if (prevSnapshot.get(k) != v) {
                            kuorumUserAudit.updates.put(k, v)
                        }
                    }
                } else {
                    kuorumUserAudit.updates = kuorumUserAudit.snapshot
                }
                filterNoAuditableFields(kuorumUserAudit)
                if (kuorumUserAudit.updates) {
                    kuorumUserAudit.save()
                }
            }else{
                //The user is editing himself
                log.debug("The user ${auditUser} is editing himself. No audit save.")
            }
        }catch (Throwable e){
            log.warn("Not audit save for user ${auditUser}", e)
        }
    }

    private void filterNoAuditableFields(KuorumUserAudit kuorumUserAudit){
        //TODO: Make this with an anotation
        List<String> noAuditableFields = ['following', 'followers', 'numFollowers', 'favorites', 'notice', 'lastNotificationChecked']

        noAuditableFields.each {noAuditableField ->
            kuorumUserAudit.updates = kuorumUserAudit.updates.findAll{!it.key.startsWith(noAuditableField)}
        }
    }

    private Map<String,String> getPropertyValues(def obj, def properties, String prefix){
        def res = [:]
        properties.each{ fieldName ->
            if (KuorumUser.embedded.contains(fieldName) &&
                    (obj."${fieldName}" instanceof java.util.List || obj."${fieldName}" instanceof java.util.Set )) {
                obj."${fieldName}".eachWithIndex { listObj, idx ->
                    res.putAll(getPropertyValues(listObj,listPropertyNames(listObj), "${prefix}${fieldName}[$idx]."))
                }
            }else if (KuorumUser.embedded.contains(fieldName) && obj."${fieldName}" && grailsApplication.isDomainClass(obj."${fieldName}".class)) {
                res.putAll(getPropertyValues(obj."${fieldName}", listPropertyNames(obj."${fieldName}"), "${prefix}${fieldName}."))
            }else {
                //Basic Data
                res << ["${prefix}${fieldName}":obj."${fieldName}"?.toString()?:null]
            }
        }
        res
    }

    private def listPropertyNames(def obj){
        if (obj){
            new org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass(obj.class).persistentProperties.collect{it.name}
        }else{
            []
        }
    }
}
