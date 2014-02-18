package kuorum.core.model.solr

import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.users.Organization
import kuorum.users.Person
import kuorum.users.Politician

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
        law.open?SolrSubType.OPEN:SolrSubType.CLOSE
    }),
    KUORUM_USER({KuorumUser kuorumUser ->
        switch (kuorumUser){
            case Organization: SolrSubType.ORGANIZATION; break;
            case Politician: SolrSubType.POLITICIAN; break;
            case Person: SolrSubType.PERSON; break;
        }

    })

    public def generateSubtype
    SolrType(generateSubtype){
        this.generateSubtype = generateSubtype
    }
}
