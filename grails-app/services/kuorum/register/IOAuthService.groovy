package kuorum.register

import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.core.exception.KuorumException

/**
 * This interface should to be implemented by all OAuth services that are in kuorum
 */
interface IOAuthService {

    OAuthToken createAuthToken(org.scribe.model.Token accessToken) throws KuorumException

    org.scribe.model.Token createTokenFromAjaxParams(Map params);

}
