package kuorum.users

import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import org.bson.types.ObjectId

class KuorumUser {


    ObjectId id
    String name
    String email
    String username
    String password
    AvailableLanguage language = AvailableLanguage.es_ES

    List<CommissionType> relevantCommissions = []

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
        name nullable: true
        username nullable:true
        password nullable:false, blank: false
    }

    static mapping = {
        email index:true, indexAttributes: [unique:true]
    }

    String toString(){
        username?:email
    }

    boolean equals(Object object){
        email && email.equals(object?.email)
    }




    static transients = ["springSecurityService"]
    static embedded = ['authorities']

//    static mapping = {
//       password column: '`password`'
//    }

    def beforeInsert() {
        username = username?.toLowerCase()
        email = email.toLowerCase()
    }

    def beforeUpdate() {
        log.debug("Se ha actualizado el usuario ${id}")
        username = username?.toLowerCase()
        email = email.toLowerCase()
//        def persisted = SecUser.collection.findOne(_id:id)?.password
//        if(persisted != password)
//            encodePassword()
    }
}
