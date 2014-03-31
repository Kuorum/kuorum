package kuorum.springSecurity

import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.users.OAuthID

/**
 * This interface should to be implemented by all OAuth services that are in kuorum
 */
interface IOAuthService {

    OAuthToken createAuthToken(accessToken)

}
