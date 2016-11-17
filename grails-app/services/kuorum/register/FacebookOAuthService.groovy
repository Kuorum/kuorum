package kuorum.register

import grails.plugin.springsecurity.oauth.FacebookOAuthToken
import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.Studies
import kuorum.users.FacebookUser
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PersonalData
import kuorum.users.RoleUser
import org.scribe.model.Token
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FacebookProfile
import org.springframework.social.facebook.api.impl.FacebookTemplate
import springSecurity.KuorumRegisterCommand

class FacebookOAuthService implements IOAuthService{

    def mongoUserDetailsService
    def kuorumMailService
    KuorumUserService kuorumUserService
    RegisterService registerService

    public static final String PASSWORD_PREFIX = "*facebook*"

    private static final PROVIDER = "Facebook"

    @Override
    OAuthToken createAuthToken(Token accessToken) {


        Facebook facebook = new FacebookTemplate(accessToken.token)
        FacebookProfile fbProfile = facebook.userOperations().userProfile

        KuorumUser user = KuorumUser.findByEmail(fbProfile.email)?:createNewUser(fbProfile)

        log.info("Logando suario '${user.email}' con facebook" )
        FacebookUser facebookUser = updateSavedAccessToken(accessToken, user, fbProfile)
        if (user.hasErrors() || !facebookUser || facebookUser.hasErrors()){
            log.error("El usuario ${user} se ha logado usando faceboook y no se ha podido crear debido a estos errores: ${user.errors}" )
            return null;
        }

        FacebookOAuthToken oAuthToken = new FacebookOAuthToken(accessToken, fbProfile.email)

        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)

        def authorities = mongoUserDetailsService.getRoles(user)
        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        oAuthToken
    }

    private KuorumUser createNewUser(FacebookProfile fbProfile ){
        KuorumRegisterCommand registerCommand = new KuorumRegisterCommand(
                email:fbProfile.email,
                name: fbProfile.name,
                password: "${PASSWORD_PREFIX}${Math.random()}"
        )
        KuorumUser user = registerService.createUser(registerCommand)
        user.accountExpired = false
        user.accountLocked = false
        user.enabled = true
        user.password = registerCommand.password
        user.alias = user.alias?:kuorumUserService.generateValidAlias(fbProfile.username?:fbProfile.name)
        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]

        user.save()
        createAvatar(user, fbProfile)

        log.info("Usuario ${user} creado usando facebook")
        kuorumMailService.sendRegisterUserViaRRSS(user, PROVIDER)
        kuorumMailService.sendWelcomeRegister(user)

        user
    }

    private FacebookUser updateSavedAccessToken(Token accessToken, KuorumUser user, FacebookProfile fbProfile){
        FacebookUser facebookUser = FacebookUser.findByUid(fbProfile.id)?:new FacebookUser(uid:fbProfile.id)
        facebookUser.accessToken = accessToken.token
        facebookUser.accessTokenExpires = getExpirationDateFromToken(accessToken)
        facebookUser.user = user
        facebookUser.save()
    }

    private Date getExpirationDateFromToken(Token accessToken){
        Calendar cal = Calendar.getInstance()
        Integer secondsAlive = Integer.parseInt(accessToken.rawResponse.split("&")[1].split("=")[1]);
        cal.add(Calendar.SECOND, secondsAlive)
        cal.getTime()
    }

    private void createAvatar(KuorumUser user, FacebookProfile fbProfile){
        if (!user.avatar){
            String imageUrl = "https://graph.facebook.com/${fbProfile.id}/picture?type=large"
            KuorumFile kuorumFile = new KuorumFile(
                    user:user,
                    originalName: user.name,
                    urlThumb: imageUrl,
                    local:Boolean.FALSE,
                    temporal:Boolean.FALSE,
                    storagePath:null,
                    fileName:null,
                    url:imageUrl,
                    fileGroup:FileGroup.USER_AVATAR,
                    fileType: FileType.IMAGE
            )
            if (kuorumFile.save()){
                user.avatar = kuorumFile
                user.save()
            }else{
                log.warn("No se ha podido salvar la imagen del usuario de facebook ${fbProfile.email}. Errors: ${kuorumFile.errors}")
            }
        }
    }
}
