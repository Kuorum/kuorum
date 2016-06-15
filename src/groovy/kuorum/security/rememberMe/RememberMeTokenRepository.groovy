package kuorum.security.rememberMe

import grails.plugin.springsecurity.web.authentication.rememberme.GormPersistentTokenRepository

/**
 * Created by iduetxe on 15/06/16.
 */
class RememberMeTokenRepository extends GormPersistentTokenRepository{

    void updateToken(String series, String tokenValue, Date lastUsed) {
        def clazz = lookupDomainClass()
        if (!clazz) return
        //NO Transaction. Mongo Gorm is not saving.
//        def persistentLogin = clazz.get(series)
        clazz.collection.update(['_id':series],['$set':[token:tokenValue, lastUsed:lastUsed]])
    }
}
