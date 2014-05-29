package kuorum

import kuorum.core.model.LawStatusType
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.users.KuorumUser

class TourController {

    def springSecurityService
    def postService
    def cluckService
    def lawService
    def postVoteService

    def beforeInterceptor ={
        KuorumUser user
        if (springSecurityService.isLoggedIn()){
            user = KuorumUser.get(springSecurityService.principal.id)
        }else{
            user = KuorumUser.list(max:1, sort:'id', order:'asc').first()
            user.name = "Nombre Usuario"
            user.avatar = null

        }
        params.user = user
    }

    def tour1() {
        KuorumUser user = params.user
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
        KuorumUser user = params.user
        Law law = Law.findByStatus(LawStatusType.OPEN)
        List<Post> victories = postService.lawVictories(law)
        def clucks = cluckService.lawClucks(law,new Pagination(max:4))
        Integer necessaryVotesForKuorum = lawService.necessaryVotesForKuorum(law)
        [user:user, law:law,victories:victories,clucks:clucks,necessaryVotesForKuorum:necessaryVotesForKuorum]
    }

    def tour3() {
        KuorumUser user = params.user
        Post post = Post.findByDefenderIsNull()
        List<Post> relatedPost = postService.relatedPosts(post,  user,  3 )
        List<KuorumUser> usersVotes = postVoteService.findVotedUsers(post, new Pagination(max:20))
        [user:user, post:post,relatedPost:relatedPost,usersVotes:usersVotes]
    }
}
