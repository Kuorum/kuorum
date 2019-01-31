package payment.contact

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.contact.ContactRDTO
import org.scribe.model.Token

@Transactional
class YahooOAuthContactService implements IOAuthLoadContacts {

    private static final String CONTACTS_END_POINT = "/v1/user/{guid}/contacts"
    private static final String CONTACTS_SOCIAL_URL = "https://social.yahooapis.com"

    def grailsApplication

    ContactService contactService

    @Override
    void loadContacts(KuorumUserSession user, Token accessToken) throws KuorumException {
        log.info("Creating Yahoo OAuth Service for user ${user.username} [GUID ${accessToken.getSecret()}]")
        def listContacts = getListContacts(accessToken)
        List<ContactRDTO> contacts = listContacts.collect{transformYahooContact(it)}.findAll{it}
        contactService.addBulkContacts(user,contacts)
    }

    private ContactRDTO transformYahooContact(def infoContact) {
        Set tags = []
        String name = ""
        String surname = ""
        String email = ""
        String notes = ""
        try{
            email = infoContact.fields.find{it.type=="email"}?.value
            notes = infoContact.fields.find{it.type=="notes"}?.value
            def infoName = infoContact.fields.find{it.type=="name"}?.value
            name = infoName?"${infoName.givenName}".trim():""
            surname = infoName?"${infoName.familyName}".trim():""

            String guid = infoContact.fields.find{it.type=="guid"}?.value
            String company = infoContact.fields.find{it.type=="company"}?.value
            if (company){
                tags << company
            }
            return new ContactRDTO(email: email, name:name,surname: surname, notes:notes, tags: tags)
        }catch (Exception e){
            log.info("Yahoo contact not created due to an exception [Exc: ${e.getMessage()}]",)
            return null
        }
    }

    private def getListContacts(Token accessToken){
        String contactsEndPoint = CONTACTS_END_POINT.replaceAll("\\{guid}", accessToken.getSecret())
        RESTClient client = new RESTClient(CONTACTS_SOCIAL_URL)
        client.getHeaders().put("Authorization", "Bearer " + accessToken.token)
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
            )
            return response.data.contacts.contact

        } catch (Exception e) {
            log.error("Error recovering contacts", e)
            return null
        }
    }
}
