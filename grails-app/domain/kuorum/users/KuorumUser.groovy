package kuorum.users

import com.mongodb.WriteConcern
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.KuorumFile
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity
import kuorum.users.extendedPoliticianData.OfficeDetails
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo
import kuorum.users.extendedPoliticianData.PoliticianLeaning
import kuorum.users.extendedPoliticianData.PoliticianRelevantEvent
import kuorum.users.extendedPoliticianData.PoliticianTimeLine
import kuorum.users.extendedPoliticianData.ProfessionalDetails
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
    String email
    String alias
    String bio
//    String username
    String password
    AvailableLanguage language = AvailableLanguage.es_ES
    Boolean verified = Boolean.FALSE

    KuorumFile avatar
    KuorumFile imageProfile

    PersonalData personalData = new PersonData()
    UserType userType = UserType.PERSON

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
            'externalPoliticianActivities',
            'timeLine',
            'relevantEvents',
            'politicianLeaning',
            'professionalDetails',
            'politicianExtraInfo',
            'careerDetails'
    ]

    /**
     * Represents the last time that the user checked the notifications
     */
    Date lastNotificationChecked = new Date()

    //Politician FIELDS
//    PoliticalParty politicalParty
//    Institution institution
    @Deprecated //Use ProfesionalDetails.region
//    Region politicianOnRegion
    PoliticianActivity politicianActivity

    List<ExternalPoliticianActivity> externalPoliticianActivities
    List<PoliticianRelevantEvent> relevantEvents
    List<PoliticianTimeLine> timeLine
    PoliticianLeaning politicianLeaning
    ProfessionalDetails professionalDetails
    PoliticianExtraInfo politicianExtraInfo
    CareerDetails careerDetails
    OfficeDetails institutionalOffice
    OfficeDetails politicalOffice




    //Spring fields
    SpringSecurityService springSecurityService

    boolean enabled = Boolean.TRUE
    boolean accountExpired = Boolean.FALSE
    boolean accountLocked = Boolean.FALSE
    boolean passwordExpired = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    Integer activityForRecommendation = 0

    static constraints = {
        name nullable:false //Limit size will be added
        email nullable: false, email: true
        alias nullable:true
        password nullable:true
        bio nullable:true
        avatar nullable:true
        imageProfile nullable:true
        userType nullable: false, validator:{val, obj ->
            obj.personalData.userType == val
        }
        notice nullable: true


        //POLITICIAN VALIDATION
//        institution nullable:true
        organization nullable: true
        politicianActivity nullable:true
        externalPoliticianActivities nullable: true
        relevantEvents nulable:true
        timeLine nulable:true
        politicianLeaning nullable:true
        professionalDetails nullable:true
        institutionalOffice nullable:true
        politicalOffice nullable:true
    }

    static mapping = {
        email index:true, indexAttributes: [unique:true]
//        following cascade:"refresh"
//        followers cascade:"refresh"
//        subscribers cascade:"refresh"


        writeConcern WriteConcern.FSYNC_SAFE
    }


    String toString(){
        name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof KuorumUser)) return false

        KuorumUser that = (KuorumUser) o

        if (email != that.email) return false

        return true
    }

    static transients = ["springSecurityService", 'activityForRecommendation']

//    static mapping = {
//       password column: '`password`'
//    }

    def beforeInsert() {
//        username = username?.toLowerCase()
        email = email.toLowerCase()

        if (!followers) followers = []
        numFollowers = followers.size()
    }

    def beforeUpdate() {
        log.debug("Se ha actualizado el usuario ${id}")
//        username = username?.toLowerCase()
        email = email.toLowerCase()
        alias = alias?.toLowerCase()
//        def persisted = SecUser.collection.findOne(_id:id)?.password
//        if(persisted != password)
//            encodePassword()
        if (!followers) followers = []
        numFollowers = followers.size()
    }

    int hashCode() {
        return id?id.hashCode():email.hashCode()
    }
}
