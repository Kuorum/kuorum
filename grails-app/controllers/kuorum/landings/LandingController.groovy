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

    def landingServices() {
        if (springSecurityService.isLoggedIn()) {
            flash.message = flash.message
            redirect(mapping: "dashboard")
            return
        }
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        def model = [
                slogan             : domainRSDTO.slogan,
                subtitle           : domainRSDTO.subtitle,
                domainDescription  : domainRSDTO.domainDescription,
                landingVisibleRoles: domainRSDTO.landingVisibleRoles,
                command            : new KuorumRegisterCommand(),
                campaigns          : null,
                starredCampaign    : null
        ]
        if (domainService.showPrivateContent()) {
            List<CampaignRSDTO> campaigns = campaignService.findRelevantDomainCampaigns()
            CampaignRSDTO starredCampaign = findStarredCampaign(campaigns, domainRSDTO.getStarredCampaignId())
            model.put('campaigns',campaigns)
            model.put('starredCampaign',starredCampaign,)
        }
        return model;
    }

    private CampaignRSDTO findStarredCampaign(List<CampaignRSDTO> relevantCampaigns, Long starredCampaign){

        CampaignRSDTO relevantCampaign = null;
        if (relevantCampaigns){
            relevantCampaign = relevantCampaigns.find {it.id == starredCampaign}
        }
        if (relevantCampaign == null){
            relevantCampaign = campaignService.find(WebConstants.FAKE_LANDING_ALIAS_USER, starredCampaign)
        }
        return relevantCampaign
    }
}
