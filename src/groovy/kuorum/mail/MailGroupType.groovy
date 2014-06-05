package kuorum.mail

/**
 * Created by iduetxe on 21/05/14.
 */
public enum MailGroupType {
    NOT_CONFIGURABLE(false),
    POLITICIAN_MAIL(false),
    MAIL_RELATED_WITH_ME(true),
    MAIL_RELATED_WITH_OTHER_USERS(true),
    MAIL_RELATED_WITH_POLITICIANS(true)

    Boolean editable
    MailGroupType(boolean editable){
        this.editable = editable
    }
}
