package payment.contact

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.codehaus.jackson.type.TypeReference
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.SearchContactRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO

@Transactional
class PromotionalCodeService {

    RestKuorumApiService restKuorumApiService;

    boolean checkPromotionalCode(String promotionalCode){
        Map<String, String> params = [ promotionCode:promotionalCode]
        Map<String, String> query = [:]

        try{
            def response= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.PROMOTIONAL_CODES,
                    params,
                    query,
                    null)
            return true;
        }catch (Exception e){
            return false;
        }

    }

    def setPromotionalCode(KuorumUser user, String promotionalCode){
        Map<String, String> params = [userId:user.id.toString(), promotionCode:promotionalCode]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.PROMOTIONAL_CODE_ADD,
                params,
                query,
                null,
                null)
    }
}
