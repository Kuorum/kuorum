package kuorum.users

import com.the6hours.grails.springsecurity.facebook.FacebookAccessToken
import com.the6hours.grails.springsecurity.facebook.FacebookAuthToken
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import kuorum.KuorumFile
import kuorum.core.model.Studies
import kuorum.helper.Helper
import kuorum.mail.KuorumMailService
import kuorum.springSecurity.FacebookAuthService
import org.springframework.social.facebook.api.*
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestOperations
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iduetxe on 20/03/14.
 */
@TestFor(FacebookAuthService)
@Mock([KuorumUser,FacebookUser,RoleUser, KuorumFile])
class FacebookAuthServiceSpec  extends Specification {

    static String ACCESS_TOKEN ="ACCES_TOKEN"
    static String MOCK_TESTING_EMAIL ="email@email.com"
    static String MOCK_TESTING_NAME ="NAME SURNAME"
    static String MOCK_TESTING_BIRTHDAY="24/06/1983"
    static long UID = 12345

    KuorumMailService kuorumMailServiceMock = Mock(KuorumMailService)

    def setup() {
        def facebookTemplateMock = createMockFacebook()
        service.kuorumMailService = kuorumMailServiceMock

        GroovySpy(FacebookTemplate, global: true)
        new FacebookTemplate(ACCESS_TOKEN)>> facebookTemplateMock

        RoleUser roleUser = new RoleUser(authority:"ROLE_USER").save()
    }

    def cleanup() {
    }

    @Unroll
    void "test creating user from facebook. Users in DDBB: #usersOnDB"() {
        given:"A facebook token and a DDBB filled"
        usersOnDB.each {email ->
            Helper.createDefaultUser(email).save()
        }

        FacebookAccessToken token = new FacebookAccessToken(accessToken: ACCESS_TOKEN, expireAt: (new Date() +1))
        FacebookAuthToken auth = new FacebookAuthToken(uid:UID,accessToken:token,code:"code",redirectUri:"http",principal:{})

        when:"It's created facebook user"
        FacebookUser facebookUser = service.create(auth)
        then:"Should be created his faccebok user and kuorum user"
        !facebookUser.hasErrors()
        KuorumUser.count() == numUsersAfterRegister
        KuorumUser user = KuorumUser.findByEmail(MOCK_TESTING_EMAIL)
        user.avatar.local == Boolean.FALSE
        user == facebookUser.user
        1 * kuorumMailServiceMock.sendRegisterUserViaRRSS(_,service.PROVIDER)
        if (usersOnDB.contains(MOCK_TESTING_NAME)){
            user.personalData.studies == Studies.DOCTOR
        }else{
            user.personalData.studies == null
        }
        where:
        usersOnDB                                   | numUsersAfterRegister
        [MOCK_TESTING_EMAIL, "email2@email.com"]    | 2
        ["email1@gmail.com", "email2@email.com"]    | 3
        [   ]                                       | 1
        [MOCK_TESTING_EMAIL]                        | 1

    }

    Facebook createMockFacebook(){
        new Facebook() {
            @Override
            CommentOperations commentOperations() {

            }

            @Override
            EventOperations eventOperations() {
                return null
            }

            @Override
            FeedOperations feedOperations() {
                return null
            }

            @Override
            FriendOperations friendOperations() {
                return null
            }

            @Override
            GroupOperations groupOperations() {
                return null
            }

            @Override
            LikeOperations likeOperations() {
                return null
            }

            @Override
            MediaOperations mediaOperations() {
                return null
            }

            @Override
            PageOperations pageOperations() {
                return null
            }

            @Override
            PlacesOperations placesOperations() {
                return null
            }

            @Override
            UserOperations userOperations() {
                new UserOperations() {
                    @Override
                    FacebookProfile getUserProfile() {
                        FacebookProfile fp = new FacebookProfile(
                                "${kuorum.users.FacebookAuthServiceSpec.UID}",
                                "username",
                                kuorum.users.FacebookAuthServiceSpec.MOCK_TESTING_NAME,
                                "firstName",
                                "lastName",
                                "male",
                                Locale.CHINA)
                        fp.birthday = kuorum.users.FacebookAuthServiceSpec.MOCK_TESTING_BIRTHDAY
                        fp.education = []
                        fp.email=kuorum.users.FacebookAuthServiceSpec.MOCK_TESTING_EMAIL
                        return fp
                    }

                    @Override
                    FacebookProfile getUserProfile(String s) {
                        return getUserProfile()
                    }

                    @Override
                    byte[] getUserProfileImage() {
                        return new byte[0]
                    }

                    @Override
                    byte[] getUserProfileImage(String s) {
                        return new byte[0]
                    }

                    @Override
                    byte[] getUserProfileImage(ImageType imageType) {
                        return new byte[0]
                    }

                    @Override
                    byte[] getUserProfileImage(String s, ImageType imageType) {
                        return new byte[0]
                    }

                    @Override
                    List<String> getUserPermissions() {
                        return null
                    }

                    @Override
                    List<org.springframework.social.facebook.api.Reference> search(String s) {
                        return null
                    }
                }
            }

            @Override
            RestOperations restOperations() {
                return null
            }

            @Override
            boolean isAuthorized() {
                return false
            }

            @Override
            def <T> T fetchObject(String s, Class<T> tClass) {
                return null
            }

            @Override
            def <T> T fetchObject(String s, Class<T> tClass, MultiValueMap<String, String> stringStringMultiValueMap) {
                return null
            }

            @Override
            def <T> List<T> fetchConnections(String s, String s2, Class<T> tClass, String... strings) {
                return null
            }

            @Override
            def <T> List<T> fetchConnections(String s, String s2, Class<T> tClass, MultiValueMap<String, String> stringStringMultiValueMap) {
                return null
            }

            @Override
            byte[] fetchImage(String s, String s2, ImageType imageType) {
                return new byte[0]
            }

            @Override
            String publish(String s, String s2, MultiValueMap<String, Object> stringObjectMultiValueMap) {
                return null
            }

            @Override
            void post(String s, String s2, MultiValueMap<String, String> stringStringMultiValueMap) {

            }

            @Override
            void delete(String s) {

            }

            @Override
            void delete(String s, String s2) {

            }
        }
    }
}
