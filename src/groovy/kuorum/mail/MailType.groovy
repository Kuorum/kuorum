package kuorum.mail

import org.kuorum.rest.model.notification.mail.sent.MailTypeRSDTO

/**
 * Created by iduetxe on 25/02/14.
 */
@Deprecated
public enum MailType {

    REGISTER_VERIFY_EMAIL               (MailGroupType.REGISTER,"register-validation",    ["confirmationLink"], [], MailTypeRSDTO.REGISTER_VERIFY_EMAIL),
    REGISTER_RESET_PASSWORD             (MailGroupType.REGISTER,"register-resetPassword", ["resetPasswordLink"], [], MailTypeRSDTO.REGISTER_RESET_PASSWORD),
    REGISTER_RRSS                       (MailGroupType.REGISTER,"register-socialNetworks",[], ["provider"],MailTypeRSDTO.REGISTER_SOCIAL_NETWORKS),
    REGISTER_ACCOUNT_COMPLETED          (MailGroupType.REGISTER,"register-completed",     [], [], MailTypeRSDTO.REGISTER_ACCOUNT_COMPLETED),
    REGISTER_CHANGE_EMAIL_VERIFY        (MailGroupType.REGISTER,"register-emailChangeNew",["confirmationLink"], [], MailTypeRSDTO.REGISTER_CHANGE_EMAIL_VERIFY),
    REGISTER_CHANGE_EMAIL_REQUESTED     (MailGroupType.REGISTER,"register-emailChangeOld",["newEmailAccount"], [], MailTypeRSDTO.REGISTER_CHANGE_EMAIL_REQUESTED),
    REGISTER_REQUEST_DEMO               (MailGroupType.REGISTER,"no-mandrillapp",[], [], MailTypeRSDTO.REGISTER_REQUEST_DEMO),
    //TODO: Change this email to REGISTER_WELCOME
    NOTIFICATION_OFFER_PURCHASED        (MailGroupType.REGISTER,"register-suscription",   [], ["user"], MailTypeRSDTO.REGISTER_WELCOME),
    CAMPAIGN_POLL_THANK_YOU             (MailGroupType.REGISTER,"register-modalelections",[],["recovery", "education", "democracy", "equalty", "constitution", "foreign", "politician", "politicianLink"]),

    NOTIFICATION_CLUCK                  (MailGroupType.EVENT_ME,         "me-cluck",                    [],["clucker","cluckerLink","postName", "postLink"]),
    NOTIFICATION_FOLLOWER               (MailGroupType.EVENT_ME,         "me-follower",                 [],["follower","followerLink"]),
    NOTIFICATION_PUBLIC_MILESTONE       (MailGroupType.EVENT_ME,         "me-milestone",                [],["postName", "numVotes", "postLink"]),
    NOTIFICATION_DEBATE_USERS           (MailGroupType.EVENT_PROPOSAL,   "proposal-debate",             [],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_AUTHOR          (MailGroupType.EVENT_ME,         "me-proposalDebate",           [],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEBATE_POLITICIAN      (MailGroupType.EVENT_POLITICIAN, "politician-debateOther",      [],["debateOwner","debateOwnerLink","postName","postOwner","postOwnerLink","message", "postLink"]),
    NOTIFICATION_DEFENDED_USERS         (MailGroupType.EVENT_PROPOSAL,   "proposal-sponsorship",        [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_AUTHOR        (MailGroupType.EVENT_ME,         "me-proposalSponsorship",      [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_BY_POLITICIAN (MailGroupType.EVENT_POLITICIAN, "politician-sponsorshipMine",  [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_DEFENDED_POLITICIANS   (MailGroupType.EVENT_POLITICIAN, "politician-sponsorshipOther", [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_COMMENTED_POST_OWNER   (MailGroupType.EVENT_ME,         "me-proposalComment",          [],["commenter","commenterLink","comment", "postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_COMMENTED_POST_USERS   (MailGroupType.EVENT_ME,         "me-commentAnswer",            [],["commenter","commenterLink","comment", "postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_USERS          (MailGroupType.EVENT_PROPOSAL,   "proposal-victory",            [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_VICTORY_DEFENDER       (MailGroupType.EVENT_POLITICIAN, "politician-victoryMine",      [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink"]),
    NOTIFICATION_CONTACT                (MailGroupType.EVENT_POLITICIAN, "politician-contact",          [],["contact", "contactLink","contactMessage","causeName"], MailTypeRSDTO.NOTIFICATION_CONTACT),

    POST_CREATED_1                      (MailGroupType.TIME, "time-proposalPublished-0", [],[]),
    POST_CREATED_2                      (MailGroupType.TIME, "time-proposalPublished-1", [],[]),
    POST_CREATED_3                      (MailGroupType.TIME, "time-proposalPublished-3", [],[]),
    POST_CREATED_4                      (MailGroupType.TIME, "time-proposalPublished-12",[],[]),

    FEEDBACK_VICTORY              (MailGroupType.ADMIN, "admin-victory",     [],["defender","defenderLink","postName", "postOwner","postOwnerLink","postLink", "victoryOk"]),
    FEEDBACK                      (MailGroupType.ADMIN, "admin-deletedUser", [],["feedbackText", "feedbackUser", "feedbackUserLink", "userDeleted"]),
    POLITICIAN_SUBSCRIPTION       (MailGroupType.ADMIN, "admin-suscription", [],["userLink", "user", "offerType", "totalPrice"]),
    EDITOR_SUBSCRIPTION           (MailGroupType.ADMIN, "admin-suscription-editor", [],["userLink", "user"]),
    POLITICIAN_SUBSCRIPTION_TESTER(MailGroupType.ADMIN, "admin-suscription-beta-tester", [],["userLink", "user"]),
    BATCH_PROCESS                 (MailGroupType.ADMIN, "admin-batchProcess",[],["body", "subject"], MailTypeRSDTO.ADMIN_COMMUNICATION);


    String nameTemplate
    List<String> requiredBindings
    List<String> globalBindings
    MailGroupType mailGroup
    MailTypeRSDTO mailTypeRSDTO

    MailType(MailGroupType mailGroup, String nameTemplate, List<String> requiredBindings, List<String> globalBindings){
        this.nameTemplate = nameTemplate
        this.requiredBindings = requiredBindings
        this.globalBindings = globalBindings
        this.mailGroup = mailGroup
    }

    MailType(MailGroupType mailGroup, String nameTemplate, List<String> requiredBindings, List<String> globalBindings, MailTypeRSDTO mailTypeRSDTO){
        this(mailGroup, nameTemplate, requiredBindings, globalBindings)
        this.mailTypeRSDTO = mailTypeRSDTO
    }

    Boolean getConfigurable(){
        mailGroup.editable
    }

}
