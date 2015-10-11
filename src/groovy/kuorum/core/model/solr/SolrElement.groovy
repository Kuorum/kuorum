package kuorum.core.model.solr

import kuorum.core.model.CommissionType

/**
 * Created by iduetxe on 17/02/14.
 */
class SolrElement {
    String id
    String name
    SolrType type
    @Deprecated
    SolrSubType subType
    Date dateCreated
    String regionName
    String regionIso3166_2
    String institutionName //Project, Post and Politicians (Normal user and Organizations will be null)
    List<CommissionType> commissions
    List<String> tags
    String urlImage

    //Not used for indexig. Is only for recovering data. On indexation time is skipped
    SolrHighlighting highlighting = new SolrHighlighting(element:this)
}
