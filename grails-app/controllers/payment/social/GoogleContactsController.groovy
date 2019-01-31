package payment.social

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUserService
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import payment.contact.ContactFromGoogleService

//@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class GoogleContactsController {

    ContactFromGoogleService contactFromGoogleService
    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(){
        KuorumUserSession user = springSecurityService.principal
        redirect(url: contactFromGoogleService.getUrlForAuthorization(getState(user)))
    }

    def loadContactsFromGoogle(){
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        def stateParams = com.mongodb.util.JSON.parse(URLDecoder.decode(params.state, "UTF-8"))
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(stateParams.userId)
        String code = params.code
        contactFromGoogleService.loadContacts(user, code)
        redirect(url:stateParams.callbackMultidomain)

    }

    private String getState(KuorumUserSession user){
        def params = [
                userId:user.id.toString(),
                callbackMultidomain:g.createLink(mapping:'politicianContactImportSuccess', absolute: true)
        ]
        (params as JSON).toString().encodeAsURL()
    }

}
