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

    String institutionName

    CommissionType commissionType


    static constraints = {
        institutionName nullable: false
        commissionType nullable: true
    }
}