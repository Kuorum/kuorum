package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.domain.DomainRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
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
        KuorumUser user = kuorumUserService.findByAlias(WebConstants.FAKE_LANDING_ALIAS_USER)
        List<CampaignRSDTO> campaigns = []
        if (user) {
            campaigns = campaignService.findAllCampaigns(user).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
//            campaigns = campaigns.subList(Math.min(campaigns.size(), 3))
            campaigns = campaigns.sort({a, b -> b.datePublished <=> a.datePublished })
            if( campaigns.size() > 3){
                campaigns = campaigns.subList(campaigns.size()-3, campaigns.size());
            }

        }else{
            log.error("User ${WebConstants.FAKE_LANDING_ALIAS_USER} not exists :: Showing landing page without campaings")
        }
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        [
                campaigns:campaigns,
                slogan:domainRSDTO.slogan,
                subtitle:domainRSDTO.subtitle,
                command: new KuorumRegisterCommand()
        ]
    }
}
