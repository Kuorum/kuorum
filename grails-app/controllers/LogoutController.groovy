
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.users.CookieUUIDService

class LogoutController {

	CookieUUIDService cookieUUIDService
	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		cookieUUIDService.removeUserUUID()
		// TODO put any pre-logout code here
        flash.message = flash.message
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}

