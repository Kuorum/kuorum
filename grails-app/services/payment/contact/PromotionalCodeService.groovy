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

    private Set<String> promotionalCodes = ["deusto", "wece"] as Set

    boolean checkPromotionalCode(String promotionalCode){
        return promotionalCodes.contains(promotionalCode)
    }

    def setPromotionalCode(KuorumUser user, String promotionalCode){
        // NOT DONE
    }
}
