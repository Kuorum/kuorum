
import grails.plugin.springsecurity.SpringSecurityUtils

class LogoutController {
	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		// TODO put any pre-logout code here
        flash.message = flash.message
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}

