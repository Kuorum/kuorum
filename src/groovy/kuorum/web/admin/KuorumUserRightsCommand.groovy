package kuorum.web.admin

import grails.validation.Validateable
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.users.RoleUser

/**
 * For editing user special fields
 */
@Validateable
class KuorumUserRightsCommand {

    KuorumUser user
    UserType userType
    Set<RoleUser> authorities
    Boolean emailAccountActive;
    Boolean active

    static constraints = {
        user nullable: false
        userType nullable: false
        authorities nullable: false, minSize: 1
    }
}
