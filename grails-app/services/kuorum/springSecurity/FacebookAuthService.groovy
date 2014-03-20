package kuorum.springSecurity

import com.the6hours.grails.springsecurity.facebook.FacebookAuthToken
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.UserType
import kuorum.users.FacebookUser
import kuorum.users.KuorumUser
import kuorum.users.PersonData
import kuorum.users.PersonalData
import kuorum.users.RoleUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.FacebookProfile
import org.springframework.social.facebook.api.impl.FacebookTemplate

class FacebookAuthService {

    private static final String FORMAT_BIRTHDAY_FACEBOOK = "MM/dd/yyyy"

    def mongoUserDetailsService

    /**
     * Called first time an user register with facebook
     * @link http://splix.github.io/grails-spring-security-facebook/guide/5%20Customization.html
     *
     * @param facebookAuthToken
     * @return
     */
    FacebookUser create(FacebookAuthToken facebookAuthToken) {
        FacebookUser facebookUser = new FacebookUser(
                accessToken:facebookAuthToken.accessToken.accessToken,
                uid:facebookAuthToken.uid,
                accessTokenExpires:facebookAuthToken.accessToken.expireAt
        )
        Facebook facebook = new FacebookTemplate(facebookAuthToken.accessToken.accessToken)
        FacebookProfile fbProfile = facebook.userOperations().userProfile
        log.info("Nuevo usario con facebook con email: ${fbProfile.email}" )


        Studies studies = recoverStudies(fbProfile)


//        def hometown = fbProfile.hometown


        KuorumUser user = KuorumUser.findByEmail(fbProfile.email)?:new KuorumUser(
                name: fbProfile.name,

//                username:fbProfile.username,
                email: fbProfile.email,
                password: "*facebook*${Math.random()}",
                accountExpired: false,
                enabled:true
        )
        if (user.userType == UserType.PERSON){
            PersonData personData = new PersonData()

            personData.gender = overwriteFieldIfNotFilled(user.personalData, "gender", fbProfile,{data ->Gender.valueOf(data.toUpperCase())?:Gender.FEMALE})
            personData.birthday =  overwriteFieldIfNotFilled(user.personalData, "birthday", fbProfile,{data ->(!data)?:Date.parse(FORMAT_BIRTHDAY_FACEBOOK,data)})
            personData.studies = (user.personalData.hasProperty("studies") && user.personalData.studies)?user.personalData.studies:studies

            user.personalData = personData
        }

        RoleUser roleUser = RoleUser.findByAuthority('ROLE_USER')
        user.authorities = [roleUser]
//        user.pathAvatar="http://graph.facebook.com/${facebookUser.uid}/picture?type=large"
//        SocialLinks socialLinks = new SocialLinks();
//        socialLinks.facebook = fbProfile.link
//        person.socialLinks = socialLinks
        user.save()
        facebookUser.user = user
        facebookUser.save()
    }

    private def overwriteFieldIfNotFilled(PersonalData personalData, String field, FacebookProfile fbProfile, def parseData){
        if (personalData.hasProperty(field) && personalData."$field")
            return personalData."$field"
        else
            parseData(fbProfile."$field")
    }

    private Studies recoverStudies(FacebookProfile fbProfile){
        def education = fbProfile.education.collect{it.type}
        Studies studies = null
        if (education.contains("High School"))
            studies = Studies.UNIVERSITY
        else if (education.contains("College"))
            studies = Studies.NONE
        else if (education.contains("College"))
            studies = Studies.STUDENT
        studies

    }
//    def afterCreate(kuorum.users.FacebookUser user, com.the6hours.grails.springsecurity.facebook.FacebookAuthToken token){
//        log.info("Afer facebook user created")
//
//    }
//    def createRoles(FacebookUser facebookUser){
//
//    }

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
