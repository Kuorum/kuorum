package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.contact.*
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO

@Transactional
class ContactService {

    RestKuorumApiService restKuorumApiService

    void addBulkContacts(KuorumUser user, List<ContactRDTO> contactRSDTOs){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACTS,
                params,
                query,
                contactRSDTOs,
                null)
//        if (response.data){
//            ContactsInfoRSDTO contactsInfo = (ContactsInfoRSDTO)response.data
//        }
    }

    List<String> getUserTags(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_TAGS,
                params,
                query,
                new TypeReference<List<String>>(){})
        List<String> tags = []
        if (response.data){
            tags = response.data
        }
        tags
    }

    List<ExtendedFilterRSDTO> getUserFilters(KuorumUser user){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTERS,
                params,
                query,
                new TypeReference<List<ExtendedFilterRSDTO>>(){})
        List<ExtendedFilterRSDTO> filters = []
        if (response.data){
            filters = response.data
        }
        filters
    }

    FilterRDTO transformCommand (ContactFilterCommand filterCommand, String anonymousFilterName){
        if (!filterCommand.filterName) {
            filterCommand.filterName = anonymousFilterName
        }
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        if (!filterRDTO?.filterConditions) {
            return null
        }
        return filterRDTO
    }

    ExtendedFilterRSDTO createFilter(KuorumUser user, FilterRDTO filterRSDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTERS,
                params,
                query,
                filterRSDTO,
                new TypeReference<ExtendedFilterRSDTO>(){})
        ExtendedFilterRSDTO filter= null
        if (response.data){
            filter = response.data
        }
        filter
    }

    ExtendedFilterRSDTO updateFilter(KuorumUser user, FilterRDTO filterRSDTO, Long filterId){
        Map<String, String> params = [userId:user.id.toString(),filterId:filterId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTER,
                params,
                query,
                filterRSDTO,
                new TypeReference<ExtendedFilterRSDTO>(){})
        ExtendedFilterRSDTO filter= null
        if (response.data){
            filter = response.data
        }
        filter
    }

    ExtendedFilterRSDTO getFilter(KuorumUser user, Long filterId){
        Map<String, String> params = [userId:user.id.toString(),filterId:filterId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTER,
                params,
                query,
                new TypeReference<ExtendedFilterRSDTO>(){})
        ExtendedFilterRSDTO filter= null
        if (response.data){
            filter = response.data
        }
        filter
    }

    void removeFilter(KuorumUser user, Long filterId){
        Map<String, String> params = [userId:user.id.toString(),filterId:filterId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTER,
                params,
                query)

    }

    ContactPageRSDTO getUsers(KuorumUser user, FilterRDTO filterRDTO = null){
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO(filter:filterRDTO);
        getUsers(user, searchContactRSDTO)
    }

    ContactPageRSDTO getUsers(KuorumUser user, SearchContactRSDTO searchContactRSDTO){
        Map<String, String> params = [userId:user.id.toString()]


        Map<String, String> query = searchContactRSDTO.encodeAsQueryParams()

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS,
                params,
                query,
                new TypeReference<ContactPageRSDTO>(){})
        ContactPageRSDTO contactPage = new ContactPageRSDTO();
        if (response.data){
            contactPage = response.data
        }
        contactPage
    }

    ContactRSDTO getContact(KuorumUser user, Long contactId){
        Map<String, String> params = [userId:user.id.toString(),contactId:contactId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT,
                params,
                query,
                new TypeReference<ContactRSDTO>(){})
        ContactRSDTO contactPage =null
        if (response.data){
            contactPage = response.data
        }
        contactPage
    }

    void removeContact(KuorumUser user, Long contactId){
        Map<String, String> params = [userId:user.id.toString(),contactId:contactId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT,
                params,
                query)
    }

    ContactRSDTO updateContact(KuorumUser user, ContactRDTO contactRDTO, Long contactId){
        Map<String, String> params = [userId:user.id.toString(),contactId:contactId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACT,
                params,
                query,
                contactRDTO,
                new TypeReference<ContactRSDTO>(){})
        ContactRSDTO contact =null
        if (response.data){
            contact = response.data
        }
        contact
    }

    ContactRSDTO addContact(KuorumUser user, ContactRDTO contactRDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.USER_CONTACTS,
                params,
                query,
                contactRDTO,
                new TypeReference<ContactRSDTO>(){})
        ContactRSDTO contact =null
        if (response.data){
            contact = response.data
        }
        contact
    }


    ContactRSDTO checkContactUser(KuorumUser user, String email, String digest){
        Map<String, String> params = [userId:user.alias]
        Map<String, String> query = [contactEmail:email, digest:digest]
        try{
            def response= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.USER_CONTACT_SUBSCRIBE,
                    params,
                    query,
                    new TypeReference<ContactRSDTO>(){})
            ContactRSDTO contactPage =null
            if (response.data){
                contactPage = response.data
            }
            return contactPage
        }catch (Exception e){
            log.warn("Someone trying to check conctact '${email}' of the user ${user.alias} that not extits")
        }

    }

    boolean unsubscribeContactUser(KuorumUser user, String email, String digest){
        Map<String, String> params = [userId:user.alias]
        Map<String, String> query = [contactEmail:email, digest:digest]
        try{
            def response= restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.USER_CONTACT_SUBSCRIBE,
                    params,
                    query)
            return true;
        }catch (Exception e){
            log.warn("Someone trying to check conctact '${email}' of the user ${user.alias} that not extits")
            return false
        }
    }

    boolean bulkRemoveContacts(KuorumUser user, SearchContactRSDTO searchContact) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = searchContact.encodeAsQueryParams()

        try {
            restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACTS,
                params,
                query
            )

            return true
        } catch (Exception e) {
            log.warn(e.getMessage())
            return false
        }
    }

    boolean bulkAddTagsContacts(KuorumUser user, BulkUpdateContactTagsRDTO bulkUpdateContactTags) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]

        try {
            restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACT_TAGS,
                params,
                query,
                bulkUpdateContactTags,
                new TypeReference<BulkUpdateContactTagsRDTO>(){}
            )

            return true
        } catch (Exception ignored) {
            return false
        }
    }

    boolean bulkRemoveTagsContacts(KuorumUser user, BulkUpdateContactTagsRDTO bulkUpdateContactTags) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]

        try {
            restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.USER_CONTACT_TAGS,
                    params,
                    query,
                    bulkUpdateContactTags,
                    new TypeReference<BulkUpdateContactTagsRDTO>(){}
            )

            return true
        } catch (Exception ignored) {
            return false
        }
    }

}
