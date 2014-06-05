package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_EMAIL               (MailGroupType.NOT_CONFIGURABLE,"03_validationEmail",           "registerUser", ["confirmationLink"], []),
    REGISTER_RESET_PASSWORD             (MailGroupType.NOT_CONFIGURABLE,"03A_resetPassword",            "registerUser", ["resetPasswordLink"], []),
    REGISTER_RRSS                       (MailGroupType.NOT_CONFIGURABLE,"04_validationEmailRRSS",       "registerUser", [], ["provider"]),
    REGISTER_ACCOUNT_COMPLETED          (MailGroupType.NOT_CONFIGURABLE,"05_registerCompleted",         "registerUser", [], []),

    NOTIFICATION_CLUCK                  (MailGroupType.MAIL_RELATED_WITH_ME,            "07_notificationCluck",         "notification", [],  ["postType", "clucker","cluckerLink","postName", "postLink"]),
    NOTIFICATION_FOLLOWER               (MailGroupType.MAIL_RELATED_WITH_ME,            "08_notificationFollower",      "notification", [],  ["follower","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE       (MailGroupType.MAIL_RELATED_WITH_ME,            "09_notificationMilestone",     "notification",[],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE_USERS           (MailGroupType.MAIL_RELATED_WITH_OTHER_USERS,   "15_notificationDebateUsers",   "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_AUTHOR          (MailGroupType.MAIL_RELATED_WITH_ME,            "13_notificationDebateAuthor",  "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_POLITICIAN      (MailGroupType.POLITICIAN_MAIL,                 "14_notificationDebatePolitician", "notification",[],["postType","debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEFENDED_USERS         (MailGroupType.MAIL_RELATED_WITH_OTHER_USERS,   "19_notificationDefendedUsers", "notification",[],   ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_AUTHOR        (MailGroupType.MAIL_RELATED_WITH_ME,            "16_notificationDefendedAuthor","notification",[],   ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_BY_POLITICIAN (MailGroupType.MAIL_RELATED_WITH_POLITICIANS,   "17_notificationDefendedByPolitician","notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_POLITICIANS   (MailGroupType.POLITICIAN_MAIL,                 "18_notificationDefendedPoliticians", "notification",[],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_USERS          (MailGroupType.MAIL_RELATED_WITH_OTHER_USERS,   "21_notificationVictoryUsers","notification",[],  ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_DEFENDER       (MailGroupType.POLITICIAN_MAIL,                 "20_notificationVictoryDefender","notification",[],  ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),

    PROMOTION_OWNER                     (MailGroupType.MAIL_RELATED_WITH_ME,            "10_notificationPromotionAuthor",    "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),
    PROMOTION_SPONSOR                   (MailGroupType.MAIL_RELATED_WITH_ME,            "11_notificationPromotionPromoter",  "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),
    PROMOTION_USERS                     (MailGroupType.MAIL_RELATED_WITH_OTHER_USERS,   "12_notificationPromotionList",      "promotion",[],["postType","postName","postLink", "promoter","promoterLink","postOwner","postOwnerLink","hashtag","hashtagLink"]),

    POST_CREATED_1                      (MailGroupType.NOT_CONFIGURABLE, "02_publishedAuthor",      "promotion",[],["postType"]),
    POST_CREATED_2                      (MailGroupType.NOT_CONFIGURABLE, "02A_timePublishedAuthor", "promotion",[],["postType"]),
    POST_CREATED_3                      (MailGroupType.NOT_CONFIGURABLE, "02B_timePublishedAuthor", "promotion",[],["postType"]),
    POST_CREATED_4                      (MailGroupType.NOT_CONFIGURABLE, "02C_timePublishedAuthor", "promotion",[],["postType"]),

    FEEDBACK                      (MailGroupType.NOT_CONFIGURABLE, "00_feedback", "internal",[],["feedbackText", "feedbackUser", "feedbackUserLink", "userDeleted"]);


    String nameTemplate
    String tagTemplate
    List<String> requiredBindings
    List<String> globalBindings
    MailGroupType mailGroup

    MailType(MailGroupType mailGroup, String nameTemplate, String tagTemplate, List<String> requiredBindings, List<String> globalBindings){
        this.nameTemplate = nameTemplate
        this.tagTemplate = tagTemplate
        this.requiredBindings = requiredBindings
        this.globalBindings = globalBindings
        this.mailGroup = mailGroup
    }

    Boolean getConfigurable(){
        mailGroup != MailGroupType.NOT_CONFIGURABLE
    }

}