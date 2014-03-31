/*
 * Copyright 2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kuorum.users

import grails.plugin.springsecurity.oauth.OAuthToken
import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.SpringSecurityUtils
import kuorum.springSecurity.IOAuthService
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.savedrequest.DefaultSavedRequest

import kuorum.users.KuorumUser
import kuorum.users.RoleUser
import kuorum.users.KuorumUserRoleUser
import kuorum.users.OAuthID

/**
 * Simple helper controller for handling OAuth authentication and integrating it
 * into Spring Security.
 */
class SpringSecurityOAuthController {

    public static final String SPRING_SECURITY_OAUTH_TOKEN = 'springSecurityOAuthToken'

    def grailsApplication
    def oauthService
    def springSecurityService

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

        // Create the relevant authentication token and attempt to log in.
        OAuthToken oAuthToken = createAuthToken(params.provider, session[sessionKey])

        authenticateAndRedirect(oAuthToken, defaultTargetUrl)
    }

    def onFailure = {
        authenticateAndRedirect(null, defaultTargetUrl)
    }


    // utils

    protected renderError(code, msg) {
        log.error msg + " (returning ${code})"
        render status: code, text: msg
    }

    protected OAuthToken createAuthToken(providerName, scribeToken) {
        IOAuthService providerService = grailsApplication.mainContext.getBean("${providerName}OAuthService")
        OAuthToken oAuthToken = providerService.createAuthToken(scribeToken)
        oAuthToken.authenticated = true
        return oAuthToken
    }

    protected Map getDefaultTargetUrl() {
        def config = SpringSecurityUtils.securityConfig
        def savedRequest = SpringSecurityUtils.getSavedRequest(session)
        def defaultUrlOnNull = '/'

        if (savedRequest && !config.successHandler.alwaysUseDefault) {
            return [url: (savedRequest.redirectUrl ?: defaultUrlOnNull)]
        } else {
            return [uri: (config.successHandler.defaultTargetUrl ?: defaultUrlOnNull)]
        }
    }

    protected void authenticateAndRedirect(OAuthToken oAuthToken, redirectUrl) {
        session.removeAttribute SPRING_SECURITY_OAUTH_TOKEN

        SecurityContextHolder.context.authentication = oAuthToken
        redirect(redirectUrl instanceof Map ? redirectUrl : [uri: redirectUrl])
    }

}
