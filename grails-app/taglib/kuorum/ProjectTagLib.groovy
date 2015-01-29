package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.project.Project
import kuorum.users.KuorumUser

class ProjectTagLib {
    static defaultEncodeAs = 'raw'

    def springSecurityService
    def postService
    def cluckService
    def postVoteService
    RegionService regionService

    static namespace = "projectUtil"

    def ifProjectIsEditable={attrs, body ->
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            Boolean isAdmin = SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN');
            Boolean isPoliticianAndSameRegion = false;
            if (SpringSecurityUtils.ifAnyGranted('ROLE_POLITICIAN')){
                KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
                isPoliticianAndSameRegion = user.personalData.province == project.region
            }
            if (isAdmin || isPoliticianAndSameRegion){
                out << body()
            }
        }
    }

    private static final String NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE = "ifProjectIsVotableElse"
    def ifProjectIsVotable = {attrs, body ->
        pageScope.setVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE, Boolean.FALSE)
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (regionService.isRelevantRegionForUser(user,project.region)){
                out << body()
            }else{
                pageScope.setVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE, Boolean.TRUE)
            }
        }
    }

    def elseProjectIsVotable = {attrs, body ->
        if (pageScope.getVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE)){
            out << body()
        }
    }
}
