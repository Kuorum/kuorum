package payment.contact

import grails.transaction.Transactional
import kuorum.OfferPurchased
import kuorum.core.exception.KuorumException
import kuorum.core.model.OfferType
import kuorum.notifications.NotificationService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.ContactsInfoRSDTO

@Transactional
class ContactService {

    RestKuorumApiService restKuorumApiService;

    void addBulkContacts(KuorumUser user, List<ContactRSDTO> contactRSDTOs){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(RestKuorumApiService.ApiMethod.USER_CONTACT_ADD_BULK, params,query, contactRSDTOs)
        if (response.data){
            ContactsInfoRSDTO contactsInfo = (ContactsInfoRSDTO)response.data
        }
    }

    List<String> getUserTags(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_CONTACT_TAGS, params,query)
        List<String> tags = []
        if (response.data){
            tags = response.data
        }
        tags
    }
}
