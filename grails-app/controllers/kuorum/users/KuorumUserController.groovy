package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.register.RegisterService
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.CampaignService
import payment.campaign.DebateService
import payment.campaign.NewsletterService
import springSecurity.KuorumRegisterCommand

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    def kuorumUserService
    def kuorumUserStatsService
    def searchSolrService
    def postService
    def projectService
    RegisterService registerService
    CookieUUIDService cookieUUIDService


    CausesService causesService
    UserNewsService userNewsService

    UserReputationService userReputationService;

    DebateService debateService;
    CampaignService campaignService
    NewsletterService newsletterService;

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
                def homeLink = g.createLink(mapping: 'home')
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
                response.setHeader("Location", homeLink);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND)
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

    def show(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        String viewerUid = cookieUUIDService.buildUserUUID()
        List<KuorumUser> recommendPoliticians = kuorumUserService.suggestUsers(new Pagination(max:12),[user])
        List<CauseRSDTO> causes = causesService.findSupportedCauses(user)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(user)
        List<UserNewRSDTO> userNews = userNewsService.findUserNews(user)
//        List<DebateRSDTO> debates = debateService.findAllDebates(user).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
//        List<PostRSDTO> posts = postService.findAllPosts(user,viewerUid).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(user,viewerUid).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
        [
                politician:user,
                recommendPoliticians:recommendPoliticians,
                causes:causes,
                userReputation: userReputationRSDTO,
                userNews:userNews,
                campaigns:campaigns,
        ]
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

    @Secured(['ROLE_USER'])
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

    @Secured(['ROLE_USER']) // Incomplete users can't follow users
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
