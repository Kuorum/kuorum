package kuorum.core.model.search

import grails.validation.Validateable
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


    static constraints = {
        word blank: false, nullable:false,minSize:1
        offset min: 0L
    }
}
