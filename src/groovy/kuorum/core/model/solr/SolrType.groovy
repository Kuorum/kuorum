package kuorum.core.model.solr

/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER,
    POST,

    @Deprecated PROJECT,
    @Deprecated ORGANIZATION,
    @Deprecated POLITICIAN,
    @Deprecated CANDIDATE;

}
