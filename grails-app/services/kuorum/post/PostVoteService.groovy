package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.users.KuorumUser

@Transactional
class PostVoteService {

    def grailsApplication
    def notificationService

    PostVote votePost(Post post, KuorumUser kuorumUser){
        if (post.id == null || kuorumUser.id == null){
            throw new KuorumException("El post o el usuario no se han salvado previamente en BBDD","error.postVoteService.paramsError")
        }
        PostVote postVote = new PostVote()
        postVote.personalData = kuorumUser.personalData
        postVote.post = post
        postVote.user = kuorumUser
        if (!postVote.save())
            throw KuorumExceptionUtil.createExceptionFromValidatable(postVote,"Error salvando el post")

        //Atomic operation - non transactional
        Post.collection.update([_id:post.id],[$inc:[numVotes:1]])
        post.refresh()

        if (checkIfPublicMilestone(post)){
            notificationService.sendPublicMilestoneNotification(post)
        }else if(checkIfMilestone(post)){
            notificationService.sendMilestoneNotification(post)
        }

        postVote

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

}
