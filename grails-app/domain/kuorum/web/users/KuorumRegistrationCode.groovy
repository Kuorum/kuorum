package kuorum.web.users

class KuorumRegistrationCode {

    String username
    String token = UUID.randomUUID().toString().replaceAll('-', '')
    Date dateCreated

    String redirectLink


    static mapping = {
        collection "registrationCode" // Spring registration code collection
        version false
    }

    static constraints = {
        redirectLink nullable:true
    }
}
