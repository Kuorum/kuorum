package payment.campaign

import grails.transaction.Transactional
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.contact.ContactRSDTO

@Transactional
class CampaignService {

    RestKuorumApiService restKuorumApiService;


}
