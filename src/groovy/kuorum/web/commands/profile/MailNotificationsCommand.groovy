package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.core.model.CommissionType
import kuorum.mail.MailGroupType
import kuorum.mail.MailType

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class MailNotificationsCommand {

    boolean mentions;
    boolean followNew;
    boolean proposalNew;
    boolean proposalNewOwner;
    boolean proposalLike;
    boolean proposalPinned;
    boolean proposalComment;
    boolean postLike;
    boolean debateNewOwner;
    boolean debateNewCause;
    boolean postNewCause;
    boolean eventNewCause;

    static constraints = {
    }
}
