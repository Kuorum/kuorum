package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.UtilException
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class CluckService {

    List<Cluck> lawClucks(Law law) {
        Cluck.findAllByLaw(law)
    }

    Cluck createCluck(Post post, KuorumUser kuorumUser){
        Cluck cluck = new Cluck(
                owner: kuorumUser,
                postOwner: post.owner,
                post: post,
                law: post.law
        )

        if (!cluck.save()){
            UtilException.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
        }
        cluck
    }

}
