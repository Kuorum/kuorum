package kuorum.mail

/**
 * Created by iduetxe on 21/05/14.
 */
@Deprecated
public enum MailGroupType {
    ADMIN(false),
    TIME(false),
    REGISTER(false),
    EVENT_POLITICIAN(false),
    EVENT_ME(true),
    EVENT_PROPOSAL(true),
    EVENT_PROJECT(true)

    Boolean editable
    MailGroupType(boolean editable){
        this.editable = editable
    }
}
