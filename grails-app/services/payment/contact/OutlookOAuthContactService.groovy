package payment.contact

import com.fasterxml.jackson.databind.ObjectMapper
import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import groovyx.net.http.URIBuilder
import kuorum.core.exception.KuorumException
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.EmailAddress
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.payment.contact.outlook.model.TokenResponse
import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.contact.ContactRDTO
import org.scribe.model.OAuthConstants

@Transactional
class OutlookOAuthContactService implements IOAuthLoadContacts {

    public static final String GRANT_TYPE = "grant_type"
    public static final String REFRESH_TOKEN = "refresh_token"
    public static final String REDIRECT_URI = "redirect_uri"

    private static final String BASE_API = "https://outlook.office.com"
    private static final String CONTACTS = "/api/v2.0/me/contacts"

    def grailsApplication

    ContactService contactService

    @Override
    void loadContacts(KuorumUserSession user, org.scribe.model.Token token) {
        log.info("Creating Outlook OAuth Service for user ${user.username}")
        // Params for the pagination
        String properties = "GivenName,Surname,EmailAddresses"
        Integer maxResults = 1000

        // Convert Token to TokenResponse
        def mapper = new ObjectMapper()
        TokenResponse tokenResponse = mapper.readValue(token.getRawResponse(), TokenResponse.class)

        // Get all contacts (per page)
        ArrayList<ContactRDTO> contactRDTOs = []
        PagedResult<Contact> pagedResult = null
        while (pagedResult == null || pagedResult.nextPageLink != null) {
            if (pagedResult != null && pagedResult.nextPageLink != null) {
                // Next pages
                pagedResult = getContacts(tokenResponse, properties, maxResults, pagedResult.nextPageLink)
            } else {
                // First page
                pagedResult = getContacts(tokenResponse, properties, maxResults)
            }

            for (Contact contact : pagedResult.value) {
                if (contact.emailAddresses.length > 0) {
                    try{
                        def newContact = new ContactRDTO()
                        newContact.setName(contact.givenName)
                        newContact.setSurname(contact.surname)
                        newContact.setEmail(contact.emailAddresses[0].getAddress())
                        contactRDTOs.add(newContact)
                    }catch (Exception e){
                        log.info("Error recovering outlook email due to exception [Excp: ${e.getMessage()}]")
                    }
                }
            }

            contactService.addBulkContacts(user, contactRDTOs)
            contactRDTOs.clear()

            // Refresh token
            tokenResponse = refreshToken(tokenResponse)
        }
    }

    PagedResult<Contact> getContacts(TokenResponse tokenResponse, String properties, Integer maxResults) {
        // Params to search
        HashMap<String, String> query = new HashMap<>()
        query.put("\$select", properties)
        query.put("\$top", String.valueOf(maxResults))

        URIBuilder uri = new URIBuilder(BASE_API + CONTACTS)
        uri.setQuery(query)

        return getContacts(tokenResponse, uri.toString())
    }

    PagedResult<Contact> getContacts(TokenResponse tokenResponse, String nextPageLink) {
        RESTClient client = new RESTClient(nextPageLink)
        HttpResponseDecorator response = client.get(
            headers: [
                    "Authorization": String.format("Bearer %s", tokenResponse.getAccessToken())
            ],
            requestContentType: ContentType.JSON
        )

        if (response.hasProperty("responseData") && response.responseData != null) {

            // Fix: create manually the object
            PagedResult<Contact> pagedResult = new PagedResult<>()
            pagedResult.nextPageLink = response.responseData.get('@odata.nextLink')

            // Fix: For each contact - not efficient
            ArrayList<TreeMap> aux = response.responseData.value
            ArrayList<Contact> arrayContact = new ArrayList<>()
            for (TreeMap treeMap : aux) {
                Contact newContact = new Contact()
                newContact.setId(treeMap.get('Id'))
                newContact.setGivenName(treeMap.get('GivenName'))
                newContact.setSurname(treeMap.get('Surname'))

                ArrayList<TreeMap> auxEmailList = treeMap.get('EmailAddresses')
                ArrayList<EmailAddress> arrayEmailAddress = new ArrayList<>()
                for (TreeMap auxEmail: auxEmailList) {
                    EmailAddress newEmail = new EmailAddress()
                    newEmail.setName(auxEmail.get('Name'))
                    newEmail.setAddress(auxEmail.get('Address'))
                    arrayEmailAddress.add(newEmail)
                }
                newContact.setEmailAddresses((EmailAddress[]) arrayEmailAddress.toArray())

                arrayContact.add(newContact)
            }
            pagedResult.value = arrayContact

            return pagedResult
        } else {
            throw new KuorumException("Error while importing contacts")
        }
    }

    /**
     * Refresh token when possible
     */
    TokenResponse refreshToken(TokenResponse tokenResponse) {
		Calendar now = Calendar.getInstance()
        if (now.getTime().before(tokenResponse.getExpirationTime())) {
			// Still valid
			return tokenResponse
        } else {
            // Update access token
            def outlookConfig = grailsApplication.config.oauth.providers.outlook
			HashMap<String, String> query = new HashMap<>()
			query.put(OAuthConstants.CLIENT_ID, (String) outlookConfig.key)
			query.put(OAuthConstants.CLIENT_SECRET, (String) outlookConfig.secret)
			query.put(GRANT_TYPE, REFRESH_TOKEN)
			query.put(REFRESH_TOKEN, tokenResponse.getRefreshToken())
			query.put(REDIRECT_URI, (String) outlookConfig.callback)

			RESTClient client = new RESTClient(BASE_API)
            try {
				def response = client.post(
                    query: query,
                    requestContentType: ContentType.JSON
				)

                def mapper = new ObjectMapper()
                tokenResponse = mapper.readValue((String) response.responseData, TokenResponse.class)

                return tokenResponse
            } catch (Exception e) {
                throw new KuorumException("")
            }
		}
	}
}
