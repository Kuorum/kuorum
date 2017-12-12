package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import payment.campaign.PostService
import kuorum.solr.SearchSolrService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditProfilePicturesCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.PageCampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import payment.CustomerService
import payment.campaign.DebateService
import payment.campaign.MassMailingService
import payment.contact.ContactService

class DashboardController {

    SpringSecurityService springSecurityService
    def projectService
    def kuorumUserService
    KuorumUserStatsService kuorumUserStatsService
    CausesService causesService
    SearchSolrService searchSolrService
    ContactService contactService
    MassMailingService massMailingService
    DebateService debateService
    PostService postService
    DashboardService dashboardService
    CookieUUIDService cookieUUIDService
    CustomerService customerService;

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
        Map model = buildPaymentDashboard(user);
        model.put("tour", params.tour?true:false)
        if (dashboardService.forceUploadContacts()) {
            render view: "/dashboard/payment/paymentNoContactsDashboard", model: model
//          }else if (!model.numberCampaigns){
//              render view: "/dashboard/payment/paymentNoCampaignsDashboard", model: model
        }else{
            List<KuorumUser> recommendations = kuorumUserService.recommendUsers(user, new Pagination([max:50]))
            model.put("recommendations",recommendations)
            render view: "/dashboard/dashboard", model: model
        }
    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def skipContacts(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.skipUploadContacts = true;
        user.save()
        redirect mapping:'dashboard'
    }

    private def buildPaymentDashboard(KuorumUser user){
        String viewerUid = cookieUUIDService.buildUserUUID()

        PageCampaignRSDTO pageCampaigns = dashboardService.findAllContactsCampaigns(user, viewerUid)

        List<CampaignRSDTO> myCampaigns = massMailingService.findCampaigns(user)
        List<DebateRSDTO> myDebates = debateService.findAllDebates(user)
        List<PostRSDTO> myPosts = postService.findAllPosts(user)
        List<CampaignRSDTO> sentDebateNewsletters = myDebates*.newsletter.findAll{it.status==CampaignStatusRSDTO.SENT}
        List<CampaignRSDTO> sentPostNewsletters = myPosts*.newsletter.findAll{it.status==CampaignStatusRSDTO.SENT}
        List<CampaignRSDTO> sentMassMailCampaigns = myCampaigns.findAll{it.status==CampaignStatusRSDTO.SENT}
        List<CampaignRSDTO> sentCampaigns = sentMassMailCampaigns + sentDebateNewsletters + sentPostNewsletters
        Long numberCampaigns = sentCampaigns?.size()?:0;
        CampaignRSDTO lastCampaign = null
        Long durationDays = 0;
        if (sentCampaigns){
            lastCampaign = sentCampaigns.sort {it.sentOn}.last()?:null
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
                campaigns: pageCampaigns.data,
                totalCampaigns: pageCampaigns.total,
                showAuthor: true
        ]

    }

    //FAST CHAPU - Evaluating empty data
    def emptyEditableData(KuorumUser user){

        List<CauseRSDTO> causes = causesService.findDefendedCauses(user);

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
        Integer totalFields = fields*.total.sum();
        Integer emptyFields= fields.sum{it.uncompleted}
        return [
                percentage: (1 - emptyFields/totalFields)*100,
                fields:fields,

        ]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardCauses(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${causesSuggested.total < pagination.offset}")
        render template: "/dashboard/dashboardModules/causeCardList", model:[causes:causesSuggested.data]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardCampaigns(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String viewerUid = cookieUUIDService.buildUserUUID()
        Integer page = pagination.offset/pagination.max;
        PageCampaignRSDTO pageCampaigns = dashboardService.findAllContactsCampaigns(user, viewerUid, page)
        List<CampaignRSDTO> campaigns = pageCampaigns.data
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${pageCampaigns.total < (pagination.offset+pagination.max)}")
        render template: "/campaigns/cards/campaignsList", model:[campaigns:campaigns, showAuthor: true]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardPoliticians(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<KuorumUser> suggesterPoliticians=  kuorumUserService.recommendUsers(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${suggesterPoliticians.size() < pagination.max}")
        render template: "/dashboard/listDashboardUserRecommendations", model:[politicians:suggesterPoliticians]
    }

    def landingPrices(){
        Map<String, KuorumPaymentPlanDTO> plans = customerService.getActivePlans();
        [plans:plans    ]
    }
}
