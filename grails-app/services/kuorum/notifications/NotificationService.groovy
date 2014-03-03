package kuorum.notifications

import grails.transaction.Transactional
import grails.util.Environment
import kuorum.mail.MailType
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostVote
import kuorum.users.KuorumUser

@Transactional
class NotificationService {

    def kuorumMailService

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
        //mailUserDatas has all the people interested on the debate
        Set<MailUserData> notificationUsers = []
        Set<KuorumUser> debateUsers = post.debates.collect{it.kuorumUser}
        KuorumUser politician = post.debates.last().kuorumUser
        Boolean isFirstDebate = post.debates.size()==1

        //All interested people for his vote
        PostVote.findAllByPostAndUserNotEqual(post, post.owner).each{PostVote postVote ->
            DebateNotification debateNotification = new DebateNotification()
            debateNotification.mailType = isFirstDebate?MailType.NOTIFICATION_FIRST_DEBATE:MailType.NOTIFICATION_MORE_DEBATE
            debateNotification.isFirstDebate=isFirstDebate
            debateNotification.post = post
            debateNotification.politician = politician
            debateNotification.kuorumUser = postVote.user
            debateNotification.save()
            notificationUsers << new MailUserData(user:postVote.user, bindings:[:])
        }
        //All interested people for his followings
        def interestedUsers = post.debates.collect{it.kuorumUser.followers}.flatten()
        def interestedUsersWithoutAlertedPeople = interestedUsers.minus(debateUsers)
        interestedUsersWithoutAlertedPeople.each {KuorumUser user ->
            if (!(notificationUsers.find{it.user==user})){
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = isFirstDebate?MailType.NOTIFICATION_FIRST_DEBATE:MailType.NOTIFICATION_MORE_DEBATE
                debateNotification.post = post
                debateNotification.politician = politician
                debateNotification.kuorumUser = user
                debateNotification.save()
                notificationUsers << new MailUserData(user:user, bindings:[:])
            }
        }

        Set<KuorumUser> alertUsers = post.debates.collect{it.kuorumUser} as Set<KuorumUser>
        alertUsers.add(post.owner)
        alertUsers = alertUsers.minus(post.debates.last().kuorumUser)

        def alertMailUsers = [] as Set<MailUserData>
        alertUsers.each { user ->
            DebateAlertNotification debateAlert = new DebateAlertNotification()
            debateAlert.mailType = isFirstDebate?MailType.ALERT_FIRST_DEBATE:MailType.ALERT_MORE_DEBATE
            debateAlert.post = post
            debateAlert.politician = politician
            debateAlert.kuorumUser = user
            debateAlert.save()
            alertMailUsers << new MailUserData(user:user, bindings:[:])
        }

        kuorumMailService.sendDebateNotificationMail(post, notificationUsers,alertMailUsers, isFirstDebate)
    }
}
