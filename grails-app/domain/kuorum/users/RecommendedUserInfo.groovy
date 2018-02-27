package kuorum.users

import org.bson.types.ObjectId

@Deprecated
class RecommendedUserInfo {


    KuorumUser user

    List<ObjectId> recommendedUsers  = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> deletedRecommendedUsers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users

    static constraints = {
        recommendedUsers maxSize: 100
    }
}
