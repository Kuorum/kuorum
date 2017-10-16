package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import springSecurity.KuorumRegisterCommand

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

    def landingCasesStudy(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }
    }

    def caseStudy001(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "/landing/caseStudies/caseStudy001")
        }
    }

    def landingAdministration(){
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
