package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.register.KuorumUserSession

/**
 * Created by iduetxe on 21/05/14.
 */
@Validateable
class SearchNotifications extends Pagination{
    KuorumUserSession user
    Boolean alerts
}
