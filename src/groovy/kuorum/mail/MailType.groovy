package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_ACCOUNT (false,  "validationEmail",      "registerUser", ["fname","verifyLink"], []),
    NOTIFICATION_CLUCK      (true,   "notificationCluck",    "notification", ["fname", "postName"],  ["cluckUserName","cluckUserLink"]),
    NOTIFICATION_FOLLOWER   (true,   "notificationFollower", "notification", [],  ["followerName","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE(true,"notificationMilestone","notification",[],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE     (true,   "notificationDebate",   "notification",[],["debateOwner","postName", "politicianName","message", "politicianLink", "postLink"]),
    ALERT_DEBATE            (false,   "alertDebate",          "notification",[],["postName", "politicianName","message", "politicianLink", "postLink"]);


    String nameTemplate
    String tagTemplate
    List<String> requiredBindings
    List<String> globalBindings
    Boolean configurable

    MailType(Boolean configurable, String nameTemplate, String tagTemplate, List<String> requiredBindings, List<String> globalBindings){
        this.nameTemplate = nameTemplate
        this.tagTemplate = tagTemplate
        this.requiredBindings = requiredBindings
        this.globalBindings = globalBindings
        this.configurable = configurable
    }

}