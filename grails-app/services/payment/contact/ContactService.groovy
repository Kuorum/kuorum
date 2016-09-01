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
class ContactService {

    RestKuorumApiService restKuorumApiService;

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

    ContactPageRSDTO getUsers(KuorumUser user, FilterRDTO filterRDTO = null){
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO(filter:filterRDTO);
        getUsers(user, searchContactRSDTO)
    }

    ContactPageRSDTO getUsers(KuorumUser user, SearchContactRSDTO searchContactRSDTO){
        Map<String, String> params = [userId:user.id.toString()]


        Map<String, String> query = convertObjectToQueryParams("",searchContactRSDTO)

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

    ContactRSDTO updateContact(KuorumUser user, ContactRSDTO contactRSDTO){
        Map<String, String> params = [userId:user.id.toString(),contactId:contactRSDTO.id.toString()]
        Map<String, String> query = [:]

        def response= restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACT,
                params,
                query,
                contactRSDTO,
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

    private Map<String, String> convertObjectToQueryParams(String path, def obj){
        Map<String, String> data = [:]
        path = path?"$path.":""
        if (obj){
            def filtered = ['class', 'active', 'metaClass']
            obj.properties.findAll{!filtered.contains(it.key)}.collect{k,v ->
                if (v && (getWrapperTypes().contains(v.class) || v instanceof Enum) ){
                    data.put("${path}${k}", v.toString())
                }else if (v instanceof Collection){
                    v.eachWithIndex { it, i ->
                        data.putAll(convertObjectToQueryParams("$path$k[$i]", it))
                    }
                }else{
                    data.putAll(convertObjectToQueryParams("$path$k", v))
                }
            }
        }
        data
    }
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        return ret;
    }
}
