package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.core.model.solr.SolrSubType
import kuorum.core.model.solr.SolrType

/**
 * Created by iduetxe on 18/02/14.
 *
 * Search options
 *
 * It is serializable because is going to be stored on user session (cookie)
 */
@Validateable
class SearchParams extends Pagination implements Serializable{
    /**
     * search text: min 3 character
     */
    String word

    /**
     * Filter by type: PROJECT, POST, USER
     */
    SolrType type

    CommissionType commissionType

    List<String> regionIsoCodes;

    List<String> filteredUserIds;


    static constraints = {
        word nullable:true
        commissionType nullable: true
        offset min: 0L
        regionIsoCodes nullable:true
        filteredUserIds nullable:true
    }
}
