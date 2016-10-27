package payment

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.core.exception.KuorumException
import kuorum.register.IOAuthService
import kuorum.users.KuorumUser
import org.springframework.security.core.context.SecurityContextHolder
import payment.contact.IOAuthLoadContacts

class ContactsOAuthController {

	public static final String SPRING_SECURITY_OAUTH_TOKEN = 'springSecurityOAuthToken'

	def grailsApplication
	def oauthService
	SpringSecurityService springSecurityService

	/**
	 * Is called on oauth callback
	 */
	def onSuccess = {
		// Validate the 'provider' URL. Any errors here are either misconfiguration
		// or web crawlers (or malicious users).
		if (!params.provider) {
			renderError 400, "The Spring Security OAuth callback URL must include the 'provider' URL parameter."
			return
		}

		def sessionKey = oauthService.findSessionKeyForAccessToken(params.provider)
		if (!session[sessionKey]) {
			renderError 500, "No OAuth token in the session for provider '${params.provider}'!"
			return
		}

		org.scribe.model.Token token = session[sessionKey]
		// Create the relevant authentication token and attempt to log in.
		String url = getRedirectUrl()
		KuorumUser loggedUser = springSecurityService.currentUser
		try{
			loadContacts(loggedUser, params.provider, token)
		}catch (KuorumException e){
			url = g.createLink(mapping: "politicianContacts")
			flash.error = "Error importing contacts"
		}

		removeTokenAndRedirect( url)
	}

	def onFailure = {
		removeTokenAndRedirect( g.createLink(mapping: "politicianContacts", absolute: true))
	}

	private String getRedirectUrl(){
		g.createLink(mapping: "politicianContactSuccess", absolute: true)
	}


	protected void loadContacts(KuorumUser user, providerName, scribeToken) throws KuorumException {
		IOAuthLoadContacts providerService = grailsApplication.mainContext.getBean("${providerName}OAuthContactService")
		providerService.loadContacts(user, scribeToken)
	}

	protected void removeTokenAndRedirect( redirectUrl) {
		session.removeAttribute SPRING_SECURITY_OAUTH_TOKEN
		redirect (url: redirectUrl)
	}
}
