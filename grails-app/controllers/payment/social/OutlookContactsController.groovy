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

}
