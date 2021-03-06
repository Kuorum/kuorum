package kuorum.core.springSecurity.handlers

import kuorum.users.KuorumUserService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public class SuccessAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    KuorumUserService kuorumUserService
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {
        if(authentication){
            String token = authentication?.accessToken?.accessToken
            kuorumUserService.checkFacebookFriendsByUserToken(token)
        }else{
            logger.info("Facebook token null: URI = ${request.requestURI}")
        }
        super.onAuthenticationSuccess(request, response, authentication)

    }
}