package kuorum.core.model.solr

/**
 * Created by iduetxe on 20/02/14.
 */
class SolrResults {
    List<SolrElement> elements
    Long numResults
    List<SolrFacets> facets
    SolrSuggest suggest
}
