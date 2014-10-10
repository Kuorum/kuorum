package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.law.Law
import kuorum.users.KuorumUser

class LawTagLib {
    static defaultEncodeAs = 'raw'

    def springSecurityService
    def postService
    def cluckService
    def postVoteService

    static namespace = "lawUtil"

    def ifLawIsEditable={attrs, body ->
        if (springSecurityService.isLoggedIn()){
            Law law = attrs.law
            Boolean isAdmin = SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN');
            Boolean isPoliticianAndSameRegion = false;
            if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
                KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
                isPoliticianAndSameRegion = user.personalData.province == law.region
            }
            if (isAdmin || isPoliticianAndSameRegion){
                out << body()
            }
        }
    }
}
