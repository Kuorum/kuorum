package kuorum.web.commands.campaign

import grails.validation.Validateable

/**
 * Created by iduetxe on 2/12/15.
 */
@Validateable
class CampaignPollCommand {

    List<String> causes
    String email
    String politicianId
    String campaignId

    static constraints = {
        email nullable: false, email: true
        causes minSize: 3, maxSize: 3, nullable:false
        politicianId nullable:false, blank: false
    }
}
