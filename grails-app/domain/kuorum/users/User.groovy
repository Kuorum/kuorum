package kuorum.users

class User {

    String name
    String email
    String username

    static constraints = {
        email nullable: false, email: true, unique: true
        name nullable: true
        username nullable:true
    }

    String toString(){
        username
    }

    boolean equals(Object object){
        name.equals(object.name)
    }
}
