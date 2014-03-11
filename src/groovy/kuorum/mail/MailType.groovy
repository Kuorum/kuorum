package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_ACCOUNT             (false,"03_validationEmail",           "registerUser", ["confirmationLink"], []),
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
    NOTIFICATION_VICTORY_DEFENDER       (true, "20_notificationVictoryDefender","notification",[],  ["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"])



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