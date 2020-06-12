package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import kuorum.register.KuorumUserSession
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditProfilePicturesCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignPageRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.CampaignService
import payment.campaign.NewsletterService
import payment.campaign.PostService
import payment.contact.ContactService

class DashboardController {

    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService
    CausesService causesService
    ContactService contactService
    NewsletterService newsletterService
    PostService postService
    CampaignService campaignService
    DashboardService dashboardService
    CookieUUIDService cookieUUIDService

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        return dashboard()
    }
    def dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        BasicDataKuorumUserRSDTO userRSDTO = kuorumUserService.findBasicUserRSDTO(springSecurityService.principal.id.toString())
        log.info("Dashboard ${userRSDTO.alias} : Start")
        Map model = buildPaymentDashboard(userRSDTO)
        model.put("tour", params.tour?Boolean.parseBoolean(params.tour):false)
        render view: "/dashboard/dashboard", model: model
    }

    private def buildPaymentDashboard(BasicDataKuorumUserRSDTO user){
        KuorumUserSession userSession = springSecurityService.principal
        CampaignPageRSDTO myCampaigns = campaignService.findAllCampaigns(userSession, false, 0, 1)
        [
                numberCampaigns:myCampaigns.total,
                user:user,
                emptyEditableData:emptyEditableData(),
                campaigns: [],
                totalCampaigns: 100,
                showAuthor: true
        ]

    }

    //FAST CHAPU - Evaluating empty data
    def emptyEditableData(){
        KuorumUserSession userSession = springSecurityService.principal
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<CauseRSDTO> causes = causesService.findSupportedCauses(userSession)

        List fields = [
                [urlMapping: 'profileEditAccountDetails',
                 uncompleted: (new AccountDetailsCommand(user)).properties?.findAll{!it.value && !["password", "user"].contains(it.key)}.size(),
                 total: (new AccountDetailsCommand(user)).properties.findAll{!["password", "user"].contains(it.key)}.size()],
                [urlMapping: 'profileCauses',
                 uncompleted:causes?0:1,
                 total: 1],
                [urlMapping: 'profileEditUser',
                 uncompleted: new EditUserProfileCommand(user).properties.findAll{!it.value}.size(),
                 total: new EditUserProfileCommand(user).properties.size()],
                [urlMapping: 'profileNews',
                 uncompleted:user.relevantEvents?0:1,
                 total: 1],
                [urlMapping: 'profilePictures',
                 uncompleted: new EditProfilePicturesCommand(user).properties.findAll {!it.value && !["class", "errors", "constraints"].contains(it.key)}.size(),
                 total: new EditProfilePicturesCommand(user).properties.findAll{!["class", "errors", "constraints"].contains(it.key)}.size()],
                [urlMapping: 'profileSocialNetworks',
                 uncompleted: (new SocialNetworkCommand(user)).properties.findAll{!it.value}.size(),
                 total: (new SocialNetworkCommand(user)).properties.size()]
        ]
        fields.each{it['completed']=it.total - it.uncompleted}
        Integer totalFields = fields*.total.sum()
        Integer emptyFields= fields.sum{it.uncompleted}
        return [
                percentage: (1 - emptyFields/totalFields)*100,
                fields:fields,

        ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardCampaigns(Pagination pagination){
        KuorumUserSession user = springSecurityService.principal
        String viewerUid = cookieUUIDService.buildUserUUID()
        Integer page = pagination.offset/pagination.max
        SearchResultsRSDTO searchResutlsRSDTO = dashboardService.findAllContactsCampaigns(user, viewerUid, page)
        List<NewsletterRQDTO> campaigns = searchResutlsRSDTO.data
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${searchResutlsRSDTO.total < (pagination.offset+pagination.max)}")
//        render template: "/campaigns/cards/campaignsList", model:[campaigns:[], showAuthor: true]
        render template: "/campaigns/cards/campaignsList", model:[campaigns:campaigns, showAuthor: true]
    }

}
