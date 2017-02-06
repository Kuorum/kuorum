package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.campaign.Campaign
import kuorum.campaign.CampaignService
import kuorum.causes.CausesService
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchUserPosts
import kuorum.core.model.solr.SolrType
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.project.Project
import kuorum.register.RegisterService
import kuorum.web.constants.WebConstants
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.springframework.web.servlet.LocaleResolver
import payment.campaign.DebateService
import payment.campaign.MassMailingService
import springSecurity.KuorumRegisterCommand

import javax.imageio.spi.RegisterableService
import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    def kuorumUserService
    def kuorumUserStatsService
    def cluckService
    def searchSolrService
    def postService
    def projectService
    RegisterService registerService

    CausesService causesService
    UserNewsService userNewsService

    CampaignService campaignService

    UserReputationService userReputationService;

    DebateService debateService;
    MassMailingService massMailingService;

//    def beforeInterceptor = [action: this.&checkUser, except: 'login']
    def beforeInterceptor = [action: this.&checkUser, except: ['index', 'politicians']]

    private checkUser(){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user) {
            //Search by old alias
            user = kuorumUserService.findByOldAlias(params.userAlias)
            if (user){
                def userLink = g.createLink(mapping: 'userShow', params: user.encodeAsLinkProperties())
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
                response.setHeader("Location", userLink);
                return false
            }else{
                response.sendError(HttpServletResponse.SC_NOT_FOUND)
                return false
            }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def secShow(String userAlias){
        KuorumUser user = KuorumUser.findByAlias(userAlias)
        log.info("Redirecting to normal user show")
        redirect (mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    @Deprecated
    def showWithId(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }else {
            log.warn("Executing show user")
            return showWithAlias(user.alias)
        }
    }

    def show(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        List<KuorumUser> recommendPoliticians = kuorumUserService.recommendPoliticians(user, new Pagination(max:12))
        List<Project> userProjects = projectService.politicianProjects(user)
        List<CauseRSDTO> causes = causesService.findDefendedCauses(user)
        Campaign campaign = campaignService.findActiveCampaign(user)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(user)
        List<UserNewRSDTO> userNews = userNewsService.findUserNews(user)
        List<DebateRSDTO> debates = debateService.findAllDebates(user).findAll{it.datePublished && it.datePublished < new Date()}
        [
                politician:user,
                userProjects:userProjects,
                recommendPoliticians:recommendPoliticians,
                campaign:campaign,
                causes:causes,
                userReputation: userReputationRSDTO,
                userNews:userNews,
                debates:debates
        ]
    }


    def userClucks(Pagination pagination){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Cluck> clucks = cluckService.userClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks]
    }
    def userPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        List<Post> userPost = postService.userPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def userVictories(SearchUserPosts searchUserPosts){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        searchUserPosts.victory = true
        List<Post> userPost = postService.userPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def politicianProjects(Pagination pagination){
        KuorumUser politician = kuorumUserService.findByAlias(params.userAlias)
        if (!politician){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        List<Project> projects = projectService.politicianProjects(politician, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${projects.size()<pagination.max}")
        render template: "/project/liProjects2Columns", model:[projects:projects]
    }
    def politicianDefendedPosts(SearchUserPosts searchUserPosts){
        KuorumUser user = KuorumUser.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        List<Post> userPost = postService.politicianDefendedPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }

    def politicianDefendedVictories(SearchUserPosts searchUserPosts){
        KuorumUser user = kuorumUserService.findByAlias(params.userAlias)
        if (!user){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        searchUserPosts.user = user
        searchUserPosts.publishedPosts =  true
        searchUserPosts.victory = true
        List<Post> userPost = postService.politicianDefendedPosts(searchUserPosts)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${userPost.size()<searchUserPosts.max}")
        render template: "/cluck/liPosts", model:[posts:userPost]
    }


    def userFollowers(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<KuorumUser> followers = kuorumUserService.findFollowers(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:followers])
        }else{
            [users:followers]
        }
    }

    def userFollowing(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<KuorumUser> following = kuorumUserService.findFollowing(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:following])
        }else{
            [users:following]
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def follow(String userAlias){
        KuorumUser following = kuorumUserService.findByAlias(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.createFollower(follower, following)
        render follower.following.size()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unFollow(String userAlias){
        KuorumUser following = kuorumUserService.findByAlias(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.deleteFollower(follower, following)
        render follower.following.size()
    }

    def subscribeTo(KuorumRegisterCommand command){
        KuorumUser following = kuorumUserService.findByAlias(params.userAlias)
        if (command.hasErrors()){
            flash.error=g.message(code: 'politician.subscribe.error')
            redirect mapping:"userShow", params: following.encodeAsLinkProperties()
            return ;
        }
        KuorumUser follower = registerService.registerUserFollowingPolitician(command, following)
        redirect mapping:"userShow", params: following.encodeAsLinkProperties()
    }
}
