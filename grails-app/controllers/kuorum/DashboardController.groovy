package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import kuorum.register.KuorumUserSession
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditProfilePicturesCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRQDTO
import org.kuorum.rest.model.notification.campaign.NewsletterRSDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import payment.campaign.CampaignService
import payment.campaign.NewsletterService
import payment.campaign.PostService
import payment.contact.ContactService

class DashboardController {

    SpringSecurityService springSecurityService
    def kuorumUserService
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
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        log.info("Dashboard ${user}")
        Map model = buildPaymentDashboard(user)
        model.put("tour", params.tour?true:false)
        if (dashboardService.forceUploadContacts()) {
            render view: "/dashboard/payment/paymentNoContactsDashboard", model: model
//          }else if (!model.numberCampaigns){
//              render view: "/dashboard/payment/paymentNoCampaignsDashboard", model: model
        }else{
            List<SearchKuorumUserRSDTO> recommendations = kuorumUserService.recommendUsers(user, new Pagination([max:50]))
            model.put("recommendations",recommendations)
            render view: "/dashboard/dashboard", model: model
        }
    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def skipContacts(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.skipUploadContacts = true
        user.save()
        redirect mapping:'dashboard'
    }

    private def buildPaymentDashboard(KuorumUser user){
        KuorumUserSession userSession = springSecurityService.principal
        String viewerUid = cookieUUIDService.buildUserUUID()

        SearchResultsRSDTO searchResultsRSDTO = dashboardService.findAllContactsCampaigns(userSession, viewerUid)

        List<NewsletterRSDTO> myNewsletters = newsletterService.findCampaigns(userSession)
        List<CampaignRSDTO> myCampaigns = campaignService.findAllCampaigns(userSession)
        List<NewsletterRSDTO> sentCampaigns = myCampaigns*.newsletter.findAll{it.status==CampaignStatusRSDTO.SENT}
        List<NewsletterRSDTO> sentNewsletters = myNewsletters.findAll{it.status==CampaignStatusRSDTO.SENT}
        List<NewsletterRSDTO> sentCommunications = sentNewsletters + sentCampaigns
        Long numberCampaigns = sentCommunications?.size()?:0
        NewsletterRSDTO lastCampaign = null
        Long durationDays = 0
        if (sentCommunications){
            lastCampaign = sentCommunications.sort {it.sentOn}.last()?:null
            use(groovy.time.TimeCategory) {
                def duration = new Date() - lastCampaign.sentOn
                durationDays = duration.days
            }
        }

//        List<KuorumUser> recommendedUsers = kuorumUserService.recommendPoliticians(user, new Pagination(max:16))

        [
                lastCampaign:lastCampaign,
                numberCampaigns:numberCampaigns,
                durationDays:durationDays,
//                contacts: contactService.getUsers(user),
//                recommendedUsers:recommendedUsers,
                user:user,
                emptyEditableData:emptyEditableData(user),
                campaigns: searchResultsRSDTO.data,
                totalCampaigns: searchResultsRSDTO.total,
                showAuthor: true
        ]

    }

    //FAST CHAPU - Evaluating empty data
    def emptyEditableData(KuorumUser user){

        List<CauseRSDTO> causes = causesService.findSupportedCauses(user)

        List fields = [
                [urlMapping: 'profileEditAccountDetails',
                 uncompleted: (new AccountDetailsCommand(user)).properties?.findAll{!it.value && !["password", "user"].contains(it.key)}.size(),
                 total: (new AccountDetailsCommand(user)).properties.findAll{!["password", "user"].contains(it.key)}.size()],
                [urlMapping: 'profileCauses',
                 uncompleted:causes?0:1,
                 total: 1],
                [urlMapping: 'profileEditUser',
                 uncompleted: new EditUserProfileCommand(user).properties.findAll{!it.value && !["workingSector", "studies", "enterpriseSector", "position"].contains(it.key)}.size(),
                 total: new EditUserProfileCommand(user).properties.findAll {!["workingSector", "studies", "enterpriseSector", "position"].contains(it.key)}.size()],
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
        render template: "/campaigns/cards/campaignsList", model:[campaigns:campaigns, showAuthor: true]
    }

}
