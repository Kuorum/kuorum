package kuorum.register

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.payment.contact.outlook.model.TokenResponse

@Transactional
class OutlookOAuthService implements IOAuthService {

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

    OAuthToken createAuthToken(accessToken) {
        log.info("Creating Outlook OAuth Service");

        /*if (securityService.isLoggedIn()) {

            // Get contacts from user
            getContacts()

            return securityService.authentication.principal
        } else {
            // TODO: Create new user or log in user
            return null
        }*/

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
            )

            response;

            return new TokenResponse()
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
