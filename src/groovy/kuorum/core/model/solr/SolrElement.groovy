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
    String urlImage

    //Not used for indexig. Is only for recovering data. On indexation time is skipped
    SolrHighlighting highlighting = new SolrHighlighting(element:this)
}
