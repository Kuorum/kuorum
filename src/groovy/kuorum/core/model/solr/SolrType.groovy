package kuorum.core.model.solr

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
        law.open?SolrSubType.OPEN:SolrSubType.CLOSE
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
}
