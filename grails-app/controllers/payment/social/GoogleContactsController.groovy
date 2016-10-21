package payment.social

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import payment.contact.ContactFromGoogleService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class GoogleContactsController {

    ContactFromGoogleService contactFromGoogleService;
    SpringSecurityService springSecurityService

    def index(){
        redirect(url: contactFromGoogleService.getUrlForAuthorization(getRedirectUrl()))
    }

    def loadContactsFromGoogle(){
        KuorumUser user = springSecurityService.currentUser
        String code = params.code
        contactFromGoogleService.loadContacts(user, code, getRedirectUrl())
        redirect(mapping: "politicianContactImportGmailSuccess")

    }

    private String getRedirectUrl(){
        g.createLink(controller: 'googleContacts', action: 'loadContactsFromGoogle', absolute: true)
    }

    def success(){

    }
}
