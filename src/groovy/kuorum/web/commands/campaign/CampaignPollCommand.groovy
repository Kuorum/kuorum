package kuorum.web.commands.campaign

import grails.validation.Validateable
import kuorum.campaign.Campaign
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 2/12/15.
 */
@Validateable
class CampaignPollCommand {

    List<String> causes
    String email
    KuorumUser politician
    Campaign campaign

    static constraints = {
        email nullable: false, email: true
        causes minSize: 3, maxSize: 3, nullable:false
        politician nullable:false
        campaign nullable: false
    }
}
