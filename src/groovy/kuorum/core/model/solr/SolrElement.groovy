package kuorum.core.model.solr

import kuorum.core.model.CommissionType

/**
 * Created by iduetxe on 17/02/14.
 */
class SolrElement {
    String id
    String name
    SolrType type
    SolrSubType subType
    Date dateCreated
    String regionName
    String regionIso3166_2
    List<CommissionType> commissions
}
