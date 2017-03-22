package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType

/**
 * Created by iduetxe on 7/05/14.
 */
@Validateable
class SearchProjects extends Pagination implements Serializable{

    ProjectStatusType projectStatusType

    CommissionType commissionType

    String regionName


    static constraints = {
        regionName nullable: false
        commissionType nullable: true
    }
}