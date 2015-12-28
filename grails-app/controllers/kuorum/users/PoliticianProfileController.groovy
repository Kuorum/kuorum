package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService

    def editExternalActivity() {
        KuorumUser user = params.user

        [command:new ExternalPoliticianActivityCommand(politician:user, externalPoliticianActivities: user.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(params.user, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianExternalActivity'
    }

    def editRelevantEvents(){
        KuorumUser user = params.user
        [command:new RelevantEventsCommand(politician:user, politicianRelevantEvents: user.relevantEvents)]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(params.user, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianRelevantEvents'
    }

    def editProfessionalDetails(){
        KuorumUser user = params.user
        [command:new ProfessionalDetailsCommand(politician:user, professionalDetails: user.professionalDetails)]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(user, command.professionalDetails)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianProfessionalDetails', params: user.encodeAsLinkProperties()
    }
}
