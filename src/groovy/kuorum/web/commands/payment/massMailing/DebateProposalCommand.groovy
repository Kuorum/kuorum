package kuorum.web.commands.payment.massMailing

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO

@Validateable
class DebateProposalCommand {

    Long debateId
    String debateAlias
    String body

    static constraints = {
        debateId nullable: false
        body nullable: false
        debateAlias nullable: true
    }

}
