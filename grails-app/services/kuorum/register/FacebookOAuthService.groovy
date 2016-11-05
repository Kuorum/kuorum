package kuorum.register

import grails.plugin.springsecurity.oauth.FacebookOAuthToken
import grails.plugin.springsecurity.oauth.OAuthToken
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.Studies
import kuorum.users.FacebookUser
import kuorum.users.KuorumUser
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

    private static final String FORMAT_BIRTHDAY_FACEBOOK = "MM/dd/yyyy"

    def mongoUserDetailsService
    def kuorumMailService
    RegisterService registerService

    public static final String PASSWORD_PREFIX = "*facebook*"

    private static final PROVIDER = "Facebook"

    @Override
    OAuthToken createAuthToken(Token accessToken) {


        Facebook facebook = new FacebookTemplate(accessToken.token)
        FacebookProfile fbProfile = facebook.userOperations().userProfile
        log.info("Nuevo usario con facebook con email: ${fbProfile.email}" )

        FacebookUser facebookUser = FacebookUser.findByUid(fbProfile.id)?:new FacebookUser(uid:fbProfile.id)
        facebookUser.accessToken = accessToken.token
        facebookUser.accessTokenExpires = getExpirationDateFromToken(accessToken)

        //YA no pedimos los estudios
//        Studies studies = recoverStudies(fbProfile)


//        def hometown = fbProfile.hometown

        KuorumRegisterCommand registerCommand = new KuorumRegisterCommand(
                email:fbProfile.email,
                name: fbProfile.name,
                password: "${PASSWORD_PREFIX}${Math.random()}"
        )
        KuorumUser user = KuorumUser.findByEmail(fbProfile.email)?:registerService.createUser(registerCommand)
        user.accountExpired = false
        user.accountLocked = false
        user.enabled = true
        user.alias = user.alias?:fbProfile.username
        //Ya no pedimos datos personales
//        if (user.userType == UserType.PERSON){
//            log.info("${fbProfile.email} es una persona")
//            PersonData personData = new PersonData()
//
//            personData.gender = overwriteFieldIfNotFilled(user.personalData, "gender", fbProfile,{data ->Gender.valueOf(data.toUpperCase())?:Gender.FEMALE})
//            personData.birthday =  overwriteFieldIfNotFilled(user.personalData, "birthday", fbProfile,{data ->(!data)?:Date.parse(FORMAT_BIRTHDAY_FACEBOOK,data)})
//            personData.studies = (user.personalData.hasProperty("studies") && user.personalData.studies)?user.personalData.studies:studies
//
//            user.personalData = personData
//        }

        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]
//        user.pathAvatar="http://graph.facebook.com/${facebookUser.uid}/picture?type=large"
//        SocialLinks socialLinks = new SocialLinks();
//        socialLinks.facebook = fbProfile.link
//        person.socialLinks = socialLinks
        user.save()
        createAvatar(user, fbProfile)
        facebookUser.user = user
        facebookUser.save()
        if (user.hasErrors() || facebookUser.hasErrors()){
            log.error("El usuario ${user} se ha logado usando faceboook y no se ha podido crear debido a estos errores: ${user.errors}" )
        }else{
            log.info("Usuario ${user} creado usando facebook")
            kuorumMailService.sendRegisterUserViaRRSS(user, PROVIDER)
        }
        facebookUser

        FacebookOAuthToken oAuthToken = new FacebookOAuthToken(accessToken, fbProfile.email)
        UserDetails userDetails =  mongoUserDetailsService.createUserDetails(user)
        def authorities = mongoUserDetailsService.getRoles(user)

        oAuthToken.principal = userDetails
        oAuthToken.authorities = authorities
        oAuthToken
    }

    private Date getExpirationDateFromToken(Token accessToken){
        Calendar cal = Calendar.getInstance()
        Integer secondsAlive = Integer.parseInt(accessToken.rawResponse.split("&")[1].split("=")[1]);
        cal.add(Calendar.SECOND, secondsAlive)
        cal.getTime()
    }
    private def overwriteFieldIfNotFilled(PersonalData personalData, String field, FacebookProfile fbProfile, def parseData){
        try{
            if (personalData.hasProperty(field) && personalData."$field")
                return personalData."$field"
            else
                parseData(fbProfile."$field")
        }catch (Exception e){
            log.warn("Eror recuperando los datos personales (${field}) de facebook del usuario ${fbProfile.email}. [Excpt: ${e.getMessage()}]")
        }
    }

    private Studies recoverStudies(FacebookProfile fbProfile){
        Studies studies = null
        try{
            def education = fbProfile.education.collect{it.type}
            if (education.contains("High School"))
                studies = Studies.UNIVERSITY
            else if (education.contains("College"))
                studies = Studies.NONE
            else if (education.contains("College"))
                studies = Studies.STUDENT
        }catch(Exception e){
            log.warn("Error recuperando los estudios de facebook: [Except:${e.getMessage()}]")
        }
        studies
    }
//    def afterCreate(kuorum.users.FacebookUser user, com.the6hours.grails.springsecurity.facebook.FacebookAuthToken token){
//        log.info("Afer facebook user created")
//
//    }
//    def createRoles(FacebookUser facebookUser){
//
//    }
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

    def getPrincipal(KuorumUser user){
        mongoUserDetailsService.createUserDetails(user)
    }

    Collection<GrantedAuthority> getRoles(FacebookUser user){
        mongoUserDetailsService.getRoles(user.user)
    }

    def findUser(long uid){
        FacebookUser.findByUid(uid)
    }
}
