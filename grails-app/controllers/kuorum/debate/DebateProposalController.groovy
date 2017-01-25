package kuorum.debate

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.massMailing.DebateProposalCommand
import org.kuorum.rest.model.communication.debate.ProposalRSDTO
import payment.campaign.DebateService
import payment.campaign.ProposalService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class DebateProposalController {

    SpringSecurityService springSecurityService

    ProposalService proposalService

    DebateService debateService

    def addProposal(DebateProposalCommand command) {
        KuorumUser user = springSecurityService.currentUser
        ProposalRSDTO proposalRSDTO = proposalService.addProposal(user, command.debateAlias, command.debateId, command.body)

        render template: '/debate/showModules/mainContent/proposalData', model:[proposal:proposalRSDTO]

    }

    def pinProposal(){}

    def likeProposal(){}

    def addComment(){

    }

    def voteComment(){}

    def deleteComment(){}
}
