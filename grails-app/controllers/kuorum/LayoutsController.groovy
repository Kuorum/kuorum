package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.core.model.UserType
import kuorum.core.model.search.SearchNotifications
import kuorum.notifications.Notification
import kuorum.users.KuorumUser
import kuorum.users.PersonalData
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.OfficeDetails
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand
import org.kuorum.rest.model.tag.CauseRSDTO

class LayoutsController {

    def springSecurityService
    def notificationService
    def postService
    CausesService causesService;

    private static final MAX_HEAD_NOTIFICATIONS = 4

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHead() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SearchNotifications searchNotificationsCommand = new SearchNotifications(user:user, max: MAX_HEAD_NOTIFICATIONS)
        List<Notification> listNotifications = notificationService.findUserNotifications(searchNotificationsCommand)
        Integer numNewNotifications = listNotifications.findAll{it.dateCreated>user.lastNotificationChecked}.size()
        def notifications =[
                list: listNotifications,
                numNews: numNewNotifications
        ]
        render template:'/layouts/userHead', model:[user:user, notifications:notifications, emptyFields:emptyEditableData(user)]
    }

    //FAST CHAPU
    def emptyEditableData(KuorumUser user){
        if (user.userType == UserType.POLITICIAN){
            List<CauseRSDTO> causes = causesService.findDefendedCauses(user);

            List fields = [
                [urlMapping: 'profileEditAccountDetails', total: (new AccountDetailsCommand(user)).properties?.findAll{!it.value && !["password"].contains(it.key)}.size()],
                [urlMapping: 'profilePoliticianCauses', total:causes?0:1],
                [urlMapping: 'profileEditUser', total:new EditUserProfileCommand(user).properties.findAll{!it.value && !["birthday", "workingSector", "studies", "enterpriseSector"].contains(it.key)}.size()],
                [urlMapping: 'profilePoliticianRelevantEvents', total:user.relevantEvents?0:1],
                [urlMapping: 'profileSocialNetworks', total:(new SocialNetworkCommand(user)).properties.findAll{!it.value}.size()],
                [urlMapping: 'profilePoliticianExternalActivity', total:user.externalPoliticianActivities?0:1],
                [urlMapping: 'profilePoliticianExperience', total:user.timeLine?0:1]
            ]
            QuickNotesCommand quickNotesCommand = new QuickNotesCommand(user);
            fields.add([urlMapping:'profilePoliticianQuickNotes', total:
                    quickNotesCommand.institutionalOffice.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size() +
                    quickNotesCommand.politicalOffice.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size() +
                    quickNotesCommand.politicianExtraInfo.properties.findAll{!it.value && !["dbo", "externalId"].contains(it.key)}.size()]
            )

            ProfessionalDetailsCommand professionalDetailsCommand = new ProfessionalDetailsCommand(user)
            fields.add([urlMapping: 'profilePoliticianProfessionalDetails', total:
                    professionalDetailsCommand.properties.findAll{!it.value}.size() +
                            professionalDetailsCommand.careerDetails.properties.findAll{!it.value && !["dbo"].contains(it.key)}.size()
            ])

            Integer totalFields = 54; // FAST CHAPU
            Integer emptyFields= fields.sum{it.total}
            return [
                    percentage: (1 - emptyFields/totalFields)*100,
                    fields:fields
            ]
        }else{
            return [
                    percentage: 100,
                    fields:[]
            ]
        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def headMessagesChecked(){

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userHeadNoLinks(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
}
