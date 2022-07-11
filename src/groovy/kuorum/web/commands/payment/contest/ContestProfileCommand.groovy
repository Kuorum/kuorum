package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.commands.profile.SocialNetworkCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class ContestProfileCommand {

    Long campaignId

    String name;
    String email;
    String nid;
    SocialNetworkCommand social;
    String phone;
    String bio;

    // FILES

    static constraints = {
        bio nullable: false, maxSize: 500
    }

}
