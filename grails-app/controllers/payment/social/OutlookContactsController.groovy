package payment.social

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.payment.contact.outlook.model.Contact
import kuorum.payment.contact.outlook.model.IdToken
import kuorum.payment.contact.outlook.model.PagedResult
import kuorum.payment.contact.outlook.model.TokenResponse
import kuorum.register.OutlookOAuthService


@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class OutlookContactsController {

	//ContactFromOutlookService contactFromOutlookService;
	OutlookOAuthService outlookOAuthService
	SpringSecurityService springSecurityService

	def index() {
		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();

		// Set session attrs
		request.session.setAttribute("expected_state", state);
		request.session.setAttribute("expected_nonce", nonce);

		// Redirect to Authorization Url
		redirect(url: outlookOAuthService.getUrlForAuthorization(state, nonce))
	}

	def authorizeCallback() {
		def code = params.get("code");
		def idToken = params.get("id_token");
		def state = params.get("state");

		UUID expectedState = (UUID) request.session.getAttribute("expected_state");
		UUID expectedNonce = (UUID) request.session.getAttribute("expected_nonce");

		UUID stateUUID = null;
		try {
			stateUUID = UUID.fromString(state.toString());
		} catch (Exception e) {
			flash.error = "Unexpected state returned from authority."
			return;
		}

		if (stateUUID.equals(expectedState)) {
			session.setAttribute("authCode", code);
			session.setAttribute("idToken", idToken);

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
				PagedResult<Contact> contacts = contactFromOutlookService.getContacts(
						sort, properties, maxResults
				);

				contacts;
				contacts.getValue();

				System.out.println(contacts.getValue().length);
			} else {
				flash.error = "ID token failed validation."
			}
		} else {
			flash.error = "Unexpected state returned from authority."
		}

		/*KuorumUser user = springSecurityService.currentUser
		String code = params.code
		contactFromOutlookService.loadContacts(user, code, getRedirectUrl())
		redirect(mapping: "politicianContactImportOutlookSuccess")*/
	}

	/*private String getRedirectUrl() {
		g.createLink(controller: 'outlookContacts', action: 'loadContactsFromOutlook', absolute: true)
	}*/

	def success() {

	}

}
