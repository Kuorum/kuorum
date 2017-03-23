package kuorum.core.model.solr

/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER,
    POST,
    DEBATE,

    @Deprecated PROJECT,
    @Deprecated ORGANIZATION,
    @Deprecated POLITICIAN,
    @Deprecated CANDIDATE;

}
