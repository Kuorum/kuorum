package kuorum.core.model.solr

import grails.validation.Validateable

/**
 * Created by iduetxe on 18/02/14.
 *
 * Search options
 */
@Validateable
class SearchParams {
    /**
     * search text: min 3 character
     */
    String word

    /**
     * Filter by type: LAW, POST, USER
     */
    SolrType type

    /**
     * Filter by subtype
     */
    SolrSubType subType


    static constraints = {
        word blank: false, nullable:false,minSize:3
    }
}
