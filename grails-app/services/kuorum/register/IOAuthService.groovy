package kuorum.register

import grails.plugin.springsecurity.oauth.OAuthToken

/**
 * This interface should to be implemented by all OAuth services that are in kuorum
 */
interface IOAuthService {

    OAuthToken createAuthToken(accessToken)

}
