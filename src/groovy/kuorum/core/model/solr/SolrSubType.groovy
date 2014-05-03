package kuorum.core.model.solr

/**
 * Created by iduetxe on 17/02/14.
 */
enum SolrSubType {
    HISTORY(SolrType.POST), QUESTION(SolrType.POST),PURPOSE(SolrType.POST), //POST
    POLITICIAN(SolrType.KUORUM_USER),PERSON(SolrType.KUORUM_USER),ORGANIZATION(SolrType.KUORUM_USER),//KUORUM_USER
    OPEN(SolrType.LAW),REJECTED(SolrType.LAW),APPROVED(SolrType.LAW) //LAW

    SolrType solrType
    SolrSubType (SolrType solrType){
        this.solrType = solrType
    }
}
