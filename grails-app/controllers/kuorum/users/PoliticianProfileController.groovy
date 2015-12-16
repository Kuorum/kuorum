package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService

    def editExternalActivity() {
        KuorumUser user = params.user

        [command:new ExternalPoliticianActivityCommand(politicianId:user.id, externalPoliticianActivities: user.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        KuorumUser user = params.user
        if (request.isUserInRole("ROLE_ADMIN")){
            user  = KuorumUser.get(command.politicianId)
        }
        if (command.hasErrors() || !user ){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(params.user, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianExternalActivity'
    }
}
