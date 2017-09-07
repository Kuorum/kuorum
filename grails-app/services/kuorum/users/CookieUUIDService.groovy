package kuorum.users

import grails.plugin.cookie.CookieService
import grails.plugin.springsecurity.SpringSecurityService
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
                 path:"/",
                 domain:WebConstants.COOKIE_DOMAIN])
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

    String getPaymentRedirect(){
        String urlRedirect = cookieService.getCookie(WebConstants.COOKIE_PAYMENT_REDIRECT)
        return urlRedirect ;
    }

    void setPaymentRedirect(String urlRedirect){
        cookieService.setCookie(
                [name:WebConstants.COOKIE_PAYMENT_REDIRECT,
                 value:urlRedirect,
                 maxAge:Integer.MAX_VALUE ,
                 path:"/",
                 domain:WebConstants.COOKIE_DOMAIN])
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
