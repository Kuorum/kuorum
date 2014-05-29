package kuorum

import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.users.KuorumUser

class TourController {

    def springSecurityService

    def tour1() {
        KuorumUser user
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }else{
            user = KuorumUser.list(max:1, sort:'id', order:'asc').first()
            user.name = "Nombre Usuario"
            user.avatar = null

        }
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
        def recommendedUsers = posts.owner
        [fakeClucks:fakeClucks, user:user, favorites:posts,recommendedUsers:recommendedUsers]
    }
    def tour2() {

    }

    def tour3() {

    }
}
