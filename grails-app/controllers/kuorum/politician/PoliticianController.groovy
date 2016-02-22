package kuorum.politician

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.users.PoliticianService

@Secured("ROLE_POLITICIAN")
class PoliticianController {

    SpringSecurityService springSecurityService
    PoliticianService politicianService;

    def requestAPoliticianBetaTester() {
        KuorumUser loggedUser = springSecurityService.currentUser
        politicianService.requestABetaTesterAccount(loggedUser)
        redirect mapping:'dashboard'
    }

    def betaTesterPage(){
        KuorumUser loggedUser = springSecurityService.currentUser
        [user:loggedUser]
    }
}
