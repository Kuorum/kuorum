package kuorum.dashboard

import com.fasterxml.jackson.core.type.TypeReference
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.PageCampaignRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import payment.contact.ContactService

class DashboardService {

    RestKuorumApiService restKuorumApiService

    SpringSecurityService springSecurityService;
    ContactService contactService;

    boolean forceUploadContacts(){
        KuorumUser user = springSecurityService.currentUser
        ContactPageRSDTO contacts = contactService.getUsers(user)
        return contacts.total==0 && !user.skipUploadContacts
    }


    PageCampaignRSDTO findAllContactsCampaigns (KuorumUser user, String viewerUid = null, Integer page = 0){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [page:page]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS_CAMPAIGNS_ALL,
                params,
                query,
                new TypeReference<PageCampaignRSDTO>() {}
        );

        response.data
    }
}

