package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.Pagination
import kuorum.users.KuorumUser

@Transactional
class PostVoteService {

    def grailsApplication
    def notificationService
    def gamificationService

    PostVote votePost(Post post, KuorumUser kuorumUser, Boolean anonymous = Boolean.FALSE){
        if (post.id == null || kuorumUser.id == null){
            throw new KuorumException("El post o el usuario no se han salvado previamente en BBDD","error.postVoteService.paramsError")
        }
        PostVote postVote = PostVote.findByPostAndUser(post,kuorumUser)
        if (postVote){
            throw new KuorumException("El usuario ${kuorumUser} ya habia votado","error.postVoteService.userAleradyVote")
        }
        postVote = new PostVote()
        postVote.personalData = kuorumUser.personalData
        postVote.post = post
        postVote.user = kuorumUser
        postVote.anonymous = anonymous
        if (!postVote.save())
            throw KuorumExceptionUtil.createExceptionFromValidatable(postVote,"Error salvando el post")

        //Atomic operation - non transactional
        post.save(flush:true)
        Post.collection.update([_id:post.id],[$inc:[numVotes:1]])
        post.refresh()

        if (checkIfPublicMilestone(post)){
            notificationService.sendPublicMilestoneNotification(post)
            gamificationService.postCreatedAward(post.owner, post)
        }else if(checkIfMilestone(post)){
            notificationService.sendMilestoneNotification(post)
        }
        gamificationService.postVotedAward(kuorumUser, post)
        postVote

    }

    boolean isAllowedToVote(Post post, KuorumUser user){
        PostVote.countByPostAndUser(post,user) == 0
    }

    boolean checkIfMilestone(Post post){

        Range<Long> rangePost = findPostRange(post)
        grailsApplication.config.kuorum.milestones.postVotes.publicVotes != post.numVotes &&
        rangePost.from == post.numVotes
    }

    boolean checkIfPublicMilestone(Post post){
        grailsApplication.config.kuorum.milestones.postVotes.publicVotes == post.numVotes
    }

    Range<Long> findPostRange(Post post){
        //Filling ranges because is lazyList and could be empty or with not enough elements
        int i = 10
        while (grailsApplication.config.kuorum.milestones.postVotes.ranges.last().to < post.numVotes){
            grailsApplication.config.kuorum.milestones.postVotes.ranges[i]
            i++
        }
        grailsApplication.config.kuorum.milestones.postVotes.ranges.find{it.contains(post.numVotes)}
    }

    List<KuorumUser> findVotedUsers(Post post, Pagination pagination = new Pagination()){
        PostVote.findAllByPostAndAnonymous(post, Boolean.FALSE,[max: pagination.max, sort: "id", order: "desc", offset: 0]).collect{it.user}
    }

}
