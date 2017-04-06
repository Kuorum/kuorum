package kuorum.users

import com.mongodb.WriteConcern
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.KuorumFile
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.users.extendedPoliticianData.*
import org.bson.types.ObjectId

/**
 * Represents the user in kuorum
 *
 * Is not separated with inheritance (Politician, Organization, Person) because an user can switch between them,
 * and is a nightmare handle it
 */
class KuorumUser {

    ObjectId id
    String name
    String surname
    String email
    String alias
    List<String> oldAlias
    String bio
//    String username
    String password
    AvailableLanguage language = AvailableLanguage.en_EN
    Boolean verified = Boolean.FALSE
    Boolean skipUploadContacts = Boolean.FALSE

    KuorumFile avatar
    KuorumFile imageProfile

        PersonalData personalData = new PersonData()
    UserType userType = UserType.PERSON
    String timeZoneId;

    @Deprecated
    Boolean requestedPoliticianBetaTester = Boolean.FALSE

    EditorRules editorRules

    List<CommissionType> relevantCommissions = CommissionType.values()
    List<ObjectId> following  = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> followers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> subscribers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users

    Integer numFollowers = 0

    List<ObjectId> favorites = [] //PostIds => Is the id instead of Post because gorm updates all

    List<MailType> availableMails = MailType.values()

    Gamification gamification = new Gamification()
    Activity activity = new Activity()
    SocialLinks socialLinks = new SocialLinks()
    KuorumUser organization

    List<String> tags

    Notice notice


    String getFullName(){
        "${name} ${surname?:''}".trim()
    }
//    static hasMany = [following:KuorumUser,followers:KuorumUser,subscribers:KuorumUser]

    static embedded = [
            'personalData',
            'authorities',
            'gamification',
            'avatar',
            'activity',
            'politicianActivity',
            'imageProfile',
            'socialLinks',
            'notice',
            'relevantEvents',
            'professionalDetails',
            'politicianExtraInfo',
            'careerDetails',
            'institutionalOffice',
            'politicalOffice',
            'editorRules'
    ]

    /**
     * Represents the last time that the user checked the notifications
     */
    Date lastNotificationChecked = new Date()

    PoliticianActivity politicianActivity

    List<PoliticianRelevantEvent> relevantEvents
    ProfessionalDetails professionalDetails
    CareerDetails careerDetails
    PoliticianExtraInfo politicianExtraInfo
    OfficeDetails institutionalOffice
    OfficeDetails politicalOffice




    //Spring fields
    SpringSecurityService springSecurityService
    KuorumUserAuditService kuorumUserAuditService

    boolean enabled = Boolean.TRUE
    boolean accountExpired = Boolean.FALSE
    boolean accountLocked = Boolean.FALSE
    boolean passwordExpired = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    Integer activityForRecommendation = 0


    public static final transient ALIAS_REGEX = "[a-zA-Z0-9_]{1,15}"
    public static final transient ALIAS_MAX_SIZE = 15
    static constraints = {
        name nullable:false
        surname nullable:true
        email nullable: false, email: true
        alias nullable:true, unique:true, maxSize: ALIAS_MAX_SIZE, matches: ALIAS_REGEX
        oldAlias nullable:true
        password nullable:true
        bio nullable:true
        avatar nullable:true
        imageProfile nullable:true
        userType nullable: false
        notice nullable: true
        skipUploadContacts nullable: true
        editorRules nullable:true
        timeZoneId nullable:true

        //POLITICIAN VALIDATION
//        institution nullable:true
        requestedPoliticianBetaTester nullable:true
        organization nullable: true
        politicianActivity nullable:true
        relevantEvents nulable:true
        professionalDetails nullable:true
        institutionalOffice nullable:true
        politicalOffice nullable:true
        careerDetails nullable:true
        politicianExtraInfo nullable:true
    }

    static mapping = {
        email index:true, indexAttributes: [unique:true]
        alias index:true, indexAttributes: [unique:true, sparse:true]
//        following cascade:"refresh"
//        followers cascade:"refresh"
//        subscribers cascade:"refresh"


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

    static transients = ["springSecurityService", 'activityForRecommendation', 'kuorumUserAuditService', 'timeZone']

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
        email = email.toLowerCase()
        alias = alias?.toLowerCase()
        if (!followers) followers = []
        numFollowers = followers.size()
        if (!personalData){
            this.personalData = new PersonalData()
        }
        personalData.userType = userType
    }


    int hashCode() {
        return id?id.hashCode():email.hashCode()
    }
}
