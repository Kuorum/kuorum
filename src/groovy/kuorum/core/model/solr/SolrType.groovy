package kuorum.core.model.solr

import kuorum.core.model.LawStatusType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 17/02/14.
 */
enum SolrType {

    POST({Post post ->
        switch (post.postType){
            case PostType.HISTORY: SolrSubType.HISTORY; break;
            case PostType.PURPOSE: SolrSubType.PURPOSE; break;
            case PostType.QUESTION: SolrSubType.QUESTION; break;
        }
    }),
    LAW({Law law->
        switch (law.status){
            case LawStatusType.OPEN: SolrSubType.OPEN; break;
            case LawStatusType.APPROVED: SolrSubType.APPROVED; break;
            case LawStatusType.REJECTED: SolrSubType.REJECTED; break;
        }
    }),
    KUORUM_USER({KuorumUser kuorumUser ->
        switch (kuorumUser.userType){
            case UserType.ORGANIZATION: SolrSubType.ORGANIZATION; break;
            case UserType.POLITICIAN: SolrSubType.POLITICIAN; break;
            case UserType.PERSON: SolrSubType.PERSON; break;
        }

    })

    public def generateSubtype
    SolrType(generateSubtype){
        this.generateSubtype = generateSubtype
    }

    List<SolrSubType> getSolrSubTypes(){
        SolrSubType.values().findAll{it.solrType==this}
    }
}
