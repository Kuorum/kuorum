package payment.contact

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.users.KuorumUser

@Transactional
class YahooOAuthContactService implements IOAuthLoadContacts {

    private static final String CONTACTS_END_POINT = "/v1/user/{guid}/contacts"
    private static final String CONTACTS_SOCIAL_URL = "https://social.yahooapis.com"

    ContactService contactService

    @Override
    void loadContacts(KuorumUser user, token) {
        log.info("Creating Yahoo OAuth Service for GUID ${accessToken.getSecret()}");
        String contactsEndPoint = CONTACTS_END_POINT.replaceAll("\\{guid}", accessToken.secret)
        RESTClient client = new RESTClient(CONTACTS_SOCIAL_URL);
        try {
            def response = client.get(
                    path: contactsEndPoint,
                    query: [start: 0, count: 'max'],
                    requestContentType: ContentType.JSON
            );

            response;

        } catch (Exception e) {
            log.error("Error recovering contacts", e)
        }
    }
}
