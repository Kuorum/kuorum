package kuorum.notifications

import grails.transaction.Transactional
import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.PostType
import kuorum.mail.MailType
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostVote
import kuorum.users.KuorumUser
import org.springframework.context.MessageSource

@Transactional
class NotificationService {

    def kuorumMailService
    MessageSource messageSource

    void sendCluckNotification(Cluck cluck) {
        if (cluck.owner != cluck.postOwner){
            CluckNotification cluckNotification = new CluckNotification(
                    post: cluck.post,
                    kuorumUser: cluck.postOwner,
                    clucker: cluck.owner
            )
            if (cluckNotification.save()){
                kuorumMailService.sendCluckNotificationMail(cluck)
            }else{
                log.error("No se ha podido salvar una notificacion de kakareo: ${cluckNotification.errors}")
            }
        }

    }

    void sendFollowerNotification(KuorumUser follower, KuorumUser following){
        if (follower != following){
            FollowerNotification followerNotification = new FollowerNotification(
                    kuorumUser: following,
                    follower: follower
            )
            if (followerNotification.save()){
                kuorumMailService.sendFollowerNotificationMail(follower, following)
            }else{
                log.error("No se ha podido salvar una notificacion de kakareo: ${followerNotification.errors}")
            }
        }
    }

    void sendCommentNotification(KuorumUser user, Post post){
        if (user != post.owner){
            CommentNotification commentNotification = new CommentNotification(
                    kuorumUser: post.owner,
                    tertullian: user,
                    post: post
            )
            commentNotification.save()
        }
    }

    void sendMilestoneNotification(Post post){
        MilestoneNotification milestoneNotification = new MilestoneNotification(
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        milestoneNotification.save()
    }

    void sendPublicMilestoneNotification(Post post){
        PublicMilestoneNotification milestoneNotification = new PublicMilestoneNotification (
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        if (milestoneNotification.save()){
            kuorumMailService.sendPublicMilestoneNotificationMail(post)
        }else{
            log.error("No se ha podido salvar una notificacion de que un hito a alcanzado apoyos para ser público: ${milestoneNotification.errors}")
        }
    }

    /**
     * A debate is created by a politician or by the owner. For instance the corresponding notifications have been sent
     *
     * Is an asynchronous procedure
     * @param post
     */
    void sendDebateNotification(Post post){
        //An async task is not possible to test (at least I don't know how), for that this trick
        Environment.executeForCurrentEnvironment {
            development{
                grails.async.Promises.task{
                    syncSendDebateNotification(post)
                }
            }
            test{
                syncSendDebateNotification(post)
            }
            production{
                grails.async.Promises.task{
                    syncSendDebateNotification(post)
                }
            }

        }

    }

    private void syncSendDebateNotification(Post post){
        Integer idDebate = post.debates.size() -1
        sendDebateNotificationInterestedUsers(post, idDebate)
        sendDebateNotificationPoliticians(post, idDebate)
        sendDebateNotificationAuthor(post, idDebate)
    }

    private void sendDebateNotificationInterestedUsers(Post post, Integer idDebate){
        Set<MailUserData> notificationUsers = []
        KuorumUser debateOwner = post.debates.last().kuorumUser
        Boolean isFirstDebate = post.debates.size()==1

        //All interested people for their vote
        PostVote.findAllByPostAndUserNotEqual(post, post.owner).each{PostVote postVote ->
            if (postVote.user != post.owner){
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEBATE_USERS
                debateNotification.isFirstDebate=isFirstDebate
                debateNotification.post = post
                debateNotification.debateWriter = debateOwner
                debateNotification.kuorumUser = postVote.user
                debateNotification.idDebate = idDebate
                debateNotification.save()
                def bindings = [mailType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"",postVote.user.language.locale)]
                notificationUsers << new MailUserData(user:postVote.user, bindings:bindings)
            }
        }
        //All interested people for his followings
        def interestedUsers = post.debates.collect{it.kuorumUser.followers}.flatten()

        interestedUsers.each {KuorumUser user ->
            if (user != post.owner && !(DebateNotification.findByPostAndKuorumUserAndIdDebate(post,user,idDebate))){
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEBATE_USERS
                debateNotification.post = post
                debateNotification.debateWriter = debateOwner
                debateNotification.kuorumUser = user
                debateNotification.idDebate = idDebate
                debateNotification.save()
                def bindings = [mailType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"",user.language.locale)]
                notificationUsers << new MailUserData(user:user, bindings:bindings)
            }
        }

        kuorumMailService.sendDebateNotificationMailInterestedUsers(post, notificationUsers)
    }

    private sendDebateNotificationPoliticians(Post post, Integer idDebate){
        Set<KuorumUser> politicians = post.debates.collect{it.kuorumUser} as Set<KuorumUser>
        KuorumUser debateOwner = post.debates.last().kuorumUser
        politicians = politicians.minus(debateOwner).minus(post.owner)
        if (politicians){
            Set<MailUserData> politicianUsers = politicians.collect{user ->
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEBATE_POLITICIAN
                debateNotification.post = post
                debateNotification.debateWriter = debateOwner
                debateNotification.kuorumUser = user
                debateNotification.idDebate =  idDebate
                debateNotification.save()
                def bindings = [mailType:messageSource.getMessage("${PostType.canonicalName}.${post.postType}",null,"", user.language.locale)]
                new MailUserData(user:user, bindings:bindings)
            }
            kuorumMailService.sendDebateNotificationMailPolitician(post, politicianUsers)
        }
    }

    private sendDebateNotificationAuthor(Post post, Integer idDebate){
        if (post.debates.last().kuorumUser != post.owner){
            KuorumUser debateOwner = post.debates.last().kuorumUser
            DebateNotification debateNotification
            if (post.debates.size() == 1){
                debateNotification = new DebateAlertNotification()
            }else{
                debateNotification = new DebateNotification()
            }
            debateNotification.mailType = MailType.NOTIFICATION_DEBATE_AUTHOR
            debateNotification.post = post
            debateNotification.debateWriter = debateOwner
            debateNotification.kuorumUser = post.owner
            debateNotification.idDebate = idDebate
            debateNotification.save()
            kuorumMailService.sendDebateNotificationMailAuthor(post)
        }
    }

    /**
    * A post has been supported by a politician
    *
    * Is an asynchronous procedure
    * @param post
    */
    void sendPostSupportedNotification(Post post){
        //An async task is not possible to test (at least I don't know how), for that this trick
        Environment.executeForCurrentEnvironment {
            development{
                grails.async.Promises.task{
                    syncPostSupportedNotification(post)
                }
            }
            test{
                syncPostSupportedNotification(post)
            }
            production{
                grails.async.Promises.task{
                    syncPostSupportedNotification(post)
                }
            }

        }
    }

    void syncPostSupportedNotification(Post post){
        if (!post.supportedBy){
            throw new KuorumException("El post $post debe de estar apadrinado para poder enviar la notificacion", "")
        }

    }
}
