package kuorum.springSecurity

import grails.converters.JSON
import grails.plugin.springsecurity.oauth.GoogleOAuthToken
import grails.plugin.springsecurity.oauth.OAuthLoginException
import grails.plugin.springsecurity.oauth.OAuthToken
import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.users.OAuthID
import kuorum.users.PersonData
import kuorum.users.PersonalData
import kuorum.users.RoleUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.social.facebook.api.FacebookProfile

@Transactional
class GoogleOAuthService implements IOAuthService{

    def oauthService
    def mongoUserDetailsService
    def kuorumMailService

    private static final String GOOLE_USER_INFO_URL = 'https://www.googleapis.com/oauth2/v1/userinfo'
    private static final String PROVIDER = 'google'

    OAuthToken createAuthToken(accessToken) {
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
        if (!user){
            user = createUser(googleUser)
            OAuthID oAuthID = new OAuthID(provider:PROVIDER,accessToken:accessToken,user:user)
            oAuthID.save()
            kuorumMailService.sendRegisterUserViaRRSS(user)
        }
        createAvatar(user, googleUser)
        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)
        def authorities = mongoUserDetailsService.getRoles(user)

        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        oAuthToken
    }

    private KuorumUser createUser(def googleUser){
        KuorumUser user = new KuorumUser(
                name: googleUser.name,

//                username:fbProfile.username,
                email: googleUser.email,
                password: "*google*${Math.random()}",
                accountExpired: false,
                enabled:true
        )

        if (user.userType == UserType.PERSON){
            PersonData personData = new PersonData()

            personData.gender = overwriteFieldIfNotFilled(user.personalData, "gender", googleUser,{data ->Gender.valueOf(data.toUpperCase())?:Gender.FEMALE})
//            personData.birthday =  overwriteFieldIfNotFilled(user.personalData, "birthday", googleUser,{data ->(!data)?:Date.parse(FORMAT_BIRTHDAY_FACEBOOK,data)})
//            personData.studies = (user.personalData.hasProperty("studies") && user.personalData.studies)?user.personalData.studies:studies

            user.personalData = personData
        }


        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]
//        user.pathAvatar="http://graph.facebook.com/${facebookUser.uid}/picture?type=large"
//        SocialLinks socialLinks = new SocialLinks();
//        socialLinks.facebook = fbProfile.link
//        person.socialLinks = socialLinks

        if (user.save()){
            return user
        }else{
            log.error("El usuario ${user} se ha logado usando faceboook y no se ha podido crear debido a estos errores: ${user.errors}" )
        }
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
                fileGroup:FileGroup.USER_AVATAR
            )
            kuorumFile.save()
            user.avatar = kuorumFile
            user.save()
        }
    }
}
