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
enum SearchType {
    ALL, REGION, NAME, CAUSE
}
