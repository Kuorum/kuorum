package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 21/05/14.
 */
@Validateable
class SearchNotifications extends Pagination{
    KuorumUser user
    Boolean alerts
}
