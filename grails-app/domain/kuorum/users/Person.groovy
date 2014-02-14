package kuorum.users

class Person extends KuorumUser{

    Date birthday

    static constraints = {
        birthday nullable:true
    }
}
