package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.search.Pagination
import kuorum.notifications.NotificationService
import kuorum.register.RegisterService
import kuorum.web.commands.customRegister.KuorumUserContactMessageCommand
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.CampaignService

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    def kuorumUserService
    def postService
    RegisterService registerService
    CookieUUIDService cookieUUIDService

    NotificationService notificationService


    CausesService causesService
    UserNewsService userNewsService

    UserReputationService userReputationService

    CampaignService campaignService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def secShow(String userAlias){
        KuorumUser user = KuorumUser.findByAliasAndDomain(userAlias, CustomDomainResolver.domain)
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
        List<SearchKuorumUserRSDTO> recommendedUsers =  kuorumUserService.recommendUsers(user, new Pagination([max:50]))
        List<CauseRSDTO> causes = causesService.findSupportedCauses(user)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(user)
        List<UserNewRSDTO> userNews = userNewsService.findUserNews(user)
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(user.id.toString(),viewerUid).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
        [
                politician:user,
                recommendedUsers:recommendedUsers,
                causes:causes,
                userReputation: userReputationRSDTO,
                userNews:userNews,
                campaigns:campaigns,
        ]
    }

    def userFollowers(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<BasicDataKuorumUserRSDTO> followers = kuorumUserService.findFollowers(user, new Pagination())
        if (request.xhr){
            render (template:'/kuorumUser/embebedUsersList', model:[users:followers])
        }else{
            [users:followers]
        }
    }

    def userFollowing(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        List<BasicDataKuorumUserRSDTO> following = kuorumUserService.findFollowing(user, new Pagination())
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
            return
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
            return
        }
        KuorumUser follower = KuorumUser.get(springSecurityService.principal.id)
        kuorumUserService.deleteFollower(follower, following)
        render follower.following.size()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def contactUser(KuorumUserContactMessageCommand contactRegister){
        if (!contactRegister.validate()){
            flash.error = g.message(error: contactRegister.errors.allErrors[0])
            if (contactRegister.politician){
                redirect mapping:"userShow", params: contactRegister.politician.encodeAsLinkProperties()
            }else{
                redirect mapping:"politicians"
            }
            return
        }

        KuorumUser user = springSecurityService.getCurrentUser()
        notificationService.sendPoliticianContactNotification(contactRegister.politician, user, contactRegister.message, contactRegister.cause)
        flash.message = g.message(code: 'kuorumUser.contactMessage.success', args: [contactRegister.politician.name])
        redirect (mapping:"userShow", params: contactRegister.politician.encodeAsLinkProperties())
    }

}
