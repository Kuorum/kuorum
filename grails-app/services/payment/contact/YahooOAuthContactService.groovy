package payment.contact

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.users.KuorumUser

@Transactional
class YahooOAuthContactService implements IOAuthLoadContacts{

    ContactService contactService

    @Override
    void loadContacts(KuorumUser user, Object accessToken) {
        log.info("Creating Yahoo OAuth Service");
    }
}
