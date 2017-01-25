package kuorum.debate

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.massMailing.DebateProposalCommand
import kuorum.web.commands.payment.massMailing.PinProposalCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO
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
        KuorumUser debateUser = KuorumUser.findByAlias(command.debateAlias)
        DebateRSDTO debate = debateService.findDebate(debateUser, command.debateId)
        ProposalRSDTO proposalRSDTO = proposalService.addProposal(user, debate, command.body)

        render template: '/debate/showModules/mainContent/proposalData', model:[debate:debate, proposal:proposalRSDTO]

    }

    def pinProposal(PinProposalCommand command){
        KuorumUser user = springSecurityService.currentUser

        ProposalRSDTO proposalRSDTO = proposalService.pinProposal(user, command.debateAlias, command.debateId, command.proposalId, command.pin)
        render "ok"
    }

    def likeProposal(){}

    def addComment(){

    }

    def voteComment(){}

    def deleteComment(){}
}
