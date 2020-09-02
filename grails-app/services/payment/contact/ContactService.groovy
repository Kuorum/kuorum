package payment.contact

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.contact.*
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class ContactService {

    RestKuorumApiService restKuorumApiService

    void addBulkContacts(BasicDataKuorumUserRSDTO user, List<ContactRDTO> contactRSDTOs){
        addBulkContacts(user.id, contactRSDTOs)
    }
    void addBulkContacts(KuorumUserSession user, List<ContactRDTO> contactRSDTOs){
        addBulkContacts(user.id.toString(), contactRSDTOs)
    }
    void addBulkContacts(String userId, List<ContactRDTO> contactRSDTOs){
        Map<String, String> params = [userId:userId]
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

    List<String> getUserTags(KuorumUserSession user){
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

    List<ExtendedFilterRSDTO> getUserFilters(KuorumUserSession user){
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

    ExtendedFilterRSDTO createFilter(KuorumUserSession user, FilterRDTO filterRSDTO){
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

    ExtendedFilterRSDTO updateFilter(KuorumUserSession user, FilterRDTO filterRSDTO, Long filterId){
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

    ExtendedFilterRSDTO getFilter(KuorumUserSession user, Long filterId){
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

    void removeFilter(KuorumUserSession user, Long filterId){
        Map<String, String> params = [userId:user.id.toString(),filterId:filterId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILTER,
                params,
                query)

    }

    ContactPageRSDTO getUsers(KuorumUserSession user, FilterRDTO filterRDTO = null){
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO(filter:filterRDTO)
        getUsers(user, searchContactRSDTO)
    }

    ContactPageRSDTO getUsers(KuorumUserSession user, SearchContactRSDTO searchContactRSDTO){
        Map<String, String> params = [userId:user.id.toString()]


        Map<String, String> query = searchContactRSDTO.encodeAsQueryParams()

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS,
                params,
                query,
                new TypeReference<ContactPageRSDTO>(){})
        ContactPageRSDTO contactPage = new ContactPageRSDTO()
        if (response.data){
            contactPage = response.data
        }
        contactPage
    }

    ContactRSDTO getContact(KuorumUserSession user, Long contactId){
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

    void removeContact(KuorumUserSession user, Long contactId){
        Map<String, String> params = [userId:user.id.toString(),contactId:contactId.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT,
                params,
                query)
    }

    ContactRSDTO updateContact(KuorumUserSession user, ContactRDTO contactRDTO, Long contactId){
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

    ContactRSDTO addContact(KuorumUserSession user, ContactRDTO contactRDTO){
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


    ContactRSDTO checkContactUser(BasicDataKuorumUserRSDTO user, String email, String digest){
        Map<String, String> params = [userId:user.id]
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

    boolean unsubscribeContactUser(BasicDataKuorumUserRSDTO user, String email, String digest){
        Map<String, String> params = [userId:user.id]
        Map<String, String> query = [contactEmail:email, digest:digest]
        try{
            def response= restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.USER_CONTACT_SUBSCRIBE,
                    params,
                    query)
            return true
        } catch (Exception e) {
            log.warn("Someone trying to check conctact '${email}' of the user ${user.alias} that not extits")
            return false
        }
    }


    boolean unsubscribeContactUser(KuorumUserSession user, Long contactId, String digest){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = [contactId : contactId, digest:digest]
        try{
            def response= restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.USER_CONTACT_SUBSCRIBE,
                    params,
                    query)
            return true
        } catch (Exception e) {
            log.warn("Someone trying to check conctact '${contactId}' of the user ${user.alias} that not exist")
            return false
        }
    }

    boolean bulkRemoveContacts(KuorumUserSession user, SearchContactRSDTO searchContact) {
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

    boolean bulkAddTagsContacts(KuorumUserSession user, BulkUpdateContactTagsRDTO bulkUpdateContactTags) {
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

    boolean bulkRemoveTagsContacts(KuorumUserSession user, BulkUpdateContactTagsRDTO bulkUpdateContactTags) {
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

    void exportContacts(KuorumUserSession user, SearchContactRSDTO searchContactRSDTO){
        Map<String, String> params = [userId:user.id.toString()]
        Map<String, String> query = searchContactRSDTO.encodeAsQueryParams()

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_REPORT,
                params,
                query,
                null)
    }


    String getSocialImportContactUrl(String userId, String provider){
        Map<String, String> params = [provider:provider]
        Map<String, String> query = [userId:userId]

        def response= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_SOCIAL_IMPORT,
                params,
                query,
                new TypeReference<String>(){})
        return ((StringReader)response.data).str
    }


    String uploadFile(KuorumUserSession user, Long contactId, File file, String fileName){
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8")
        Map<String, String> params = [contactId: contactId.toString(), userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILES,
                params,
                query,
                file,
                fileName
        )
    }


    void deleteFile(KuorumUserSession user, Long contactId, String fileName){
        Map<String, String> params = [contactId: contactId.toString(), userId: user.id.toString()]
        Map<String, String> query = [fileName:fileName]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILES,
                params,
                query,
                new TypeReference<String>(){}
        )
    }

    List<String> getFiles( KuorumUserSession userSession, ContactRSDTO contactRSDTO){
        Map<String, String> params = [contactId: contactRSDTO.getId().toString(), userId: userSession.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FILES,
                params,
                query,
                new TypeReference<List<String>>(){}
        )
        response.data
    }

}
