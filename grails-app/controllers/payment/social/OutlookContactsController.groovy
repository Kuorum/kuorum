package payment.social

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.IdToken
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.payment.contact.outlook.model.TokenResponse

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class OutlookContactsController {

	SpringSecurityService springSecurityService

	def index() {
		// Redirect to OAuth authorization page
		redirect([controller: 'oauth', action: 'authenticate', params: [provider: 'outlook']])
	}

	def callback() {
		// Check id_token
		IdToken idTokenObj = IdToken.parseEncodedToken(idToken.toString(), expectedNonce.toString());
		if (idTokenObj != null) {
			// Get token from auth_code and id_token
			TokenResponse tokenResponse = contactFromOutlookService.getTokenFromAuthCode(
					code.toString(), idTokenObj.getTenantId()
			);
			session.setAttribute("accessToken", tokenResponse.getAccessToken());
			session.setAttribute("userConnected", true);
			session.setAttribute("userName", idTokenObj.getName());
			session.setAttribute("userTenantId", idTokenObj.getTenantId());

			// Params for the pagination
			String sort = "GivenName ASC";
			String properties = "GivenName,Surname,CompanyName,EmailAddresses";
			Integer maxResults = 100;

			// Get contacts from user
//			PagedResult<Contact> contacts = outlookOAuthService.getContacts(
//					sort, properties, maxResults
//			);

//			System.out.println(contacts.getValue().length);
		} else {
			flash.error = "ID token failed validation."
		}
	}

	def success() {

	}

}
