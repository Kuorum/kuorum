package kuorum.admin

import kuorum.users.KuorumUser
import kuorum.users.PoliticianProfileController
import kuorum.users.PoliticianService
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand

class AdminPoliticianController extends AdminController{

    PoliticianService politicianService

    def editExternalActivity(){
        KuorumUser politician = KuorumUser.get(params.id)
        [command:new ExternalPoliticianActivityCommand(politician:politician, externalPoliticianActivities: politician.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        if (command.hasErrors()){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(command.politician, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'adminEditPoliticianExternalActivity', params: command.politician.encodeAsLinkProperties()
    }

    def editRelevantEvents(){
        KuorumUser politician = KuorumUser.get(params.id)
        [command:new RelevantEventsCommand(politician:politician, politicianRelevantEvents: politician.relevantEvents)]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        if (command.hasErrors()){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(command.politician, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'adminEditPoliticianRelevantEvents', params: command.politician.encodeAsLinkProperties()
    }

}
