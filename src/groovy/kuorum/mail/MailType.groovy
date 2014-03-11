package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_ACCOUNT             (false,"validationEmail",              "registerUser", ["fname","verifyLink"], []),
    NOTIFICATION_CLUCK                  (true, "notificationCluck",            "notification", ["fname", "postName"],  ["cluckUserName","cluckUserLink"]),
    NOTIFICATION_FOLLOWER               (true, "notificationFollower",         "notification", [],  ["followerName","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE       (true, "notificationMilestone",        "notification",[],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE_USERS           (true, "notificationDebateUsers",      "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_AUTHOR          (true, "notificationDebateAuthor",     "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_POLITICIAN      (true, "notificationDebatePolitician", "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEFENDED_USERS         (true, "notificationDefendedUsers",    "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_AUTHOR        (true, "notificationDefendedAuthor",   "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_BY_POLITICIAN (true, "notificationDefendedByPolitician","notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_POLITICIANS   (true, "notificationDefendedPoliticians", "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY                (true, "notificationVictory",           "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"])



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