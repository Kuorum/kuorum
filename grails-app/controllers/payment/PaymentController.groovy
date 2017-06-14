package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.CookieUUIDService
import kuorum.users.KuorumUser
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.kuorum.rest.model.payment.SubscriptionCycleDTO
import payment.contact.PromotionalCodeService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class PaymentController {

    SpringSecurityService springSecurityService

    CustomerService customerService;

    PromotionalCodeService promotionalCodeService

    CookieUUIDService cookieUUIDService;

    def index(){
        KuorumUser user = springSecurityService.currentUser;
        if (customerService.validSubscription(user)){
            flash.message="You already has a valid subscription."
            String urlRedirectAfterPay = cookieUUIDService.getPaymentRedirect()
            redirect(uri:urlRedirectAfterPay)
        }
        List<KuorumPaymentPlanDTO> plans = customerService.getUserPaymentPlans(user)
        [
                plans:plans
        ]

    }

    def paymentGateway(){
        SubscriptionCycleDTO subscriptionCycle;
        try{
            subscriptionCycle = SubscriptionCycleDTO.valueOf(params.subscriptionCycle)
        }catch (Exception){
            subscriptionCycle = null;
        }

        if (!subscriptionCycle){
            flash.error="No subscritpion type"
            redirect mapping:"paymentStart"
            return;
        }
        KuorumUser user = springSecurityService.currentUser;
        KuorumPaymentPlanDTO plan = customerService.getInfoUserPlan(user, subscriptionCycle)
        String token = customerService.getPaymentToken(user)
        [
                plan:plan,
                token:token,
                locale: user.getLanguage().locale
        ]
    }

    def paymentGatewaySubmitSubscription(){
        SubscriptionCycleDTO subscriptionCycle;
        try{
            subscriptionCycle = SubscriptionCycleDTO.valueOf(params.subscriptionCycle)
        }catch (Exception){
            subscriptionCycle = SubscriptionCycleDTO.MONTHLY;
        }
        KuorumUser user = springSecurityService.currentUser;
        if (!subscriptionCycle){
            flash.error="No subscritpion type"
            redirect mapping:"paymentStart"
            return;
        }
        String promotionalCode = params.promotionalCode
        customerService.createSubscription(user, subscriptionCycle, promotionalCode);
        redirect(mapping:'paymentSuccess')

    }

    def getInfoPlan(){
        SubscriptionCycleDTO subscriptionCycle;
        try{
            subscriptionCycle = SubscriptionCycleDTO.valueOf(params.subscriptionCycle)
        }catch (Exception){
            subscriptionCycle = SubscriptionCycleDTO.MONTHLY;
        }
        KuorumUser user = springSecurityService.currentUser;
        String promotionalCode = params.promotionalCode
        KuorumPaymentPlanDTO plan;
        Boolean success = false;
        if (promotionalCodeService.checkPromotionalCode(promotionalCode)){
            plan = customerService.getInfoUserPlan(user, subscriptionCycle, promotionalCode)
            success = plan != null;
        }
        render ([success:success, plan:plan] as JSON)
    }

    def savePaymentMethod(String nonce){

        KuorumUser user = springSecurityService.currentUser;
        customerService.savePaymentMethod(user, nonce)
        render ([success:"success"] as JSON)
    }

    def paymentSuccess(){
        String urlRedirectAfterPay = cookieUUIDService.getPaymentRedirect()
        if(!urlRedirectAfterPay){
            urlRedirectAfterPay = g.createLink(mapping: 'home')
        }
        [urlRedirectAfterPay:urlRedirectAfterPay]
    }

    def promotionalCodeValidation(String code){
        Boolean validator = promotionalCodeService.checkPromotionalCode(code);
        render ([validator:validator] as JSON)
    }

}
