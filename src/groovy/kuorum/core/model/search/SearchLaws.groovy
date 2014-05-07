package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.core.model.LawStatusType

/**
 * Created by iduetxe on 7/05/14.
 */
@Validateable
class SearchLaws extends Pagination implements Serializable{

    LawStatusType lawStatusType

    String regionName

    CommissionType commissionType


    static constraints = {
        regionName nullable: false
        commissionType nullable: true
    }
}