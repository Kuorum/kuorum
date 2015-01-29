package kuorum.core.model.solr

import kuorum.core.model.ProjectStatusType
import kuorum.core.model.PostType
import kuorum.core.model.UserType

/**
 * Created by iduetxe on 17/02/14.
 */
enum SolrSubType {
    HISTORY(SolrType.POST), QUESTION(SolrType.POST),PURPOSE(SolrType.POST), //POST
    POLITICIAN(SolrType.KUORUM_USER),PERSON(SolrType.KUORUM_USER),ORGANIZATION(SolrType.KUORUM_USER),//KUORUM_USER
    OPEN(SolrType.PROJECT),REJECTED(SolrType.PROJECT),APPROVED(SolrType.PROJECT) //PROJECT

    SolrType solrType
    SolrSubType (SolrType solrType){
        this.solrType = solrType
    }

    static SolrSubType fromOriginalType(def originalType){
        if (originalType instanceof ProjectStatusType){
            SolrSubType.valueOf(originalType.toString())
        }else if (originalType instanceof UserType){
            SolrSubType.valueOf(originalType.toString())
        }else if (originalType instanceof PostType){
            SolrSubType.valueOf(originalType.toString())
        }
    }
}
