package payment.contact

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.SearchContactRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO

@Transactional
class ContactService {

    RestKuorumApiService restKuorumApiService;

    void addBulkContacts(KuorumUser user, List<ContactRSDTO> contactRSDTOs){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(RestKuorumApiService.ApiMethod.USER_CONTACTS, params,query, contactRSDTOs)
//        if (response.data){
//            ContactsInfoRSDTO contactsInfo = (ContactsInfoRSDTO)response.data
//        }
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

    List<ExtendedFilterRSDTO> getUserFilters(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_CONTACT_FILTERS, params,query)
        List<ExtendedFilterRSDTO> filters = []
        if (response.data){
            filters = response.data
        }
        filters
    }

    ExtendedFilterRSDTO createFilter(KuorumUser user, FilterRDTO filterRSDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.post(RestKuorumApiService.ApiMethod.USER_CONTACT_FILTERS, params,query, filterRSDTO)
        ExtendedFilterRSDTO filter= null
        if (response.data){
            filter = response.data
        }
        filter
    }

    ContactPageRSDTO getUsers(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO();

        Map<String, String> query = new org.apache.commons.beanutils.BeanMap(searchContactRSDTO);
        query = query.findAll {k,v -> v }

        def response= restKuorumApiService.get(RestKuorumApiService.ApiMethod.USER_CONTACTS, params,query)
        ContactPageRSDTO contactPage = new ContactPageRSDTO();
        if (response.data){
            contactPage = response.data
        }
        contactPage
    }
}
