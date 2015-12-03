package kuorum.web.commands.campaign

import grails.validation.Validateable

/**
 * Created by iduetxe on 2/12/15.
 */
@Validateable
class CampaignPollCommand {

    List<String> selections
    String email
    String politicianId

    static constraints = {
        email nullable: false, email: true
        selections minSize: 1
        politicianId nullable:false, blank: false
    }
}
