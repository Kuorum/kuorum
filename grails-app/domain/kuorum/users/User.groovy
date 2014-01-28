package kuorum.users

class User {

    String name
    String email
    String username

    static constraints = {
        email nullable: false, email: true
        name nullable: true
        username nullable:true
    }
}
