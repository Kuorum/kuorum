package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.UserType
import kuorum.core.model.search.Pagination
import kuorum.post.Cluck
import kuorum.project.Project
import kuorum.project.ProjectEvent
import kuorum.solr.SearchSolrService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserStatsService
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.notification.campaign.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SuggestedCausesRSDTO
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

    private  static final Integer MAX_PROJECT_EVENTS = 2

    def index(){
        if (springSecurityService.isLoggedIn()){
//            render(view: "dashboard", model: dashboard())
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingPoliticians", model: landingPoliticians())
        }
    }
    def dashboard() {
        if (!springSecurityService.isLoggedIn()){
            redirect(mapping:"home")
            return
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
//        if (kuorumUserService.isPaymentUser(user)){
            Map model = buildPaymentDashboadr(user);
            if (model.contacts.total==0) {
                render view: "/dashboard/payment/paymentNoContactsDashboard", model: model
            }else if (!model.campaigns?.size()){
                render view: "/dashboard/payment/paymentNoCampaignsDashboard", model: model
            }else{
                render view: "/dashboard/payment/paymentDashboard", model: model
            }
//        }else{
//            buildUserDashboard(user)
//        }
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

    private def buildPaymentDashboadr(KuorumUser user){
        List<CampaignRSDTO> campaigns = massMailingService.findCampaigns(user)
        CampaignRSDTO lastCampaign = null
        List<CampaignRSDTO> sentCampaigns = campaigns.findAll{it.status==CampaignStatusRSDTO.SENT}
        Long durationDays = 0;
        if (sentCampaigns){
            lastCampaign = sentCampaigns.sort {it.sentOn}.last()?:null
            use(groovy.time.TimeCategory) {
                def duration = new Date() - lastCampaign.sentOn
                durationDays = duration.days
            }
        }
        List<KuorumUser> recommendedUsers = kuorumUserService.recommendPoliticians(user, new Pagination(max:16))

        [
                lastCampaign:lastCampaign,
                campaigns:campaigns,
                durationDays:durationDays,
                contacts: contactService.getUsers(user),
                recommendedUsers:recommendedUsers,
                user:user,
                emptyEditableData:emptyEditableData(user)
        ]

    }

    //FAST CHAPU - Evaluating empty data
    def emptyEditableData(KuorumUser user){
        if (user.userType == UserType.POLITICIAN || user.userType == UserType.CANDIDATE){
            List<CauseRSDTO> causes = causesService.findDefendedCauses(user);

            List fields = [
                    [urlMapping: 'profileEditAccountDetails', total: (new AccountDetailsCommand(user)).properties?.findAll{!it.value && !["password"].contains(it.key)}.size()],
                    [urlMapping: 'profileCauses', total:causes?0:1],
                    [urlMapping: 'profileEditUser', total:new EditUserProfileCommand(user).properties.findAll{!it.value && !["birthday", "workingSector", "studies", "enterpriseSector"].contains(it.key)}.size()],
                    [urlMapping: 'profileNews', total:user.relevantEvents?0:1],
                    [urlMapping: 'profileSocialNetworks', total:(new SocialNetworkCommand(user)).properties.findAll{!it.value}.size()]
            ]
            QuickNotesCommand quickNotesCommand = new QuickNotesCommand(user);
            fields.add([urlMapping:'profileQuickNotes', total:
                    quickNotesCommand.institutionalOffice.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size() +
                            quickNotesCommand.politicalOffice.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size() +
                            quickNotesCommand.politicianExtraInfo.properties.findAll{!it.value && !["dbo", "externalId"].contains(it.key)}.size()]
            )

            ProfessionalDetailsCommand professionalDetailsCommand = new ProfessionalDetailsCommand(user)
            fields.add([urlMapping: 'profileProfessionalDetails', total:
                    professionalDetailsCommand.properties.findAll{!it.value}.size() +
                            professionalDetailsCommand.careerDetails.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size()
            ])

            Integer totalFields = 52; // FAST CHAPU
            Integer emptyFields= fields.sum{it.total}
            return [
                    percentage: (1 - emptyFields/totalFields)*100,
                    fields:fields
            ]
        }else{
            List fields = [
                    [urlMapping: 'profileEditAccountDetails', total: (new AccountDetailsCommand(user)).properties?.findAll{!it.value && !["password"].contains(it.key)}.size()],
                    [urlMapping: 'profileEditUser', total:new EditUserProfileCommand(user).properties.findAll{!it.value && !["position", "politicalParty"].contains(it.key)}.size()],
                    [urlMapping: 'profileSocialNetworks', total:4],
            ]
            Integer totalFields = 8+7+4; // FAST CHAPU
            Integer emptyFields= fields.sum{it.total}
            return [
                    percentage: (1 - emptyFields/totalFields)*100,
                    fields:fields
            ]
        }
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
        List<KuorumUser> suggesterPoliticians=  kuorumUserService.recommendPoliticians(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${suggesterPoliticians.size() < pagination.max}")
        render template: "/dashboard/listDashboardPoliticians", model:[politicians:suggesterPoliticians]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def dashboardClucks(Pagination pagination){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        List<ProjectEvent> projectEvents = projectService.findRelevantProjectEvents(user, new Pagination(max: MAX_PROJECT_EVENTS, offset: pagination.offset))
        List<Cluck> clucks =  cluckService.dashboardClucks(user, pagination)
        response.setHeader(WebConstants.AJAX_END_INFINITE_LIST_HEAD, "${clucks.size()<pagination.max}")
        render template: "/cluck/liClucks", model:[clucks:clucks, projectEvents:projectEvents]
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
}
