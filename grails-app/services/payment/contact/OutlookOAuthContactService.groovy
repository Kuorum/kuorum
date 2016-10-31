package payment.contact

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.people.v1.PeopleScopes
import com.google.api.services.plusDomains.PlusDomains
import com.google.api.services.plusDomains.PlusDomainsScopes
import com.google.api.services.plusDomains.model.Circle
import com.google.api.services.plusDomains.model.CircleFeed
import com.google.api.services.plusDomains.model.PeopleFeed
import com.google.api.services.plusDomains.model.Person
import com.google.common.io.Files
import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.contacts.ContactEntry
import com.google.gdata.data.contacts.ContactFeed
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRDTO

import javax.annotation.PreDestroy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Transactional
class OutlookOAuthContactService implements IOAuthLoadContacts{

    ContactService contactService

    @Override
    void loadContacts(KuorumUser user, org.scribe.model.Token accessToken) {
        log.info("Creating Outlook OAuth Service");
    }

    //SpringSecurityService securityService

    public enum OutlookUrl {
        BASE_API ("https://outlook.office.com"),
        CONTACTS ("/api/v2.0/me/contacts");

        String url;
        OutlookUrl(String url) {
            this.url = url;
        }

        String buildUrl(String contextPath, Map<String,String> params) {
            String builtUrl = url
            params.each{ k, v -> builtUrl = builtUrl.replaceAll("\\{${k}}", v) }
            contextPath + builtUrl
        }

        String toString() {
            return url;
        }
    }

    public PagedResult<Contact> getContacts(String sort, String properties, Integer maxResults) {
        HashMap<String, String> query = new HashMap<>();
        query.put("$orderby", sort);
        query.put("$select", properties);
        query.put("$top", maxResults);

        RESTClient client = new RESTClient(OutlookUrl.BASE_API);
        try {
            def response = client.get(
                    path: OutlookUrl.CONTACTS,
                    query: query,
                    requestContentType: ContentType.JSON
            );

            response;

            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*public TokenResponse ensureTokens(TokenResponse tokens, String tenantId) {
		// Are tokens still valid?
		Calendar now = Calendar.getInstance();
		if (now.getTime().before(tokens.getExpirationTime())) {
			// Still valid
			return tokens;
		} else {
			// Set POST params
			HashMap<String, String> query = new HashMap<>();
			query.put("tenantid", tenantId)
			query.put("client_id", getAppId())
			query.put("client_secret", getAppPassword())
			query.put("grant_type", "refresh_token")
			query.put("refresh_token", tokens.getRefreshToken())
			query.put("redirect_uri", getRedirectUrl())

			RESTClient client = new RESTClient(OutlookUrl.BASE_API);
			try {
				def response = client.post(
						query: query,
						requestContentType: ContentType.JSON
				)

				response;

				return response
			} catch (Exception e) {
				System.out.println(e.getMessage())
			}
		}
	}*/
}
