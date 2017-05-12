package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import kuorum.post.Cluck
import kuorum.post.PostService
import kuorum.project.Project
import kuorum.solr.SearchSolrService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditProfilePicturesCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
import payment.campaign.DebateService
import payment.campaign.MassMailingService
import payment.contact.ContactService
import springSecurity.KuorumRegisterCommand

class DashboardController {

    SpringSecurityService springSecurityService
    def cluckService
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

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        return landingLeaders()
    }
    def dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        log.info("Dashboard ${user}")
//        if (kuorumUserService.isPaymentUser(user)){
            Map model = buildPaymentDashboard(user);
            model.put("tour", params.tour?true:false)
            if (dashboardService.forceUploadContacts()) {
                render view: "/dashboard/payment/paymentNoContactsDashboard", model: model
//            }else if (!model.numberCampaigns){
//                render view: "/dashboard/payment/paymentNoCampaignsDashboard", model: model
            }else{
                List<KuorumUser> recommendations = kuorumUserService.recommendUsers(user, new Pagination([max:50]))
                model.put("recommendations",recommendations)
                render view: "/dashboard/payment/paymentDashboard", model: model
            }
//        }else{
//            buildUserDashboard(user)
//        }
    }
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def skipContacts(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.skipUploadContacts = true;
        user.save()
        redirect mapping:'dashboard'
    }

//    private Map buildUserDashboard(KuorumUser user){
//        Pagination causesPagination = new Pagination(max:6)
//        SuggestedCausesRSDTO causesSuggested = causesService.suggestCauses(user, causesPagination)
//        Pagination politiciansDashboardPagination = new Pagination(max:6)
//        List<KuorumUser> politicians = kuorumUserService.recommendPoliticians(user,politiciansDashboardPagination)
//        [
//                loggedUser:user,
//                causesSuggested:causesSuggested,
//                causesPagination:causesPagination,
//                politicians:politicians,
//                politiciansDashboardPagination:politiciansDashboardPagination
//        ]
//    }

    private def buildPaymentDashboard(KuorumUser user){
        String viewerUid = cookieUUIDService.buildUserUUID()

        PagePostRSDTO pagePosts = dashboardService.findAllContactsPosts(user, viewerUid)
        List<PostRSDTO> posts = pagePosts.data
        PageDebateRSDTO pageDebates = dashboardService.findAllContactsDebates(user)
        List<DebateRSDTO> debates = pageDebates.data

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
                debates: debates,
                posts: posts,
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
        //Integer totalFields = 9+3+2+8+1+1; // FAST CHAPU
        Integer totalFields = fields*.total.sum(); // FAST CHAPU
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

    private def splitClucksInParts(List<Cluck> clucks){
        if (clucks && clucks.size() > 2){
            return [clucks_1:clucks[0..1], clucks_2:clucks[2..clucks.size()-1]]
        }
        return [clucks_1:clucks, clucks_2:[]]
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardPoliticians(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<KuorumUser> suggesterPoliticians=  kuorumUserService.recommendUsers(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${suggesterPoliticians.size() < pagination.max}")
        render template: "/dashboard/listDashboardUserRecommendations", model:[politicians:suggesterPoliticians]
    }

    def landingPoliticians(){
        [command: new KuorumRegisterCommand()]
    }

    def discover(){
        //TODO: QUE HACER SI NO ES ESPA�A
        Region region = Region.findByIso3166_2("EU-ES")
        redirect mapping:'discoverProjectsByRegion', params: region.encodeAsLinkProperties()
//        List<Project> relevantProjects = projectService.relevantProjects(new Pagination(max:4))
//        List<Project> recommendedProjects = projectService.relevantProjects(new Pagination(max:5))
//        List<KuorumUser> mostActiveUsers = kuorumUserService.mostActiveUsersSince(new Date() -7, new Pagination(max:24))
////        List<KuorumUser> bestSponsors = kuorumUserService.bestSponsorsSince(new Date() -7, new Pagination(max:24))
//        List<KuorumUser> bestSponsors = kuorumUserService.mostActiveUsersSince(new Date() -365, new Pagination(max:24))
//        List<KuorumUser> bestPoliticians = kuorumUserService.bestPoliticiansSince(new Date() -7, new Pagination(max:32))
////        log.debug("discover")
//        [
//                relevantProjects:relevantProjects,
//                recommendedProjects:recommendedProjects,
//                mostActiveUsers:mostActiveUsers,
//                bestSponsors:bestSponsors,
//                bestPoliticians:bestPoliticians
//        ]
    }


    def customPostMapping(String customLink){
        Project project = Project.findByHashtag("#losDatosCuentan")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def customPostMappingEmpleoJuvenil(){
        Project project = Project.findByHashtag("#empleoJuvenil")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def customPostMappingSayNoToFracking(){
        Project project = Project.findByHashtag("#sayNoToFracking")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }
    def customPostMappingImmigrationrc(){
        Project project = Project.findByHashtag("#immigrationRC")
        redirect mapping: "projectShow" , params: project.encodeAsLinkProperties()
    }

    def landingCitizens(){
        [command: new KuorumRegisterCommand()]
    }

    def landingPrices(){
        []
    }

    def landingLeaders(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingLeaders", model: [command: new KuorumRegisterCommand()])
        }
    }

    def landingCorporations(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingCorporations", model: [command: new KuorumRegisterCommand()])
        }
    }

    def landingCorporationsBrands(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingCorporationsBrands", model: [command: new KuorumRegisterCommand()])
        }
    }

    def landingOrganizations(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingOrganizations", model: [command: new KuorumRegisterCommand()])
        }
    }
}
