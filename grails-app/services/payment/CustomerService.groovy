package payment

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.payment.KuorumPaymentPlanDTO
import org.kuorum.rest.model.payment.PaymentRQDTO
import org.kuorum.rest.model.payment.SubscriptionCycleDTO
import org.kuorum.rest.model.payment.SubscriptionRQDTO

@Transactional
class CustomerService {

    RestKuorumApiService restKuorumApiService

    String getPaymentToken(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_TOKEN,
                params,
                query,
//                new TypeReference<String>(){}
                null
        )
        response.responseData.str
    }

    boolean savePaymentMethod(KuorumUser user, String nonce){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        PaymentRQDTO paymentRQDTO= new PaymentRQDTO()
        paymentRQDTO.setPaymentNonce(nonce)
        try{
            def response = restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_TOKEN,
                    params,
                    query,
                    paymentRQDTO,
                    null
            )
            return true
        }catch (Exception e){
            return false
        }
//        response.data
    }

    void createSubscription(KuorumUser user, SubscriptionCycleDTO subscriptionCycleDTO, String promotionCode){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        SubscriptionRQDTO subscriptionRQDTO= new SubscriptionRQDTO()
        subscriptionRQDTO.setPromotionalCode(promotionCode)
        subscriptionRQDTO.setSubscriptionCycle(subscriptionCycleDTO)
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_SUBSCRIPTION,
                params,
                query,
                subscriptionRQDTO,
                null
        )
    }

    Boolean validSubscription(KuorumUserSession user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_SUBSCRIPTION,
                params,
                query,
                new TypeReference<Boolean>(){}
        )
        response.data
    }

    void removeSubscription(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_SUBSCRIPTION,
                params,
                query,
                null
        )
    }

    List<KuorumPaymentPlanDTO> getUserPaymentPlans(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_PLANS,
                params,
                query,
                new TypeReference<List<KuorumPaymentPlanDTO>>(){}
        )
        response.data
    }

    KuorumPaymentPlanDTO getInfoUserPlan(KuorumUser user, SubscriptionCycleDTO subscriptionCycleDTO, String promotionalCode = null){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [promotionalCode:promotionalCode, subscriptionCycle:subscriptionCycleDTO.toString()]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_PAYMENT_PLAN,
                params,
                query,
                new TypeReference<KuorumPaymentPlanDTO>(){}
        )
        response.data
    }


    Map<String, List<KuorumPaymentPlanDTO>> getActivePlans(){
        Map<String, String> params = [:]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.CUSTOMER_ACTIVE_PLANS,
                params,
                query,
                new TypeReference<Map<String, List<KuorumPaymentPlanDTO>>>(){}
        )
        response.data
    }
}
