package kuorum.landings

import grails.plugin.springsecurity.SpringSecurityService
import springSecurity.KuorumContactUsCommand
import springSecurity.KuorumRegisterCommand

class LandingController {

    SpringSecurityService springSecurityService

    def index() { }

    def landingServices(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "landingServices", model: [command: new KuorumRegisterCommand()])
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

    def individualCaseStudy(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
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
