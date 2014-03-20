package kuorum.users

import org.bson.types.ObjectId

class FacebookUser {
    ObjectId id
    String accessToken
    long uid

    Date accessTokenExpires


    static belongsTo = [user: KuorumUser] //connected to main Spring Security domain

    static constraints = {
        uid unique: true
    }

}
