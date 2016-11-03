package payment.contact

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser
import org.apache.commons.codec.binary.Base64
import org.scribe.builder.api.DefaultApi10a
import org.scribe.model.Token
import org.scribe.services.HMACSha1SignatureService
import org.scribe.services.SignatureService

@Transactional
class YahooOAuthContactService implements IOAuthLoadContacts {

    private static final String CONTACTS_END_POINT = "/v1/user/{guid}/contacts"
    private static final String CONTACTS_SOCIAL_URL = "https://social.yahooapis.com"

    def grailsApplication

    ContactService contactService

    @Override
    void loadContacts(KuorumUser user, Token accessToken) throws KuorumException {
        log.info("Creating Yahoo OAuth Service for GUID ${accessToken.getSecret()}");
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

            response;

        } catch (Exception e) {
            log.error("Error recovering contacts", e)
        }
    }
}
