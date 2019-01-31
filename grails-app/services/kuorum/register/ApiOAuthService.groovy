package kuorum.register

import com.fasterxml.jackson.core.type.TypeReference
import grails.converters.JSON
import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.users.KuorumUserService
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.scribe.model.Token
import org.springframework.security.core.userdetails.UserDetails

class ApiOAuthService implements IOAuthService{

    MongoUserDetailsService mongoUserDetailsService
    KuorumUserService kuorumUserService
    RegisterService registerService

    RestKuorumApiService restKuorumApiService

//    public static final String PASSWORD_PREFIX = "*kuorumApi*"

    OAuthToken createAuthToken(org.scribe.model.Token accessToken) {
        KuorumUserRSDTO user = getUserWithToken(accessToken.token)

        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)
        def authorities = mongoUserDetailsService.getRoles(user)
        OAuthToken oAuthToken = new KuorumApioAuthToken(accessToken, user.id, CustomDomainResolver.domain)
        OAuthToken.metaClass.newUser = false
        oAuthToken.metaClass = null
        oAuthToken.newUser = user

        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        oAuthToken
    }

    @Override
    Token createTokenFromAjaxParams(Map params) {
        String rawResponse = params as JSON
        String tokenString = params.token
        if (!tokenString){
            return null
        }
        org.scribe.model.Token token = new org.scribe.model.Token(tokenString, "", rawResponse)
        return token
    }

    private KuorumUserRSDTO getUserWithToken(String token){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.LOGIN,
                [:],
                [token:token],
                new TypeReference<KuorumUserRSDTO>(){}
        )
        KuorumUserRSDTO kuorumUserRSDTO = response.data

        return kuorumUserRSDTO
    }
}


class KuorumApioAuthToken extends OAuthToken{

    public static final String PROVIDER_NAME = 'KuorumApi'

    String id
    String domain

    KuorumApioAuthToken(Token accessToken, String id, String domain) {
        super(accessToken)
        this.id = id
        this.domain = domain
    }

    String getProviderName() {
        return "KuorumApi"
    }

    String getSocialId() {
        return id
    }

    String getScreenName() {
        return "${id}(${domain})"
    }
}