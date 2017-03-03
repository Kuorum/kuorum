package kuorum.debate

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.massMailing.CommentProposalCommand
import kuorum.web.commands.payment.massMailing.DebateProposalCommand
import kuorum.web.commands.payment.massMailing.LikeProposalCommand
import kuorum.web.commands.payment.massMailing.PinProposalCommand
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.ProposalCommentRDTO
import org.kuorum.rest.model.communication.debate.ProposalCommentRSDTO
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

        render template: '/debate/showModules/mainContent/proposalData', model:[debate:debate, debateUser:debateUser,proposal:proposalRSDTO]

    }

    def deleteProposal(){
        Long proposalId = Long.parseLong(params.proposalId)
        Long debateId = Long.parseLong(params.debateId)
        String debateAlias = params.debateAlias
        KuorumUser user = springSecurityService.currentUser
        proposalService.deleteProposal(user, debateAlias, debateId, proposalId)
        render "true"
    }

    @Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
    def addProposalAndRegister(){

    }

    def pinProposal(PinProposalCommand command){
        KuorumUser user = springSecurityService.currentUser

        ProposalRSDTO proposalRSDTO = proposalService.pinProposal(user, command.debateAlias, command.debateId, command.proposalId, command.pin)
        render "ok"
    }

    def likeProposal(LikeProposalCommand command){
        KuorumUser user = springSecurityService.currentUser
        proposalService.likeProposal(user, command.debateAlias,command.debateId, command.proposalId, command.like)
        render "Ok"
    }

    def addComment(CommentProposalCommand command){
        KuorumUser user = springSecurityService.currentUser
        KuorumUser debateUser = KuorumUser.findByAlias(command.debateAlias)
        DebateRSDTO debate = debateService.findDebate(debateUser, command.debateId)
        ProposalRSDTO proposalRSDTO = proposalService.addComment(user, debate, command.proposalId, command.body)
        ProposalCommentRSDTO comment = proposalRSDTO.comments.reverseFind{it.userAlias == user.alias}
        render template: "/debate/showModules/mainContent/proposalDataComment", model:[debate:debate, proposal:proposalRSDTO, comment:comment]
    }

    def voteComment(){}

    def deleteComment(){
        Long proposalId = Long.parseLong(params.proposalId)
        Long debateId = Long.parseLong(params.debateId)
        String debateAlias = params.debateAlias
        Long commentId = Long.parseLong(params.commentId)
        KuorumUser user = springSecurityService.currentUser
        proposalService.deleteComment(user, debateId,debateAlias, proposalId, commentId)
        render "true"
    }
}
