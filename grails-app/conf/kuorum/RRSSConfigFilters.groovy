package kuorum

import org.codehaus.groovy.grails.commons.GrailsApplication

class RRSSConfigFilters {

    GrailsApplication grailsApplication

    def filters = {
        all(controller: '*', action: '*') {
            before = {

            }
            after = { Map model ->
                if (!model){
                    model = [:]
                }
                model.put("_facebookConfig",getFacebookConfig())
                model.put("_googleConfig",getGoogleConfig())

            }
            afterView = { Exception e ->

            }
        }
    }

    private def getFacebookConfig(){
        return grailsApplication.config.oauth.providers.facebook
    }
    private def getGoogleConfig(){
        return grailsApplication.config.oauth.providers.google
    }
}
