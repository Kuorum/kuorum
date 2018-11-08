package kuorum.core.model.solr

/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER("fa-user"),
    POST("fa-newspaper"),
    DEBATE("fa-comments"),
    EVENT("fa-calendar-check"),
    SURVEY("fa-chart-pie"),
    PETITION("fa-microphone"),
    PARTICIPATORY_BUDGET("fa-money-bill-alt"),
    DISTRICT_PROPOSAL("fa-rocket");

    String faIcon;

    SolrType(String faIcon) {
        this.faIcon = faIcon
    }

    String getFaIcon() {
        return faIcon
    }

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
