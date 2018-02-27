package kuorum.core.model.solr
/**
 * Created by iduetxe on 17/02/14.
 */
class SolrElement {
    String id
    String name
    SolrType type
    Date dateCreated
    String regionName
    List<String> tags
    String urlImage
    String text
    String alias

    //Not used for indexing. Is only for recovering data. On indexation time is skipped
    SolrHighlighting highlighting = new SolrHighlighting(element:this)
}
