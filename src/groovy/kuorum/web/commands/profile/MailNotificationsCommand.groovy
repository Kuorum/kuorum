package kuorum.web.commands.profile

import grails.validation.Validateable

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
    boolean participatoryBudgetNewOwner;
    boolean participatoryBudgetNewCause;
    boolean districtProposalNewOwner;
    boolean districtProposalNewCause;
    boolean districtProposalParticipatoryBudgetOwner;
    boolean districtProposalSupport;
    boolean districtProposalVote;
    boolean petitionSign;

    static constraints = {
    }
}
