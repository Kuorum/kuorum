package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import payment.campaign.CampaignService
import springSecurity.KuorumRegisterCommand

class LandingController {

    def kuorumUserService
    SpringSecurityService springSecurityService
    CampaignService campaignService
    DomainService domainService

    def index() { }

    def landingServices(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
            return;
        }
        List<CampaignRSDTO> campaigns = campaignService.findRelevantDomainCampaigns()
        if (!campaigns) {
            log.error("User ${WebConstants.FAKE_LANDING_ALIAS_USER} not exists :: Showing landing page without campaings")
        }
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        [
                campaigns:campaigns,
                slogan:domainRSDTO.slogan,
                subtitle:domainRSDTO.subtitle,
                domainDescription:domainRSDTO.domainDescription,
                command: new KuorumRegisterCommand()
        ]
    }
}
