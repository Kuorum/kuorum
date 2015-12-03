package kuorum.campaign

import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class Campaign {
    /*
  Campaign is not abstract because grails(GORM) tries to instantiate Campaign, and
  then showing _class, cast the object to de corresponding class.
 */

    List<ObjectId> politicianIds
    Date startDate;
    Date endDate;

    static constraints = {
        politicianIds nullable: false; minSize: 1
    }
}
