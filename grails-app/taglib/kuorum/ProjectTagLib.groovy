package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.model.RegionType
import kuorum.core.model.UserType
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService

class ProjectTagLib {
    static defaultEncodeAs = 'raw'

    def springSecurityService
    def postService
    def cluckService
    def postVoteService
    RegionService regionService
    KuorumUserService kuorumUserService
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
    def ifUserAvailableForVoting= {attrs, body ->
        pageScope.setVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE, Boolean.FALSE)
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (kuorumUserService.isUserRegisteredCompletely(user)){
                out << body()
            }else{
                pageScope.setVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE, Boolean.TRUE)
            }
        }
    }

    def elseUserAvailableForVoting = {attrs, body ->
        if (pageScope.getVariable(NAME_VAR_IF_PROJECT_IS_VOTABLE_ELSE)){
            out << body()
        }
    }

    def ifAllowedToUpdateProject = {attrs, body ->
        Project project = attrs.project
        if (springSecurityService.isLoggedIn() && springSecurityService.getCurrentUser().equals(project.owner)){
            out << body()
        }
    }

    def ifAllowedToAddPost = {attrs, body ->
        Project project = attrs.project
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = springSecurityService.getCurrentUser();
            if (!(user.userType.equals(UserType.POLITICIAN)  && project.owner.politicianOnRegion.equals(user.politicianOnRegion))){
                out << body()
            }
        }
    }

    def showProjectRegionIcon={attrs ->
        Project project = attrs.project
        String cssClass= ""
        switch (project.region.regionType){
            case RegionType.LOCAL:          cssClass = "icon2-ciudad"; break;
            case RegionType.STATE:          cssClass = "icon2-region"; break;
            case RegionType.NATION:         cssClass = "icon2-estado"; break;
            case RegionType.SUPRANATIONAL:  cssClass = "icon2-europe"; break;
        }
        String regionTypeText = message(code:'kuorum.core.model.RegionType.'+project.region.regionType)
        out << """
                <span class="${cssClass} fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="${regionTypeText}"></span>
                <span class="sr-only">${regionTypeText}</span>
        """
    }
}
