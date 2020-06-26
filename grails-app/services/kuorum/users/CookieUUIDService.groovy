    package kuorum.users

import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
    import kuorum.core.customDomain.CustomDomainResolver
    import kuorum.web.constants.WebConstants

class CookieUUIDService {

    SpringSecurityService springSecurityService
    CookieService cookieService

    String getUserUUID(){
        String evaluatorId = cookieService.getCookie(WebConstants.COOKIE_USER_UUID)
        if (springSecurityService.isLoggedIn()){
            evaluatorId = springSecurityService.principal.id.toString()
        }
        return evaluatorId;
    }

    void setUserUUID(String evaluatorId){
        cookieService.setCookie(
                [name:WebConstants.COOKIE_USER_UUID,
                 value:evaluatorId,
                 maxAge:Integer.MAX_VALUE ,
                 path:WebConstants.COOKIE_PATH,
                 domain:WebConstants.COOKIE_DOMAIN])
    }

    void removeUserUUID(){
        cookieService.deleteCookie(WebConstants.COOKIE_USER_UUID,WebConstants.COOKIE_PATH,WebConstants.COOKIE_DOMAIN)
    }

    /**
     * Recovers the UUID of the user from the cookie.
     *
     * If not exits, creates new one and saves it on the cookie
     * @return
     */
    String buildUserUUID(){
        String userUUID = getUserUUID();
        if (!userUUID){
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
        cookieService.deleteCookie(cookieName, WebConstants.COOKIE_PATH, CustomDomainResolver.domain)
    }

    String getRememberPasswordRedirect(){
        String urlRedirect = cookieService.getCookie(WebConstants.COOKIE_URL_CALLBACK_REMEMBER_PASS)
        if (urlRedirect){
            return URLDecoder.decode(urlRedirect,"UTF-8")+"#recoverStatus";// Hash to launch the recover event on js
        }else{
            return null;
        }
    }
}
