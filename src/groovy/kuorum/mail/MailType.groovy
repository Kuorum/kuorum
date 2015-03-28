package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_EMAIL               (MailGroupType.REGISTER,"register-validation",    ["confirmationLink"], []),
    REGISTER_RESET_PASSWORD             (MailGroupType.REGISTER,"register-resetPassword", ["resetPasswordLink"], []),
    REGISTER_RRSS                       (MailGroupType.REGISTER,"register-socialNetworks",[], ["provider"]),
    REGISTER_ACCOUNT_COMPLETED          (MailGroupType.REGISTER,"register-completed",     [], []),
    REGISTER_CHANGE_EMAIL_VERIFY        (MailGroupType.REGISTER,"register-emailChangeOld",["confirmationLink"], []),
    REGISTER_CHANGE_EMAIL_REQUESTED     (MailGroupType.REGISTER,"register-emailChangeNew",["newEmailAccount"], []),
    NOTIFICATION_OFFER_PURCHASED        (MailGroupType.REGISTER,"register-suscription",   [], ["userLink", "user", "offerType", "totalPrice"]),

    PROJECT_CREATED_NOTIFICATION        (MailGroupType.EVENT_PROJECT, "project-new", ["projectName", "projectOwner", "commissionType"], ["projectYoutube", "projectLink", "projectImage"]),

    NOTIFICATION_CLUCK                  (MailGroupType.EVENT_ME,         "me-cluck",                    ["postType"],["clucker","cluckerLink","postName", "postLink"]),
    NOTIFICATION_FOLLOWER               (MailGroupType.EVENT_ME,         "me-follower",                 [],["follower","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE       (MailGroupType.EVENT_ME,         "me-milestone",                ["postType"],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE_USERS           (MailGroupType.EVENT_PROPOSAL,   "proposal-debate",             ["postType"],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_AUTHOR          (MailGroupType.EVENT_ME,         "me-proposalDebate",           ["postType"],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_POLITICIAN      (MailGroupType.EVENT_POLITICIAN, "politician-debateOther",      ["postType"],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEFENDED_USERS         (MailGroupType.EVENT_PROPOSAL,   "proposal-sponsorship",        ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_AUTHOR        (MailGroupType.EVENT_ME,         "me-proposalSponsorship",      ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_BY_POLITICIAN (MailGroupType.EVENT_POLITICIAN, "politician-sponsorshipMine",  ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_POLITICIANS   (MailGroupType.EVENT_POLITICIAN, "politician-sponsorshipOther", ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_COMMENTED_POST_OWNER   (MailGroupType.EVENT_ME,         "me-proposalComment",          ["postType"],["commenter","commenterLink","comment", "postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_COMMENTED_POST_USERS   (MailGroupType.EVENT_ME,         "me-commentAnswer",            ["postType"],["commenter","commenterLink","comment", "postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_USERS          (MailGroupType.EVENT_PROPOSAL,   "proposal-victory",            ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_DEFENDER       (MailGroupType.EVENT_POLITICIAN, "politician-victoryMine",      ["postType"],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),

    POST_CREATED_1                      (MailGroupType.TIME, "time-proposalPublished-0", [],["postType"]),
    POST_CREATED_2                      (MailGroupType.TIME, "time-proposalPublished-1", [],["postType"]),
    POST_CREATED_3                      (MailGroupType.TIME, "time-proposalPublished-3", [],["postType"]),
    POST_CREATED_4                      (MailGroupType.TIME, "time-proposalPublished-12",[],["postType"]),

    FEEDBACK_VICTORY              (MailGroupType.ADMIN, "admin-victory",     [],["postType","defender","defenderLink","postName", "postOwner","postOwnerLink","postLink", "victoryOk"]),
    FEEDBACK                      (MailGroupType.ADMIN, "admin-deletedUser", [],["feedbackText", "feedbackUser", "feedbackUserLink", "userDeleted"]),
    POLITICIAN_SUBSCRIPTION       (MailGroupType.ADMIN, "admin-suscription", [],["userLink", "user", "offerType", "totalPrice"]);


    String nameTemplate
    List<String> requiredBindings
    List<String> globalBindings
    MailGroupType mailGroup

    MailType(MailGroupType mailGroup, String nameTemplate, List<String> requiredBindings, List<String> globalBindings){
        this.nameTemplate = nameTemplate
        this.requiredBindings = requiredBindings
        this.globalBindings = globalBindings
        this.mailGroup = mailGroup
    }

    Boolean getConfigurable(){
        mailGroup.editable
    }
}
