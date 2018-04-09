package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.CampaignService

import javax.servlet.http.HttpServletResponse


class LandingController {

    def kuorumUserService
    SpringSecurityService springSecurityService
    CampaignService campaignService

    def index() { }

    def landingServices(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }

        KuorumUser user = kuorumUserService.findByAlias("admin")
        if (!user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return false
        }
        List<CampaignRSDTO> campaigns = campaignService.findAllCampaigns(user).findAll{it.newsletter.status == CampaignStatusRSDTO.SENT}
        [
                campaigns:campaigns
        ]
    }

    def landingTechnology(){
        cache "landings"
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingEnterprise(){
        cache "landings"
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingCaseStudy(){}

    def caseStudy001(){
        cache "landings"
        render(view: "/landing/caseStudies/caseStudy001", model:[caseStudyId:"001", suggestedCaseStudies:["002","003","006"]])
    }
    def caseStudy002(){
        cache "landings"
        render(view: "/landing/caseStudies/caseStudy002", model:[caseStudyId:"002", suggestedCaseStudies:["001","003","004"]])
    }
    def caseStudy003(){
        render(view: "/landing/caseStudies/caseStudy003", model:[caseStudyId:"003", suggestedCaseStudies:["001","002","004"]])
    }
    def caseStudy004(){
        render(view: "/landing/caseStudies/caseStudy004", model:[caseStudyId:"004", suggestedCaseStudies:["008","003","001"]])
    }
    def caseStudy005(){
        render(view: "/landing/caseStudies/caseStudy005", model:[caseStudyId:"005", suggestedCaseStudies:["009","008","004"]])
    }
    def caseStudy006(){
        render(view: "/landing/caseStudies/caseStudy006", model:[caseStudyId:"006", suggestedCaseStudies:["001","007","005"]])
    }
    def caseStudy007(){
        render(view: "/landing/caseStudies/caseStudy007", model:[caseStudyId:"007", suggestedCaseStudies:["009","006","003"]])
    }
    def caseStudy008(){
        render(view: "/landing/caseStudies/caseStudy008", model:[caseStudyId:"008", suggestedCaseStudies:["007","006","003"]])
    }
    def caseStudy009(){
        render(view: "/landing/caseStudies/caseStudy009", model:[caseStudyId:"009", suggestedCaseStudies:["008","005","003"]])
    }

    def landingGovernments(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingOrganization(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }
}
