package payment

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import org.scribe.model.Token
import payment.contact.IOAuthLoadContacts

import javax.annotation.PreDestroy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ContactsOAuthController {

	public static final String SPRING_SECURITY_OAUTH_TOKEN = 'springSecurityOAuthToken'

	def grailsApplication
	def oauthService
	SpringSecurityService springSecurityService

	ExecutorService executor = Executors.newSingleThreadExecutor()
	@PreDestroy
	void shutdown() {
		executor.shutdownNow()
	}


	/**
	 * Is called on oauth callback
	 */
	def onSuccess() {
		// Validate the 'provider' URL. Any errors here are either misconfiguration
		// or web crawlers (or malicious users).
		if (!params.provider) {
			//flash.error = g.message(code: 'tools.contact.import.oauth.errorProvidero')
			flash.error = "The Spring Security OAuth callback URL must include the 'provider' URL parameter."
			redirect(mapping: 'politicianContacts')

			//renderError(400, "The Spring Security OAuth callback URL must include the 'provider' URL parameter.")
			return
		}

		def sessionKey = oauthService.findSessionKeyForAccessToken(params.provider)
		if (!session[sessionKey]) {
			flash.error = "No OAuth token in the session for provider '${params.provider}'!"
			redirect(mapping: 'politicianContacts')

			//renderError(500, "No OAuth token in the session for provider '${params.provider}'!")
			return
		}

		Token token = (Token) session[sessionKey]

		// Create the relevant authentication token and attempt to log in.
		String url = getRedirectUrl()
		KuorumUserSession loggedUser = springSecurityService.principal
		try {
			loadContacts(loggedUser, params.provider, token)
		} catch (KuorumException e) {
			flash.error = "Error importing contacts"
			url = g.createLink(mapping: "politicianContacts", absolute: true)
		}

		removeTokenAndRedirect(url)
	}

	def onFailure() {
		flash.error="Error recovering contacts"
		removeTokenAndRedirect(g.createLink(mapping: "politicianContacts", absolute: true))
	}

	private String getRedirectUrl() {
		g.createLink(mapping: "politicianContactImportSuccess", absolute: true)
	}

	protected void loadContacts(KuorumUserSession user, providerName, Token scribeToken) throws KuorumException {
		IOAuthLoadContacts providerService = (IOAuthLoadContacts) grailsApplication.mainContext.getBean("${providerName}OAuthContactService")
		executor.execute{
			providerService.loadContacts(user, scribeToken)
		}
	}

	protected void removeTokenAndRedirect(redirectUrl) {
		session.removeAttribute(SPRING_SECURITY_OAUTH_TOKEN)
		redirect(url: redirectUrl)
	}
}
