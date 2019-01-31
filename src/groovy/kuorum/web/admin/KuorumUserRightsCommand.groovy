package kuorum.web.admin

import grails.validation.Validateable

/**
 * For editing user special fields
 */
@Validateable
class KuorumUserRightsCommand {

    String userId
    Boolean active
    Long relevance
    String password
    static constraints = {
        userId nullable: false
        relevance nullable: true
        password nullable:true
        active nullable: true
    }
}
