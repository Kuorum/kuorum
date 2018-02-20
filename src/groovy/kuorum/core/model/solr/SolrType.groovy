package kuorum.core.model.solr

/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER,
    POST,
    DEBATE,
    EVENT,
    SURVEY,

    @Deprecated PROJECT,
    @Deprecated ORGANIZATION,
    @Deprecated POLITICIAN,
    @Deprecated CANDIDATE;

}
