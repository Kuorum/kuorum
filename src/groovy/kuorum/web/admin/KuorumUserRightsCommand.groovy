package kuorum.web.admin

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * For editing user special fields
 */
@Validateable
class KuorumUserRightsCommand {

    KuorumUser user
    Boolean active
    Long relevance
    String password
    static constraints = {
        user nullable: false
        relevance nullable: true
        password nullable:true
        active nullable: true
    }
}
