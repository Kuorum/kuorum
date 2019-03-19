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
            return
        }
        List<CampaignRSDTO> campaigns = campaignService.findRelevantDomainCampaigns()
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        CampaignRSDTO starredCampaign = findStarredCampaign(campaigns, domainRSDTO.getStarredCampaignId())
        [
                campaigns:campaigns,
                starredCampaign:starredCampaign,
                slogan:domainRSDTO.slogan,
                subtitle:domainRSDTO.subtitle,
                domainDescription:domainRSDTO.domainDescription,
                landingVisibleRoles: domainRSDTO.landingVisibleRoles,
                command: new KuorumRegisterCommand()
        ]
    }

    private CampaignRSDTO findStarredCampaign(List<CampaignRSDTO> relevantCampaings, Long starredCampaign){
        CampaignRSDTO relevantCampaign = relevantCampaings.find {it.id == starredCampaign}
        if (relevantCampaings == null){
            relevantCampaign = campaignService.find(WebConstants.FAKE_LANDING_ALIAS_USER, starredCampaign)
        }
        return relevantCampaign
    }
}
