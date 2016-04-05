package kuorum.users

import org.bson.types.ObjectId

class KuorumUserAudit {

    ObjectId id
    KuorumUser user;
//    KuorumUser snapshotUser;
    KuorumUser editor;
    Date dateCreated;

    Map<String, String> updates
    Map<String, String> snapshot


    static embedded = ['snapshot','updates']

    static constraints = {

    }
}


