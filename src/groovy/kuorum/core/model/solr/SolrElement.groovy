package kuorum.core.model.solr

import kuorum.core.model.CommissionType

/**
 * Created by iduetxe on 17/02/14.
 */
class SolrElement {
    String id
    String name
    SolrType type
    Date dateCreated
    String regionName
    String regionIso3166_2
    Long regionIso3166_2Length
    List<String> tags
    String urlImage
    Long kuorumRelevance
    String text

    //Not used for indexig. Is only for recovering data. On indexation time is skipped
    SolrHighlighting highlighting = new SolrHighlighting(element:this)
}
