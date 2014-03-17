package kuorum.users

import com.mongodb.WriteConcern
import kuorum.Institution
import kuorum.ParliamentaryGroup
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.MailType
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

    PersonalData personalData = new PersonalData()
    UserType userType = UserType.PERSON

    List<CommissionType> relevantCommissions = []
    List<KuorumUser> following  = []
    List<KuorumUser> followers = []
    List<KuorumUser> subscribers = []

    List<MailType> availableMails = MailType.values()

    static hasMany = [following:KuorumUser,followers:KuorumUser]
    static embedded = ['personalData', 'authorities']


    //Politician FIELDS
    ParliamentaryGroup parliamentaryGroup
    Institution institution

    //Spring fields
    transient springSecurityService

    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    static constraints = {
        email nullable: false, email: true
        password nullable:false, blank: false
        bio nullable:true
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

    boolean equals(Object object){
        email && email.equals(object?.email)
    }




    static transients = ["springSecurityService"]

//    static mapping = {
//       password column: '`password`'
//    }

    def beforeInsert() {
//        username = username?.toLowerCase()
        email = email.toLowerCase()
    }

    def beforeUpdate() {
        log.debug("Se ha actualizado el usuario ${id}")
//        username = username?.toLowerCase()
        email = email.toLowerCase()
//        def persisted = SecUser.collection.findOne(_id:id)?.password
//        if(persisted != password)
//            encodePassword()
    }

    int hashCode() {
        return id?id.hashCode():email.hashCode()
    }
}
