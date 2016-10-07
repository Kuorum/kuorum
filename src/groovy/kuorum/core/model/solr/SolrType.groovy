package kuorum.core.model.solr

import kuorum.core.model.ProjectStatusType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.project.Project
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
    PROJECT({ProjectStatusType projectStatusType->
        switch (projectStatusType){
            case ProjectStatusType.OPEN: SolrSubType.OPEN; break;
            case ProjectStatusType.APPROVED: SolrSubType.APPROVED; break;
            case ProjectStatusType.REJECTED: SolrSubType.REJECTED; break;
        }
    }),
    KUORUM_USER({KuorumUser kuorumUser ->
        switch (kuorumUser.userType){
            case UserType.ORGANIZATION: SolrSubType.ORGANIZATION; break;
            case UserType.POLITICIAN: SolrSubType.POLITICIAN; break;
            case UserType.PERSON: SolrSubType.PERSON; break;
            case UserType.CANDIDATE: SolrSubType.CANDIDATE; break;
        }

    }),
    ORGANIZATION({KuorumUser kuorumUser ->
        switch (kuorumUser.userType){
            case UserType.ORGANIZATION: SolrSubType.ORGANIZATION; break;
            case UserType.POLITICIAN: SolrSubType.POLITICIAN; break;
            case UserType.PERSON: SolrSubType.PERSON; break;
            case UserType.CANDIDATE: SolrSubType.CANDIDATE; break;
        }
    }),
    POLITICIAN({KuorumUser kuorumUser ->
        switch (kuorumUser.userType){
            case UserType.ORGANIZATION: SolrSubType.ORGANIZATION; break;
            case UserType.POLITICIAN: SolrSubType.POLITICIAN; break;
            case UserType.PERSON: SolrSubType.PERSON; break;
            case UserType.CANDIDATE: SolrSubType.CANDIDATE; break;
        }
    }),
    CANDIDATE({KuorumUser kuorumUser ->
        switch (kuorumUser.userType){
            case UserType.ORGANIZATION: SolrSubType.ORGANIZATION; break;
            case UserType.POLITICIAN: SolrSubType.POLITICIAN; break;
            case UserType.PERSON: SolrSubType.PERSON; break;
            case UserType.CANDIDATE: SolrSubType.CANDIDATE; break;
        }
    })

    public def generateSubtype
    SolrType(generateSubtype){
        this.generateSubtype = generateSubtype
    }

    List<SolrSubType> getSolrSubTypes(){
        SolrSubType.values().findAll{it.solrType==this}
    }

    static SolrType createFromUserType(UserType userType){
        switch (userType){
            case UserType.ORGANIZATION: SolrType.KUORUM_USER; break;
            case UserType.POLITICIAN: SolrType.POLITICIAN; break;
            case UserType.PERSON: SolrType.KUORUM_USER; break;
            case UserType.CANDIDATE: SolrType.CANDIDATE; break;
        }
    }
}
