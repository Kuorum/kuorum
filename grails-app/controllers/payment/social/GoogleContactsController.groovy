package payment.social

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import payment.contact.ContactFromGoogleService

//@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class GoogleContactsController {

    ContactFromGoogleService contactFromGoogleService;
    SpringSecurityService springSecurityService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        redirect(url: contactFromGoogleService.getUrlForAuthorization(getState(user)))
    }

    def loadContactsFromGoogle(){
//        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        def stateParams = com.mongodb.util.JSON.parse(URLDecoder.decode(params.state, "UTF-8"));
        KuorumUser user = KuorumUser.get(new ObjectId(stateParams.userId))
        String code = params.code
        contactFromGoogleService.loadContacts(user, code)
        redirect(url:stateParams.callbackMultidomain)

    }

    private String getState(KuorumUser user){
        def params = [
                userId:user.id.toString(),
                callbackMultidomain:g.createLink(mapping:'politicianContactImportSuccess', absolute: true)
        ]
        (params as JSON).toString().encodeAsURL()
    }

}
