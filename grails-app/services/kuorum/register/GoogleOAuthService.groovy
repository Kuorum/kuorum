package kuorum.register

import grails.converters.JSON
import grails.plugin.springsecurity.oauth.GoogleOAuthToken
import grails.plugin.springsecurity.oauth.OAuthLoginException
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.Gender
import kuorum.notifications.NotificationService
import kuorum.users.*
import kuorum.util.LookUpEnumUtil
import org.springframework.security.core.userdetails.UserDetails
import springSecurity.KuorumRegisterCommand

@Transactional
class GoogleOAuthService implements IOAuthService{

    def oauthService
    def mongoUserDetailsService
    def kuorumMailService
    KuorumUserService kuorumUserService
    RegisterService registerService

    private static final String GOOLE_USER_INFO_URL = 'https://www.googleapis.com/oauth2/v1/userinfo'
    private static final String PROVIDER = 'Google+'
    public static final String PASSWORD_PREFIX = "*google*"

    OAuthToken createAuthToken(org.scribe.model.Token accessToken) {
        def response = oauthService.getGoogleResource(accessToken, GOOLE_USER_INFO_URL)
        def googleUser
        try {
            googleUser = JSON.parse(response.body)
        } catch (Exception e) {
            log.error "Error parsing response from Google. Response:\n${response.body}"
            throw new OAuthLoginException('Error parsing response from Google', e)
        }
        if (!googleUser?.email) {
            log.error "No user email from Google. Response:\n${response.body}"
            throw new OAuthLoginException('No user email from Google')
        }
        log.info("Creating user with google: ${googleUser.email}")
        GoogleOAuthToken oAuthToken =  new GoogleOAuthToken(accessToken, googleUser.email)

        KuorumUser user = KuorumUser.findByEmail(googleUser.email)
        Boolean newUser = false;
        if (!user){
            user = createUser(googleUser);
            newUser = true;
        }

        OAuthID oAuthID = new OAuthID(provider:PROVIDER.toLowerCase(),accessToken:accessToken,user:user)
        oAuthID.save()

        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)
        def authorities = mongoUserDetailsService.getRoles(user)

        OAuthToken.metaClass.newUser = false;
        oAuthToken.metaClass = null
        oAuthToken.newUser = newUser

        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        oAuthToken
    }

    private KuorumUser createUser(def googleUser){
        KuorumRegisterCommand registerCommand = new KuorumRegisterCommand(
                email:googleUser.email,
                name: googleUser.name,
                password: "${PASSWORD_PREFIX}${Math.random()}"
        )
        KuorumUser user = registerService.createUser(registerCommand)
        user.password = registerCommand.password
        user.accountExpired = false;
        user.accountLocked = false
        user.enabled = true;
        user.alias = kuorumUserService.generateValidAlias(registerCommand.name)

//        if (user.userType == UserType.PERSON){
            PersonData personData = new PersonData()

            personData.gender = overwriteFieldIfNotFilled(
                    user.personalData, "gender",
                    googleUser,
                    {data -> LookUpEnumUtil.lookup(Gender.class, data,Gender.FEMALE)})
//            personData.birthday =  overwriteFieldIfNotFilled(user.personalData, "birthday", googleUser,{data ->(!data)?:Date.parse(FORMAT_BIRTHDAY_FACEBOOK,data)})
//            personData.studies = (user.personalData.hasProperty("studies") && user.personalData.studies)?user.personalData.studies:studies

            user.personalData = personData
//        }
        user.language = AvailableLanguage.fromLocaleParam(googleUser.locale)?:AvailableLanguage.en_EN


        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]

        if (!user.save()){
            log.error("El usuario ${user} se ha logado usando google y no se ha podido crear debido a estos errores: ${user.errors}" )
            return null;
        }

        createAvatar(user, googleUser)
        kuorumMailService.sendRegisterUserViaRRSS(user,PROVIDER)
        kuorumMailService.sendWelcomeRegister(user)
        return user;
    }

    private def overwriteFieldIfNotFilled(PersonalData personalData, String field,def user,def parseData){
        if (personalData.hasProperty(field) && personalData."$field")
            return personalData."$field"
        else
            parseData(user."$field")
    }

    private void createAvatar(KuorumUser user, def googleUser){
        if (!user.avatar && googleUser.has("picture")){
            KuorumFile kuorumFile = new KuorumFile(
                user:user,
                local:Boolean.FALSE,
                temporal:Boolean.FALSE,
                storagePath:null,
                fileName:null,
                url:googleUser.picture,
                originalName: "avatar.png",
                urlThumb: googleUser.picture,
                fileGroup:FileGroup.USER_AVATAR
            )
            kuorumFile.save()
            user.avatar = kuorumFile
            user.save()
        }
    }
}
