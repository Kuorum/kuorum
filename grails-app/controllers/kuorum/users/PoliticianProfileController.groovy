package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService
    CausesService causesService

}
