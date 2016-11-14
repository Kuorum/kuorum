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
        contactService.addBulkContacts(user,contacts)
    }

    private ContactRDTO transformYahooContact(def infoContact) {
        Set tags = []
        String name = ""
        String email = ""
        String notes = ""
        try{
            def infoName = infoContact.fields.find{it.type=="name"}?.value
            email = infoContact.fields.find{it.type=="email"}?.value
            notes = infoContact.fields.find{it.type=="notes"}?.value
            name = infoName?"${infoName.givenName} ${infoName.familyName}".trim():email

            String guid = infoContact.fields.find{it.type=="guid"}?.value
            String company = infoContact.fields.find{it.type=="company"}?.value
            if (company){
                tags << company
            }
        }catch (Exception e){
            log.info("Yahoo contact not created due to an exception [Exc: ${e.getMessage()}]",)
        }
        new ContactRDTO(email: email, name:name, notes:notes, tags: tags);
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
