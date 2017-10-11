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
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "landingTechnology", model: [command: new KuorumContactUsCommand()])
        }
    }

    def landingEnterprise(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "landingEnterprise", model: [command: new KuorumContactUsCommand()])
        }
    }

    def landingCasesStudy(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
            render(view: "landingCasesStudy")
        }
    }

    def individualCaseStudy(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "individualCaseStudy", model: [command: new KuorumContactUsCommand()])
        }
    }

    def landingAdministration(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "landingAdministration", model: [command: new KuorumContactUsCommand()])
        }
    }

    def landingOrganization(){
        if (springSecurityService.isLoggedIn()){
            flash.message = flash.message
            redirect (mapping:"dashboard")
        }else{
//            render(view: "landingServices", model: [command: new KuorumRegisterCommand(), siteKey: RECAPTCHA_SITEKEY])
            render(view: "landingOrganization", model: [command: new KuorumContactUsCommand()])
        }
    }
}
