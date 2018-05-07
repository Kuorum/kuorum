import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import springSecurity.KuorumRegisterCommand

import javax.servlet.http.HttpServletResponse

class LoginController {

	/**
	 * Dependency injection for the authenticationTrustResolver.
	 */
	def authenticationTrustResolver

	/**
	 * Dependency injection for the springSecurityService.
	 */
	def springSecurityService

	RegisterService registerService;

	/**
	 * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
	 */
	def index = {
		if (springSecurityService.isLoggedIn()) {
			redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
		} else {
			redirect mapping:'loginAuth', params: params
		}
	}

	/**
	 * Show the login page.
	 */
	def auth = {

		def config = SpringSecurityUtils.securityConfig

		if (springSecurityService.isLoggedIn()) {
			redirect uri: config.successHandler.defaultTargetUrl
			return
		}

		String view = 'auth'
		String postUrl = "${CustomDomainResolver.getBaseUrlAbsolute()}${config.apf.filterProcessesUrl}"
		String username = params.email?:''
		render view: view, model: [postUrl: postUrl,
		                           rememberMeParameter: config.rememberMe.parameter,
								   username:username]
	}

    def headAuth = {
        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
            return
        }

        KuorumRegisterCommand registerCommand = new KuorumRegisterCommand()
        render template:"/layouts/noLoggedHead",
                model: [
                        registerCommand: registerCommand,
                        rememberMeParameter: config.rememberMe.parameter
                ]
    }

    def loginForm = {
//        def config = SpringSecurityUtils.securityConfig
//        String postUrl = "${config.loginDomain}${config.apf.filterProcessesUrl}"
        render template: "/layouts/loginForm", model:[ modalId:'registro']
    }

    def homeLogin = {
        def config = SpringSecurityUtils.securityConfig

        if (springSecurityService.isLoggedIn()) {
            redirect uri: config.successHandler.defaultTargetUrl
            return
        }

        String view = 'auth'
        String postUrl = "${config.loginDomain}${config.apf.filterProcessesUrl}"
        render template:"/dashboard/landingPageModules/loginHome", model: [postUrl: postUrl]
    }

	/**
	 * The redirect action for Ajax requests.
	 */
	def authAjax = {
		response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
		response.sendError HttpServletResponse.SC_UNAUTHORIZED
	}

	def checkEmailAndPass = {
		String email = params.j_username
		String pass = params.j_password
		KuorumUser user = KuorumUser.findByEmailAndDomain(email, CustomDomainResolver.domain);
		Boolean valid = registerService.isValidPassword(user, pass);
		render valid.toString();
	}

	def modalAuth = {
		String email = params.j_username
		String pass = params.j_password
		KuorumUser user = KuorumUser.findByEmailAndDomain(email, CustomDomainResolver.domain);
		if (registerService.isValidPassword(user, pass)){
			springSecurityService.reauthenticate(email, pass)
			render ([success:true, url: g.createLink(mapping:'loginAuthError')] as JSON)
		}else{
//			session[WebAttributes.AUTHENTICATION_EXCEPTION] = new Exception("Wrong pass");
			render ([success:false, url: g.createLink(mapping:'loginAuthError', params: [email:email]),  error: g.message(code:'springSecurity.errors.login.fail')] as JSON)
		}
	}

	/**
	 * Show denied page.
	 */
	def denied = {
		if (springSecurityService.isLoggedIn() &&
				authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
			// have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
			redirect mapping:'loginFull', params: params
		}
	}

	/**
	 * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
	 */
	def full = {
//		def config = SpringSecurityUtils.securityConfig
		render view: 'auth', params: params,
			model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
			        postUrl: "${CustomDomainResolver.baseUrlAbsolute}${config.apf.filterProcessesUrl}"]
	}

	/**
	 * Callback after a failed login. Redirects to the auth page with a warning message.
	 */
	def authfail = {

		def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		String msg = ''
		def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
		if (exception) {
			if (exception instanceof AccountExpiredException) {
				msg = g.message(code: "springSecurity.errors.login.expired")
			}
			else if (exception instanceof CredentialsExpiredException) {
				msg = g.message(code: "springSecurity.errors.login.passwordExpired")
			}
			else if (exception instanceof DisabledException) {
				msg = g.message(code: "springSecurity.errors.login.disabled")
			}
			else if (exception instanceof LockedException) {
//				msg = g.message(code: "springSecurity.errors.login.locked")
                redirect mapping:'registerResendMail', params:[email:exception.authentication.principal]
                return
			}
			else {
				msg = g.message(code: "springSecurity.errors.login.fail")
			}
		}

		if (springSecurityService.isAjax(request)) {
			render([error: msg] as JSON)
		}
		else {
			flash.message = msg
			redirect mapping:'loginAuth', params: params
		}
	}

	/**
	 * The Ajax success redirect url.
	 */
	def ajaxSuccess = {
		render([success: true, username: springSecurityService.authentication.name] as JSON)
	}

	/**
	 * The Ajax denied redirect url.
	 */
	def ajaxDenied = {
		render([error: 'access denied'] as JSON)
	}
}

