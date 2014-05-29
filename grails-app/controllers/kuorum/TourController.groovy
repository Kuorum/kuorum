package kuorum

import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.users.KuorumUser

class TourController {

    def tour1() {
        KuorumUser user = KuorumUser.list(max:1, sort:'id', order:'asc').first()
        user.name = "Nombre Usuario"
        List<Post> posts = Post.findAllByOwnerNotEqual(user,[max:5, sort:'id', order:'asc'])
        List<Cluck> fakeClucks = posts.collect{post ->
            Cluck cluckFake = new Cluck(
                    owner:user,
                    postOwner:post.owner,
                    defendedBy:null,
                    sponsors:[],
                    debateMembers:[],
                    isFirstCluck:Boolean.FALSE,
                    law:post.law,
                    post:post,
                    dateCreated:post.dateCreated,
                    lastUpdated:post.dateCreated
            )
            cluckFake.dateCreated = post.dateCreated
            cluckFake.post = post
            cluckFake.law = post.law
            cluckFake
        }
        [fakeClucks:fakeClucks, user:user]
    }
    def tour2() {

    }

    def tour3() {

    }
}
