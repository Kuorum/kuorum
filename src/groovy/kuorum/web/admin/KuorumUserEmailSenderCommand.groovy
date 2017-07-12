package kuorum.web.admin

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by toni on 12/7/17.
 */
@Validateable
class KuorumUserEmailSenderCommand {

    KuorumUser user;
    String emailSender;

    static constraints = {
        emailSender nullable:true
    }

}
