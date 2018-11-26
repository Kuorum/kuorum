package kuorum.web.admin

import grails.validation.Validateable

/**
 * Created by toni on 12/7/17.
 */
@Validateable
class KuorumUserEmailSenderCommand {

    String mandrillAppKey

    static constraints = {
        mandrillAppKey nullable:true
    }

}
