package kuorum.users

import com.mongodb.WriteConcern
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.ParliamentaryGroup
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.MailType
import kuorum.post.Post
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
    String bio
//    String username
    String password
    AvailableLanguage language = AvailableLanguage.es_ES

    KuorumFile avatar

    PersonalData personalData = new PersonData()
    UserType userType = UserType.PERSON

    List<CommissionType> relevantCommissions = []
    List<KuorumUser> following  = []
    List<KuorumUser> followers = []
    List<KuorumUser> subscribers = []

    Integer numFollowers = 0

    List<ObjectId> favorites = []

    List<MailType> availableMails = MailType.values()

    Gamification gamification = new Gamification()

    static hasMany = [following:KuorumUser,followers:KuorumUser]
    static embedded = ['personalData', 'authorities','gamification','avatar']

    /**
     * Represents the last time that the user checked the notifications
     */
    Date lastNotificationChecked = new Date()

    //Politician FIELDS
    ParliamentaryGroup parliamentaryGroup
    Institution institution

    //Spring fields
    transient springSecurityService

    boolean enabled = Boolean.TRUE
    boolean accountExpired = Boolean.FALSE
    boolean accountLocked = Boolean.FALSE
    boolean passwordExpired = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    static constraints = {
        email nullable: false, email: true
        password nullable:false, blank: false
        bio nullable:true
        avatar nullable:true
        userType nullable: false, validator:{val, obj ->
            obj.personalData.userType == val
        }

        //POLITICIAN VALIDATION
        parliamentaryGroup nullable: true, validator: { val, obj ->
            if (val  && val.institution != obj.institution) {
                return ['notCorrectInstitution']
            }
            if (!val && obj.institution){
                return ['notParliamentaryGroup']
            }
        }

        institution nullable:true
    }

    static mapping = {
        email index:true, indexAttributes: [unique:true]
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

    static transients = ["springSecurityService"]

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
