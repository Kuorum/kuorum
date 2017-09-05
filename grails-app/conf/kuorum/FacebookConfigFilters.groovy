package kuorum

import org.codehaus.groovy.grails.commons.GrailsApplication

class FacebookConfigFilters {

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

            }
            afterView = { Exception e ->

            }
        }
    }

    private def getFacebookConfig(){
        return grailsApplication.config.oauth.providers.facebook
    }
}
