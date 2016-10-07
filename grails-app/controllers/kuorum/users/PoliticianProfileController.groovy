package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService
    CausesService causesService

    def editProfessionalDetails(){
        KuorumUser politician = params.user
        ProfessionalDetailsCommand command = new ProfessionalDetailsCommand(politician)
        [command:command]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editProfessionalDetails", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(user, command)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianProfessionalDetails', params: user.encodeAsLinkProperties()
    }
}
