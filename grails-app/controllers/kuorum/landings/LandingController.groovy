package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService

class LandingController {

    SpringSecurityService springSecurityService

    def index() { }

    def landingServices(){
        cache "landings"
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingTechnology(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingEnterprise(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def landingCaseStudy(){}

    def caseStudy001(){
        render(view: "/landing/caseStudies/caseStudy001", model:[caseStudyId:"001", suggestedCaseStudies:["002","003","004"]])
    }
    def caseStudy002(){
        render(view: "/landing/caseStudies/caseStudy002", model:[caseStudyId:"002", suggestedCaseStudies:["001","003","004"]])
    }
    def caseStudy003(){
        render(view: "/landing/caseStudies/caseStudy003", model:[caseStudyId:"003", suggestedCaseStudies:["001","002","004"]])
    }
    def caseStudy004(){
        render(view: "/landing/caseStudies/caseStudy004", model:[caseStudyId:"004", suggestedCaseStudies:["002","003","001"]])
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
