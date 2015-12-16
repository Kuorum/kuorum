package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    def editKnownFor() {
        KuorumUser user = params.user

        [command:new ExternalPoliticianActivityCommand(politicianId:user.id, externalPoliticianActivities: user.externalPoliticianActivities)]
    }

    def updateKnownFor(ExternalPoliticianActivityCommand command){

        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianKnownFor'
    }
}
