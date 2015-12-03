package kuorum.campaign

import org.bson.types.ObjectId

class CampaignVote {

    ObjectId id
    /*
  CampaignVote is not abstract because grails(GORM) tries to instantiate CampaignVote, and
  then showing _class, cast the object to de corresponding class.
 */

    static constraints = {
    }
}
