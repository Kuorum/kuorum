package kuorum.web.commands.payment.contest

import grails.validation.Validateable
import kuorum.web.commands.payment.CampaignContentCommand
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

@Validateable
class ContestAreasCommand {

    Long campaignId

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn
    String sendType

    Integer numWinnerApplications
    Integer maxApplicationsPerUser

//    @BindUsing({ obj, org.grails.databinding.DataBindingSource source ->
//        String normalizedCauses = source['causes'].replaceAll(';', ',')
//        normalizedCauses.split(',').findAll({it}).collect{it.decodeHashtag().trim()}.unique { a, b -> a <=> b }
//    })
//    Set<String> causes;

    static constraints = {
        importFrom CampaignContentCommand, include: ["publishOn", "sendType"]
        numWinnerApplications nullable: false
        maxApplicationsPerUser nullable: false
    }

}
