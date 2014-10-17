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
    RegionService regionService

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

    private static final String NAME_VAR_IF_LAW_IS_VOTABLE_ELSE = "ifLawIsVotableElse"
    def ifLawIsVotable = {attrs, body ->
        pageScope.setVariable(NAME_VAR_IF_LAW_IS_VOTABLE_ELSE, Boolean.FALSE)
        if (springSecurityService.isLoggedIn()){
            Law law = attrs.law
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (regionService.isRelevantRegionForUser(user,law.region)){
                out << body()
            }else{
                pageScope.setVariable(NAME_VAR_IF_LAW_IS_VOTABLE_ELSE, Boolean.TRUE)
            }
        }
    }

    def elseLawIsVotable = {attrs, body ->
        if (pageScope.getVariable(NAME_VAR_IF_LAW_IS_VOTABLE_ELSE)){
            out << body()
        }
    }
}
