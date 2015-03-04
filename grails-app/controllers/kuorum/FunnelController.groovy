package kuorum

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.OfferType
import kuorum.mail.KuorumMailService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.PersonalDataCommand
import org.springframework.security.core.userdetails.UsernameNotFoundException
import springSecurity.KuorumRegisterCommand

class FunnelController {

    RegisterService registerService
    SpringSecurityService springSecurityService
    KuorumMailService kuorumMailService
    /**
     * Funnel Step1
     */
    def funnelSuccessfulStories() {}
    /**
     * Funnel Step2
     */
    def funnelOffers() {}
    /**
     * Funnel Step3
     */
    def funnelPay() {
        OfferType offerType
        try{
            offerType= OfferType.valueOf(params.offerType)
        }catch (Exception e){
            flash.error="No se ha detectado la oferta"  //Por aqui no debería pasar nunca
            redirect mapping:"funnelOffers"
            return
        }
        [
                offerType:offerType,
                totalPrice:offerType.finalPrice,
                yearly:offerType.isYearlyPay(),
                command:new KuorumRegisterCommand()
        ]
    }
    /**
     * Funnel Step3-register
     */
    def funnelSubscription(KuorumRegisterCommand command) {
        OfferType offerType
        try{
            offerType= OfferType.valueOf(params.offerType)
        }catch (Exception e){
            flash.error="No se ha detectado la oferta"  //Por aqui no debería pasar nunca
            redirect mapping:"funnelOffers"
            return
        }
        if (command.hasErrors()) {
            render view: 'funnelPay',
                    model: [
                            command: command,
                            offerType:offerType,
                            totalPrice:offerType.finalPrice,
                            yearly:offerType.isYearlyPay(),
                        ]
            return
        }
        KuorumUser user = registerService.registerUser(command);
        flash.offerType=offerType
        redirect mapping:"funnelPaySuccess"
    }

    /**
     * Funnel Step3-login
     */
    def funnelLogin() {
        OfferType offerType
        try{
            offerType= OfferType.valueOf(params.offerType)
        }catch (Exception e){
            flash.error="No se ha detectado la oferta"  //Por aqui no debería pasar nunca
            redirect mapping:"funnelOffers"
            return
        }
        if (!params.email) {
            KuorumRegisterCommand command = new KuorumRegisterCommand(email:params.email)
            command.errors.rejectValue("password",message(code: 'funnel.payment.email'))
            render view: 'funnelPay',
                    model: [
                            command: command,
                            offerType:offerType,
                            totalPrice:offerType.finalPrice,
                            yearly:offerType.isYearlyPay(),
                    ]
            return
        }
        try{
            springSecurityService.reauthenticate(params.email,params.password)
            flash.offerType=offerType
            redirect mapping:"funnelPaySuccess"
        }catch(UsernameNotFoundException e){
            KuorumRegisterCommand command = new KuorumRegisterCommand(email:command.email);
            command.errors.rejectValue("password",message(code: 'springSecurity.errors.login.fail'))
            render view: 'funnelPay',
                    model: [
                            command: command,
                            offerType:offerType,
                            totalPrice:offerType.finalPrice,
                            yearly:offerType.isYearlyPay(),
                    ]
            return
        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def funnelSuccess(){
        OfferType offerType = flash.offerType
        if (!offerType){
            try{
                offerType= OfferType.valueOf(params.offerType)
            }catch (Exception e){
                flash.error="No se ha detectado la oferta"  //Por aqui no debería pasar nunca
                redirect mapping:"funnelOffers"
                return
            }
        }
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        kuorumMailService.sendPoliticianSubscription(user,offerType)
        log.info("Usuario registrado")
        [
                command:new PersonalDataCommand()
        ]
    }

    def funnelUpdatePersonalData(PersonalDataCommand command){


    }
}
