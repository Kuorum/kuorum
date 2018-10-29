package kuorum.web.commands.payment.petition

import grails.validation.Validateable

@Validateable
class SignPetitionCommand {

    Long campaignId
    String petitionUserId
    Boolean sign

    static constraints = {
        campaignId nullable: false
        petitionUserId nullable: false
        sign nullable: false
    }
}
