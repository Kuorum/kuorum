package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService

class LandingController {

    SpringSecurityService springSecurityService

    def index() { }

    def landingServices(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
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
        render(view: "/landing/caseStudies/caseStudy004", model:[caseStudyId:"004", suggestedCaseStudies:["002","003","001"]])
    }
    def caseStudy005(){
        render(view: "/landing/caseStudies/caseStudy005", model:[caseStudyId:"005", suggestedCaseStudies:["001","003","004"]])
    }
    def caseStudy006(){
        render(view: "/landing/caseStudies/caseStudy006", model:[caseStudyId:"006", suggestedCaseStudies:["001","007","005"]])
    }
    def caseStudy007(){
        render(view: "/landing/caseStudies/caseStudy007", model:[caseStudyId:"007", suggestedCaseStudies:["002","006","003"]])
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
