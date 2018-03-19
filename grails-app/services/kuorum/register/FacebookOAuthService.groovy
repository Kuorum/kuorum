package kuorum.register

import grails.converters.JSON
import grails.plugin.springsecurity.oauth.FacebookOAuthToken
import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.users.FacebookUser
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.RoleUser
import org.scribe.model.Token
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.User
import org.springframework.social.facebook.api.impl.FacebookTemplate
import springSecurity.KuorumRegisterCommand

class FacebookOAuthService implements IOAuthService {

    def mongoUserDetailsService
    def kuorumMailService
    KuorumUserService kuorumUserService
    RegisterService registerService

    public static final String PASSWORD_PREFIX = "*facebook*"

    private static final PROVIDER = "Facebook"

    @Override
    OAuthToken createAuthToken(Token accessToken) throws KuorumException {
        Facebook facebook = new FacebookTemplate(accessToken.token)
        User fbProfile = facebook.userOperations().userProfile

        // No email provided
        if (fbProfile.email == null) {
            log.error("El usuario no ha proporcionado un email en el login con Facebook" );
            throw new KuorumException("Email de Facebook no proporcionado", "login.rrss.error.noEmail");
        }

        KuorumUser user = KuorumUser.findByEmail(fbProfile.email)
        Boolean newUser = false;
        if (!user){
            user = createNewUser(fbProfile);
            newUser = true;
        }

        log.info("Logando suario '${user.email}' con facebook" )
        FacebookUser facebookUser = updateSavedAccessToken(accessToken, user, fbProfile)
        if (user.hasErrors() || !facebookUser || facebookUser.hasErrors()) {
            log.error("El usuario ${user} se ha logado usando faceboook y no se ha podido crear debido a estos errores: ${user.errors}" )
            throw new KuorumException("Ha habido errores al crear el usuario", "register.errors");
        }

        FacebookOAuthToken oAuthToken = new FacebookOAuthToken(accessToken, fbProfile.email)

        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)

        def authorities = mongoUserDetailsService.getRoles(user)

        OAuthToken.metaClass.newUser = false;
        oAuthToken.metaClass = null
        oAuthToken.newUser = newUser

        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        return oAuthToken
    }

    private KuorumUser createNewUser(User fbProfile ){
        KuorumRegisterCommand registerCommand = new KuorumRegisterCommand(
                email:fbProfile.email,
                name: fbProfile.firstName?:fbProfile.name,
                password: "${PASSWORD_PREFIX}${Math.random()}"
        )
        KuorumUser user = registerService.createUser(registerCommand)
        user.accountExpired = false
        user.accountLocked = false
        user.enabled = true
        user.password = registerCommand.password
        user.surname=fbProfile.lastName
        user.language = AvailableLanguage.fromLocaleParam(fbProfile?.locale?.language)
        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]

        user.save()
        createAvatar(user, fbProfile)

        log.info("Usuario ${user} creado usando facebook")
        kuorumMailService.sendRegisterUserViaRRSS(user, PROVIDER)
        kuorumMailService.sendWelcomeRegister(user)

        user
    }

    private FacebookUser updateSavedAccessToken(Token accessToken, KuorumUser user, User fbProfile){
        FacebookUser facebookUser = FacebookUser.findByUid(fbProfile.id)?:new FacebookUser(uid:fbProfile.id)
        facebookUser.accessToken = accessToken.token
        facebookUser.accessTokenExpires = getExpirationDateFromToken(accessToken)
        facebookUser.user = user
        facebookUser.save()
    }

    private Date getExpirationDateFromToken(Token accessToken){
        Calendar cal = Calendar.getInstance()
        def responseData = JSON.parse(accessToken.rawResponse)
        Integer secondsAlive = responseData.expires_in
        cal.add(Calendar.SECOND, secondsAlive)
        cal.getTime()
    }

    private void createAvatar(KuorumUser user, User fbProfile){
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

    @Override
    Token createTokenFromAjaxParams(Map params) {
        if (params.expiresIn){ // FACEBOOK STYLE
            params.put("expires_in", Integer.parseInt(params.expiresIn))
        }
        String rawResponse = params as JSON
        org.scribe.model.Token token = new org.scribe.model.Token(params.accessToken, params.signedRequest, rawResponse);
        return token;
    }
}
