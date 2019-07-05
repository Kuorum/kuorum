package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.notifications.NotificationService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.web.commands.customRegister.KuorumUserContactMessageCommand
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.message.NewMessageRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.news.UserNewRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.CampaignService

import javax.servlet.http.HttpServletResponse

class KuorumUserController {

    def springSecurityService
    KuorumUserService kuorumUserService
    def postService
    RegisterService registerService
    CookieUUIDService cookieUUIDService

    NotificationService notificationService
    InternalMessageService internalMessageService


    CausesService causesService
    UserNewsService userNewsService

    UserReputationService userReputationService

    CampaignService campaignService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def secShow(String userAlias){
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(userAlias)
        log.info("Redirecting to normal user show")
        redirect (mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    def show(String userAlias){
        KuorumUser user = kuorumUserService.findByAlias(userAlias)
        KuorumUserRSDTO userRSDTO = null
        try{
            userRSDTO = kuorumUserService.findUserRSDTO(userAlias)
        }catch (Exception e){
            log.info("User [$userAlias] not found")
            userRSDTO = null
        }
        if (!userRSDTO) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        String viewerUid = cookieUUIDService.buildUserUUID()
        List<SearchKuorumUserRSDTO> recommendedUsers =  kuorumUserService.recommendUsers(user, new Pagination([max:50]))
        List<CauseRSDTO> causes = causesService.findSupportedCauses(userRSDTO)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(userRSDTO)
        List<UserNewRSDTO> userNews = userNewsService.findUserNews(userRSDTO)
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(userRSDTO.id,viewerUid).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
        [
                politician:userRSDTO,
                recommendedUsers:recommendedUsers,
                causes:causes,
                userReputation: userReputationRSDTO,
                userNews:userNews,
                campaigns:campaigns,
        ]
    }

    def userFollowers(String userAlias){
        List<BasicDataKuorumUserRSDTO> followers = kuorumUserService.findFollowers(userAlias, new Pagination())
        render (template:'/kuorumUser/embebedUsersList', model:[users:followers])
    }

    def userFollowing(String userAlias){
        List<BasicDataKuorumUserRSDTO> following = kuorumUserService.findFollowing(userAlias, new Pagination())
        render (template:'/kuorumUser/embebedUsersList', model:[users:following])
    }

    @Secured(['ROLE_USER'])
    def follow(String userAlias){
        BasicDataKuorumUserRSDTO following = kuorumUserService.findBasicUserRSDTO(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        KuorumUserSession follower = springSecurityService.principal
        kuorumUserService.createFollower(follower, following)
        BasicDataKuorumUserRSDTO followerRSDTO = kuorumUserService.findBasicUserRSDTO(follower)
        render followerRSDTO.numFollowing
    }

    @Secured(['ROLE_USER']) // Incomplete users can't follow users
    def unFollow(String userAlias){
        BasicDataKuorumUserRSDTO following = kuorumUserService.findBasicUserRSDTO(userAlias)
        if (!following){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        KuorumUserSession follower = springSecurityService.principal
        kuorumUserService.deleteFollower(follower, following)
        BasicDataKuorumUserRSDTO followerRSDTO = kuorumUserService.findBasicUserRSDTO(follower)
        render followerRSDTO.numFollowing
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def contactUser(KuorumUserContactMessageCommand contactRegisterCommand){
        if (!contactRegisterCommand.validate()){
            flash.error = g.message(error: contactRegisterCommand.errors.allErrors[0])
            redirect mapping:"politicians"
            return
        }
        BasicDataKuorumUserRSDTO userContacted = kuorumUserService.findBasicUserRSDTO(contactRegisterCommand.contactUserId, true)
        if (!userContacted){
            flash.error = "User not found"
            redirect mapping:"politicians"
        }

        KuorumUserSession loggedUser = springSecurityService.principal

        NewMessageRDTO newMessageRDTO = new NewMessageRDTO()
        newMessageRDTO.setSubject(contactRegisterCommand.subject)
        newMessageRDTO.setBody(contactRegisterCommand.message)
        newMessageRDTO.setToUserId(contactRegisterCommand.contactUserId)
        newMessageRDTO.setCause(contactRegisterCommand.cause)
        internalMessageService.sendInternalMessage(loggedUser, newMessageRDTO)
        flash.message = g.message(code: 'kuorumUser.contactMessage.success', args: [userContacted.name])
        redirect (mapping:"userShow", params: userContacted.encodeAsLinkProperties())
    }

}
