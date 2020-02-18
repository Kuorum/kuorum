package kuorum.mail

import org.kuorum.rest.model.notification.mail.sent.MailTypeRSDTO

/**
 * Created by iduetxe on 25/02/14.
 */
@Deprecated
enum MailType {

    REGISTER_VERIFY_EMAIL               ("register-validation",    ["confirmationLink"],    [],                                                                     MailTypeRSDTO.REGISTER_VERIFY_EMAIL),
    REGISTER_RESET_PASSWORD             ("register-resetPassword", ["resetPasswordLink"],   [],                                                                     MailTypeRSDTO.REGISTER_RESET_PASSWORD),
    REGISTER_ACCOUNT_COMPLETED          ("register-completed",     [],                      [],                                                                     MailTypeRSDTO.REGISTER_ACCOUNT_COMPLETED),
    REGISTER_CHANGE_EMAIL_VERIFY        ("register-emailChangeNew",["confirmationLink"],    [],                                                                     MailTypeRSDTO.REGISTER_CHANGE_EMAIL_VERIFY),
    REGISTER_CHANGE_EMAIL_REQUESTED     ("register-emailChangeOld",["newEmailAccount"],     [],                                                                     MailTypeRSDTO.REGISTER_CHANGE_EMAIL_REQUESTED),
    REGISTER_REQUEST_DEMO               ("no-mandrillapp",         [],                      [],                                                                     MailTypeRSDTO.REGISTER_REQUEST_DEMO),
    REGISTER_WELLCOME                   ("register-suscription",   [],                      ["user"],                                                               MailTypeRSDTO.REGISTER_WELCOME),
    NOTIFICATION_CONTACT                ("politician-contact",     [],                      ["contact", "contactLink","contactMessage","causeName"],                MailTypeRSDTO.NOTIFICATION_CONTACT),
    FEEDBACK                            ("admin-deletedUser",      [],                      ["feedbackText", "feedbackUser", "feedbackUserLink", "userDeleted", "domain"]),
    BATCH_PROCESS                       ("admin-batchProcess",     [],                      ["body", "subject"],                                                    MailTypeRSDTO.ADMIN_COMMUNICATION)


    String nameTemplate
    List<String> requiredBindings
    List<String> globalBindings
    MailTypeRSDTO mailTypeRSDTO

    MailType(String nameTemplate, List<String> requiredBindings, List<String> globalBindings){
        this.nameTemplate = nameTemplate
        this.requiredBindings = requiredBindings
        this.globalBindings = globalBindings
    }

    MailType(String nameTemplate, List<String> requiredBindings, List<String> globalBindings, MailTypeRSDTO mailTypeRSDTO){
        this(nameTemplate, requiredBindings, globalBindings)
        this.mailTypeRSDTO = mailTypeRSDTO
    }
}
