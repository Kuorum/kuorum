package kuorum.debate

import grails.plugin.springsecurity.annotation.Secured
import kuorum.campaign.Event
import kuorum.campaign.EventRegistration
import kuorum.files.FileService
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import net.sf.json.JSON
import payment.CustomerService
import payment.campaign.DebateService
import payment.campaign.ProposalService
import payment.contact.ContactService

class EventController {

    def springSecurityService

    KuorumUserService kuorumUserService
    FileService fileService
    ContactService contactService
    DebateService debateService
    ProposalService proposalService
    CookieUUIDService cookieUUIDService
    CustomerService customerService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def confirmAssistance(Long debateId){
        KuorumUser user = springSecurityService.currentUser
        Event event = Event.findByDebateId(debateId)
        if (!event){
            render ([success:false, error:"Debate not exists"]) as JSON
            return;
        }
        EventRegistration eventRegistration = EventRegistration.findByDebateIdAndUserId(debateId, user.getId())
        if (eventRegistration){
            render ([success:true, error:"Already register"]) as JSON
        }else{
            eventRegistration = new EventRegistration()
            eventRegistration.debateId = debateId
            eventRegistration.postId = 0
            eventRegistration.alias = user.alias
            eventRegistration.dateCreated = new Date();
            eventRegistration.userId = user.id
            eventRegistration.name = user.fullName
            eventRegistration.email = user.email
            eventRegistration.language = user.language
            if (eventRegistration.save()){
                render ([success:true, error:""]) as JSON
            }else{
                render ([success:false, error:"Error saving registration"]) as JSON
            }


        }
    }
}
