package kuorum.users

import com.mongodb.WriteConcern
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import org.apache.commons.lang.StringUtils
import org.bson.types.ObjectId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Represents the user in kuorum
 */
class KuorumUser {

    ObjectId id
    String name
    String surname
    String email
    String domain
    String nid
    String alias
    List<String> oldAlias
    String bio
//    String username
    String password
    AvailableLanguage language = AvailableLanguage.en_EN
    Boolean verified = Boolean.FALSE

    KuorumFile avatar
    KuorumFile imageProfile

    PersonalData personalData = new PersonalData()
    UserType userType = UserType.PERSON
    String timeZoneId;

    List<CommissionType> relevantCommissions = CommissionType.values()
    List<ObjectId> following  = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> followers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users

    List<ObjectId> favorites = [] //PostIds => Is the id instead of Post because gorm updates all

    SocialLinks socialLinks = new SocialLinks()

    List<String> tags

    String getFullName(){
        "${name} ${surname?:''}".trim()
    }

    static embedded = [
            'personalData',
            'authorities',
            'avatar',
            'activity',
            'imageProfile',
            'socialLinks',
            'relevantEvents',
            'professionalDetails'
    ]

    @Deprecated
    List<PoliticianRelevantEvent> relevantEvents
    ProfessionalDetails professionalDetails


    //Spring fields
//    SpringSecurityService springSecurityService

    boolean enabled = Boolean.TRUE
    boolean accountExpired = Boolean.FALSE
    boolean accountLocked = Boolean.FALSE
    boolean passwordExpired = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    public static final transient ALIAS_REGEX = "[a-zA-Z0-9_]{1,15}"
    public static final transient ALIAS_MAX_SIZE = 15
    static constraints = {
        name nullable:false
        surname nullable:true
        email nullable: false, email: true
        alias nullable:true, maxSize: ALIAS_MAX_SIZE, matches: ALIAS_REGEX, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAliasAndDomain(val.toLowerCase(), CustomDomainResolver.domain)){
                return "unique"
            }
            if (!val && obj.user && obj.user.enabled){
                return "nullable"
            }
        }
        oldAlias nullable:true
        password nullable:true
        bio nullable:true
        nid nullable: true
        avatar nullable:true
        imageProfile nullable:true
        userType nullable: false
        timeZoneId nullable:true

        //POLITICIAN VALIDATION
//        institution nullable:true
//        requestedPoliticianBetaTester nullable:true
        relevantEvents nulable:true
        professionalDetails nullable:true
        verified nullable: true
//        brainTreeId nullable:true
//        brainTreePaymentToken nullable:true
//        brainTreePaymentTokenNonce nullable:true
    }

    static mapping = {
//        email index:true, indexAttributes: [unique:true]
//        alias index:true, indexAttributes: [unique:true, sparse:true]
//        following cascade:"refresh"
//        followers cascade:"refresh"


        writeConcern WriteConcern.FSYNC_SAFE
    }

    TimeZone getTimeZone() {
        return timeZoneId ? TimeZone.getTimeZone(timeZoneId) : null
    }

    void setTimeZone(TimeZone timeZone) {
        this.timeZoneId = timeZone?.getID()?:null
    }

    String toString(){
        name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (o==null) return false
        if (!(o instanceof KuorumUser)) return false

        KuorumUser that = (KuorumUser) o

        if (email != that.email) return false

        return true
    }

    static transients = ['timeZone']

//    static mapping = {
//       password column: '`password`'
//    }

    def beforeInsert() {
        updateDenormalizedData()
    }

    def beforeUpdate() {
        log.debug("Se ha actualizado el usuario ${id}")
        updateDenormalizedData()
    }

    //Is not private for call it from service. I'm not proud for that
    void updateDenormalizedData(){
        email = plainText(email.toLowerCase())
        alias = plainText(alias?.toLowerCase())
        bio = plainText(bio)
        name = plainText(name)
        surname = plainText(surname)
        if (!followers) followers = []
        if (!personalData) {
            this.personalData = new PersonalData()
        }
    }

    private static String plainText(String str) {
        if (StringUtils.isBlank(str)){
            return "";
        }
        Document doc = Jsoup.parse(str);
        return doc.text();
    }

    int hashCode() {
        return id?id.hashCode():email.hashCode()
    }
}
