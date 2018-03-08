package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.CommissionType
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
//    SolrType type = SolrType.KUORUM_USER
    SolrType type

    SearchType searchType = SearchType.ALL

    List<String> filteredUserIds;

    List<String> boostedRegions;


    static constraints = {
        word nullable:true
        offset min: 0L
        filteredUserIds nullable:true
        searchType nullable:true
        type nullable:true
        boostedRegions nullable: true
    }
}
