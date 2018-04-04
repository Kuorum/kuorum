package kuorum.core.model.solr

/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER,
    POST,
    DEBATE,
    EVENT,
    SURVEY;

    static final SolrType safeParse(String rawType){
        SolrType solrType = null;
        try{
            SolrType.valueOf(rawType)
        }catch (Exception){
            solrType = null;
        }
        return solrType;
    }

}
