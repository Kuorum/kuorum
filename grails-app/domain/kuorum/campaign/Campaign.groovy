package kuorum.campaign

import org.bson.types.ObjectId

class Campaign {
    /*
  Campaign is not abstract because grails(GORM) tries to instantiate Campaign, and
  then showing _class, cast the object to de corresponding class.
 */

    ObjectId id;
    String name;
    List<ObjectId> politicianIds
    Date startDate;
    Date endDate;

    static constraints = {
        politicianIds nullable: false; minSize: 1
    }
}
