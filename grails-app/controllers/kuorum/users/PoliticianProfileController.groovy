package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService
    CausesService causesService

}
