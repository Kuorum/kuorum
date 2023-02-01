package kuorum.users

import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.register.KuorumUserSession
import kuorum.web.constants.WebConstants

import javax.servlet.http.Cookie

class CookieUUIDService {

    SpringSecurityService springSecurityService
    CookieService cookieService

    // I don't know why  cookieService.deleteCookie(cookie) doesn't delete the cookie. That's why I'm going to use a logic delete with this text
    private static final String DELETED_COOKIE_VALUE = "KUORUM_DELETED";
    public static final String COOKIE_BROWSER_ID = "BROWSER_ID";

    String getUserUUID() {
        String evaluatorId = cookieService.getCookie(WebConstants.COOKIE_USER_UUID)
        if (springSecurityService.isLoggedIn()) {
            evaluatorId = springSecurityService.principal.id.toString()
        }
        return evaluatorId;
    }

    KuorumUserSession buildAnonymousUser(String userId) {
        if (springSecurityService.isLoggedIn()) {
            log.info("VALIDATION: User ${springSecurityService.principal.id.toString()} ->anonymous user recovered from user logged")
            return springSecurityService.principal
        } else {
            log.info("VALIDATION: User ${userId} -> Setting cookie")
            return createAnonymousUserSession(userId);
        }
    }

    private createAnonymousUserSession(String userId) {
        String uuid = userId
        if (userId) {
            // Updating UUID with the new one
            setUserUUID(userId);
        } else {
            uuid = getUserUUID();
        }
        return new KuorumUserSession(
                uuid,
                uuid,
                "",
                false,
                true,
                true,
                true,
                [],
                uuid,
                uuid,
                null,
                null,
                null
        )
    }

    void setUserUUID(String evaluatorId) {
        cookieService.setCookie(
                [name    : WebConstants.COOKIE_USER_UUID,
                 value   : evaluatorId,
                 maxAge  : Integer.MAX_VALUE,
                 path    : WebConstants.COOKIE_PATH,
                 domain  : CustomDomainResolver.domain,
                 secure  : true,
                 httpOnly: false])
    }

    void removeUserUUID() {
        // NOT WORKING
        Cookie cookie = cookieService.findCookie(WebConstants.COOKIE_USER_UUID)
        setUserUUID(DELETED_COOKIE_VALUE)
        if (cookie) {
            cookieService.deleteCookie(cookie)
        }
    }

    Boolean isUserUUIDSet() {
        Cookie cookie = cookieService.findCookie(WebConstants.COOKIE_USER_UUID)
        return cookie != null && cookie.getValue() != DELETED_COOKIE_VALUE
    }

    /**
     * Recovers the UUID of the user from the cookie.
     *
     * If not exits, creates new one and saves it on the cookie
     * @return
     */
    String buildUserUUID() {
        String userUUID = getUserUUID();
        if (!userUUID || userUUID == DELETED_COOKIE_VALUE) {
            userUUID = UUID.randomUUID().toString()
            setUserUUID(userUUID)
        }
        return userUUID;
    }

    String getDomainCookie(String cookieName){
        String urlRedirect = cookieService.getCookie(cookieName)
        return urlRedirect ;
    }

    void setDomainCookie(String cookieName, String value){
        cookieService.setCookie(
                [name:cookieName,
                 value:value,
                 maxAge:Integer.MAX_VALUE ,
                 path:WebConstants.COOKIE_PATH,
                 domain: CustomDomainResolver.domain])
    }

    void deleteDomainCookie(String cookieName){
        if (cookieName) {
            cookieService.deleteCookie(cookieName, WebConstants.COOKIE_PATH, CustomDomainResolver.domain)
        }
    }

    String getRememberPasswordRedirect() {
        String urlRedirect = cookieService.getCookie(WebConstants.COOKIE_URL_CALLBACK_REMEMBER_PASS)
        if (urlRedirect) {
            return URLDecoder.decode(urlRedirect, "UTF-8") + "#recoverStatus";// Hash to launch the recover event on js
        } else {
            return null;
        }
    }

    String getBrowserId() {
        String browserId = cookieService.getCookie(COOKIE_BROWSER_ID)
        if (browserId) {
            return browserId
        }
        browserId = UUID.randomUUID().toString()
        cookieService.setCookie(
                [name  : COOKIE_BROWSER_ID,
                 value : browserId,
                 maxAge: Integer.MAX_VALUE,
                 path  : WebConstants.COOKIE_PATH,
                 domain: CustomDomainResolver.domain])
    }
}
