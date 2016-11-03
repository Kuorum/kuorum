package payment.contact

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRDTO
import org.scribe.model.Token

@Transactional
class YahooOAuthContactService implements IOAuthLoadContacts {

    private static final String CONTACTS_END_POINT = "/v1/user/{guid}/contacts"
    private static final String CONTACTS_SOCIAL_URL = "https://social.yahooapis.com"

    def grailsApplication

    ContactService contactService

    @Override
    void loadContacts(KuorumUser user, Token accessToken) throws KuorumException {
        log.info("Creating Yahoo OAuth Service for GUID ${accessToken.getSecret()}");
        def listContacts = getListContacts(accessToken)
        List<ContactRDTO> contacts = listContacts.collect{
            transformYahooContact(it)
        }
        contactService.addBulkContacts(contacts)
    }

    private ContactRDTO transformYahooContact(def infoContact) {
        def infoName = infoContact.fields.find{it.type=="name"}?.value
        String email = infoContact.fields.find{it.type=="email"}?.value
        String guid = infoContact.fields.find{it.type=="guid"}?.value
        String name = "${infoName.givenName} ${infoName.familyName}".trim()
        ContactRDTO contactRDTO = new ContactRDTO(email: email, name:name);
    }

    private def getListContacts(Token accessToken){
        String contactsEndPoint = CONTACTS_END_POINT.replaceAll("\\{guid}", accessToken.getSecret())
        RESTClient client = new RESTClient(CONTACTS_SOCIAL_URL);
        client.getHeaders().put("Authorization", "Bearer " + accessToken.token);
        Map query = [
                start: 0,
                count: 'max',
                format: 'json',
        ]
        try {
            def response = client.get(
                    path: contactsEndPoint,
                    query: query,
                    requestContentType: ContentType.JSON
            );
            return response.data.contacts.contact

        } catch (Exception e) {
            log.error("Error recovering contacts", e)
            return null
        }
    }
}
