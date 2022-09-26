package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
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
        List<CampaignRSDTO> campaigns = campaignService.findRelevantDomainCampaigns()
        DomainRSDTO domainRSDTO = domainService.getConfig(CustomDomainResolver.domain)
        def model = [
                slogan             : domainRSDTO.slogan?:g.message(code: "kuorum.web.landing.configuration.default.slogan"),
                subtitle           : domainRSDTO.subtitle?:g.message(code: "kuorum.web.landing.configuration.default.subtitle"),
                domainDescription  : domainRSDTO.domainDescription,
                landingVisibleRoles: domainRSDTO.landingVisibleRoles,
                command            : new KuorumRegisterCommand(),
                campaigns          : null,
                starredCampaign    : null,
                carouselFooter1    : domainRSDTO.carouselFooter1?:g.message(code: "landingServices.carousel.slide1.text"),
                carouselFooter2    : domainRSDTO.carouselFooter2?:g.message(code: "landingServices.carousel.slide2.text"),
                carouselFooter3    : domainRSDTO.carouselFooter3?:g.message(code: "landingServices.carousel.slide3.text")
        ]
        if (domainService.showPrivateContent()) {
            CampaignRSDTO starredCampaign = campaignService.findStarredCampaign(campaigns, domainRSDTO.getStarredCampaignId())
            model.put('campaigns',campaigns)
            model.put('starredCampaign',starredCampaign,)
        }
        return model;
    }
}
