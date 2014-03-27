package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_EMAIL               (false,"03_validationEmail",           "registerUser", ["confirmationLink"], []),
    REGISTER_RESET_PASSWORD             (false,"03A_resetPassword",            "registerUser", ["reserPasswordLink"], []),
    REGISTER_RRSS                       (false,"04_validationEmailRRSS",       "registerUser", [], []),
    REGISTER_ACCOUNT_COMPLETED          (false,"05_registerCompleted",         "registerUser", [], []),

    NOTIFICATION_CLUCK                  (true, "07_notificationCluck",         "notification", [],  ["clucker","cluckerLink","postName"]),
    NOTIFICATION_FOLLOWER               (true, "08_notificationFollower",      "notification", [],  ["follower","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE       (true, "09_notificationMilestone",     "notification",[],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE_USERS           (true, "15_notificationDebateUsers",   "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_AUTHOR          (true, "13_notificationDebateAuthor",  "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_POLITICIAN      (true, "14_notificationDebatePolitician", "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEFENDED_USERS         (true, "19_notificationDefendedUsers", "notification",[],   ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_AUTHOR        (true, "16_notificationDefendedAuthor","notification",[],   ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_BY_POLITICIAN (true, "17_notificationDefendedByPolitician","notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_POLITICIANS   (true, "18_notificationDefendedPoliticians", "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_USERS          (true, "21_notificationVictoryUsers","notification",[],  ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_DEFENDER       (true, "20_notificationVictoryDefender","notification",[],  ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),

    PROMOTION_OWNER        (true, "10_notificationPromotionAuthor",    "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),
    PROMOTION_SPONSOR      (true, "11_notificationPromotionPromoter",  "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),
    PROMOTION_USERS        (true, "12_notificationPromotionList",      "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),

    POST_CREATED_1         (false, "02_publishedAuthor",      "promotion",[],["postType"]),
    POST_CREATED_2         (false, "02A_timePublishedAuthor", "promotion",[],["postType"]),
    POST_CREATED_3         (false, "02B_timePublishedAuthor", "promotion",[],["postType"]),
    POST_CREATED_4         (false, "02C_timePublishedAuthor", "promotion",[],["postType"]);


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