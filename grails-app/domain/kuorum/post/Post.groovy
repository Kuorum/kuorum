package kuorum.post

import kuorum.law.Law
import kuorum.users.KuorumUser

class Post {

    KuorumUser owner
    Law law
    String title
    String text
    String photo
    Integer numVotes

    static constraints = {
        numVotes min:0
    }
}
