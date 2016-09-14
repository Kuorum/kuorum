package kuorum

import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.model.RegionType
import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrPost
import kuorum.core.model.solr.SolrProject
import kuorum.post.Post
import kuorum.project.Project
import kuorum.project.ProjectUpdate
import kuorum.project.ProjectVote
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import org.bson.types.ObjectId

class ProjectTagLib {
    static defaultEncodeAs = 'raw'

    def springSecurityService
    def postService
    def cluckService
    def postVoteService
    def projectService
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

    def projectFromSolr={attrs ->
        SolrProject solrProject = attrs.solrProject
        String var=attrs.var
        Project project= Project.get(new ObjectId(attrs.solrProject.id))
        pageScope.setVariable(var, project)

    }

    private static final String NAME_VAR_IF_USER_CAN_VOTE_PROEYET = "elseIfUserAvailableForNormalVoting"
    def ifUserAvailableForNormalVoting = {attrs, body ->
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (kuorumUserService.isUserRegisteredCompletely(user) && kuorumUserService.isUserConfirmedMail(user)){
                pageScope.setVariable(NAME_VAR_IF_USER_CAN_VOTE_PROEYET, false)
                out << body()
            }else{
                pageScope.setVariable(NAME_VAR_IF_USER_CAN_VOTE_PROEYET, true)
            }
        }
    }
    def elseUserAvailableForNormalVoting = {attrs, body ->
        if (pageScope.getVariable(NAME_VAR_IF_USER_CAN_VOTE_PROEYET)){
            out << body()
        }
    }

    def ifUserAvailableForVotingWithoutPersonalData = {attrs, body ->
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (!kuorumUserService.isUserRegisteredCompletely(user)){
                out << body()
            }
        }
    }
    def ifUserAvailableForVotingWithoutConfirmedMail = {attrs, body ->
        if (springSecurityService.isLoggedIn()){
            Project project = attrs.project
            KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
            if (kuorumUserService.isUserRegisteredCompletely(user) && !kuorumUserService.isUserConfirmedMail(user)){
                out << body()
            }
        }
    }

    def ifAllowedToUpdateProject = {attrs, body ->
        Project project = attrs.project
        if (springSecurityService.isLoggedIn() && (springSecurityService.getCurrentUser().equals(project.owner) || SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN'))){
            out << body()
        }
    }

    def ifAllowedToAddPost = {attrs, body ->
        Project project = attrs.project
        if ((springSecurityService.isLoggedIn()) && (SpringSecurityUtils.ifAnyGranted('ROLE_USER, ROLE_ADMIN, ROLE_PREMIUM, ROLE_POLITICIAN'))){
            KuorumUser user = springSecurityService.getCurrentUser();
            if (!(user.userType.equals(UserType.POLITICIAN)  &&  project.owner.professionalDetails.region.equals(user.professionalDetails?.region?:null))){
                out << body()
            }
        }
    }

    def showProjectRegionIcon={attrs ->
        Project project = attrs.project
        Region region
        if (project){
            region = project.region
        }else{
            region = attrs.region
        }
        String cssClass= ""
        switch (region.regionType){
            case RegionType.LOCAL:          cssClass = "icon2-ciudad"; break;
            case RegionType.STATE:          cssClass = "icon2-region"; break;
            case RegionType.NATION:         cssClass = "icon2-estado"; break;
            case RegionType.SUPRANATIONAL:  cssClass = "icon2-europe"; break;
        }
        String regionTypeText = message(code:'kuorum.core.model.RegionType.'+region.regionType)
        out << """
                <span class="fa ${cssClass} fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="${regionTypeText}"></span>
                <span class="sr-only">${regionTypeText}</span>
        """
    }

    def showProjectModule={attrs->
        Project project = attrs.project
        ProjectUpdate projectUpdate =attrs.projectUpdate?:null
        ProjectVote userVote = null
        if ((springSecurityService.isLoggedIn()) && (SpringSecurityUtils.ifAnyGranted('ROLE_USER, ROLE_ADMIN, ROLE_PREMIUM, ROLE_POLITICIAN'))){
            KuorumUser user = springSecurityService.getCurrentUser();
            userVote = projectService.findProjectVote(project, user);
        }
        out << render (template:'/modules/projects/projectOnList', model: [project: project,projectUpdate:projectUpdate, userVote:userVote])
    }
}
