package kuorum.notifications

import grails.transaction.Transactional
import grails.util.Environment
import kuorum.OfferPurchased
import kuorum.campaign.PollCampaignVote
import kuorum.core.exception.KuorumException
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.VoteType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchNotifications
import kuorum.project.Project
import kuorum.mail.MailType
import kuorum.mail.MailUserData
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.post.PostVote
import kuorum.project.ProjectVote
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

@Transactional
class NotificationService {

    def kuorumMailService

    private static Integer BUFFER_NOTIFICATIONS_SIZE = 1000

    /**
     * Returns notifications, including alerts that the user hasn't see.
     * @param user
     * @return
     */
    List<Notification> findUserNotificationsNotChecked(KuorumUser user, Pagination pagination = new Pagination()){
        Notification.findAllByKuorumUserAndDateCreatedGreaterThan(user, user.lastNotificationChecked,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    /**
     * Returns all notifications
     * @param user
     * @return
     */
    List<Notification> findUserNotifications(SearchNotifications searchNotifications){
        def criteria = Notification.createCriteria()
        def result = criteria.list(max:searchNotifications.max, offset:searchNotifications.offset) {
            eq('kuorumUser', searchNotifications.user)
            if (searchNotifications.alerts==Boolean.TRUE) eq('isAlert', true)
            if (searchNotifications.alerts==Boolean.FALSE) eq('isAlert', false)
            order("dateCreated","desc")
        }
        result
//        Notification.findAllByKuorumUser(user,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    /**
     * Marks all the notifications as checked by the user
     * @param user
     */
    KuorumUser markUserNotificationsAsChecked(KuorumUser user){
        user.lastNotificationChecked = new Date()
        user.save()
    }
    /**
     * Returns active alerts
     * @param user
     * @return
     */
    List<Notification> findActiveUserAlerts(KuorumUser user, Pagination pagination = new Pagination()){
        Notification.findAllByKuorumUserAndIsAlertAndIsPostponed(user, Boolean.TRUE, Boolean.FALSE,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    /**
     * Returns num active alerts
     * @param user
     * @return
     */
    Integer countActiveUserAlerts(KuorumUser user){
        Notification.countByKuorumUserAndIsAlertAndIsActive(user, Boolean.TRUE, Boolean.TRUE)
    }

    /**
     * Marks a notification as postponed if the finished is false. And mark the notification as finished and deactiverd if
     * finished is true
     * @param user
     * @param notification
     * @param finished
     * @return
     */
    Notification markAsInactive(KuorumUser user, Notification notification, Boolean finished){
        if (notification.kuorumUser != user){
            throw new KuorumException("Usuario ${user} no esta autorizado para modificar la notificacion ${notification.id}")
        }
        if (isAlertNotification(notification)){
            // I don't know why is not saving on mongo with .save()
            Notification.collection.update([_id:notification.id],[$set:[isPostponed:true]])
            if (finished){
                Notification.collection.update([_id:notification.id],[$set:[isActive:false]])
            }
            notification.refresh();
        }else{
            log.warn("Se ha tratado de desactivar una notificación que no es alerta")
        }
        notification
    }

    boolean isAlertNotification(Notification notification){
        notification.hasProperty("isActive") && notification.hasProperty("isAlert") && notification.isAlert
    }


    /**
     * Returns all user alerts
     * @param user
     * @return
     */
    List<Notification> findUserAlerts(KuorumUser user, Pagination pagination = new Pagination()){
        Notification.findAllByKuorumUserAndIsAlert(user, Boolean.TRUE,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    void sendCommentVoteNotification(Post post,  PostComment postComment,VoteType voteType, KuorumUser votingUser){
        if (votingUser != postComment.kuorumUser && voteType.equals(VoteType.POSITIVE)){
            CommentVotedNotification commentVotedNotification = new CommentVotedNotification()
            commentVotedNotification.setVotingUser(votingUser)
            commentVotedNotification.setVoteType(voteType)
            commentVotedNotification.setPost(post)
            commentVotedNotification.setCommentWriter(postComment.getKuorumUser())
            commentVotedNotification.setText(postComment.getText())
            commentVotedNotification.setKuorumUser(postComment.getKuorumUser())
//            commentVotedNotification.setIsActive(Boolean.TRUE)
            commentVotedNotification.save(flush:true)
        }
    }

    void sendCluckNotification(Cluck cluck) {
        if (cluck.owner != cluck.postOwner){
            CluckNotification cluckNotification = new CluckNotification(
                    post: cluck.post,
                    kuorumUser: cluck.postOwner,
                    clucker: cluck.owner
            )
            if (cluckNotification.save(flush:true)){
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
            if (followerNotification.save(flush:true)){
                kuorumMailService.sendFollowerNotificationMail(follower, following)
            }else{
                log.error("No se ha podido salvar una notificacion de kakareo: ${followerNotification.errors}")
            }
        }
    }

    public void sendPollCampaignNotification(PollCampaignVote pollCampaign){
        kuorumMailService.sendPollCampaignMail(pollCampaign)
    }

    public void sendPoliticianContactNotification(KuorumUser politician, KuorumUser user, String message, String cause){
        kuorumMailService.sendPoliticianContact(politician, user, message, cause)
    }

    void sendCommentNotifications(Post post, PostComment comment){
        sendCommentNotificationsOwner(post, comment)
        sendCommentNotificationsUsers(post, comment)
    }
    private void sendCommentNotificationsOwner(Post post, PostComment comment){
        if (post.owner != comment.kuorumUser){
            CommentMyPostNotification commentNotification = new CommentMyPostNotification(
                    kuorumUser: post.owner,
                    commentWriter: comment.kuorumUser,
                    post: post,
                    text: comment.text
            )
            commentNotification.save(flush:true)
            kuorumMailService.sendCommentedPostNotificationOwner(post, comment)
        }
    }
    private void sendCommentNotificationsUsers(Post post, PostComment comment){
        List<KuorumUser> users = post.comments*.kuorumUser
        KuorumUser commentWriter = comment.kuorumUser
        users = users.unique()
        users.remove(post.owner)
        users.remove(commentWriter)
        Set<MailUserData> notificationUsers = []
        users.each {KuorumUser user ->
            CommentGenericNotification commentNotification = new CommentGenericNotification(
                    kuorumUser: user,
                    commentWriter: commentWriter,
                    post: post,
                    text: comment.text
            )
            commentNotification.save(flush:true)
            notificationUsers << new MailUserData(user:user, bindings:[:])
            if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                kuorumMailService.sendCommentedPostNotificationUsers(post, comment, notificationUsers)
                notificationUsers.clear()
            }
        }
        if (!notificationUsers.isEmpty()){
            kuorumMailService.sendCommentedPostNotificationUsers(post, comment, notificationUsers)
            notificationUsers.clear()
        }
    }

    void sendMilestoneNotification(Post post){
        MilestoneNotification milestoneNotification = new MilestoneNotification(
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        milestoneNotification.save(flush:true)
    }

    void sendPublicMilestoneNotification(Post post){
        PublicMilestoneNotification milestoneNotification = new PublicMilestoneNotification (
                kuorumUser: post.owner,
                post: post,
                numVotes: post.numVotes
        )
        if (milestoneNotification.save(flush:true)){
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
        try{
            sendDebateNotificationInterestedUsers(post, idDebate)
        }catch (Exception e){
            log.error("Ha habido algun error notificando a los usuarios de que hay un nuevo debate en el post $post",e)
        }
        try{
            sendDebateNotificationPoliticians(post, idDebate)
        }catch (Exception e){
            log.error("Ha habido algun error notificando a los políticos de que hay un nuevo debate en el post $post",e)
        }
        try{
            sendDebateNotificationAuthor(post, idDebate)
        }catch (Exception e){
            log.error("Ha habido algun error notificando al autor de que hay un nuevo debate en el post $post",e)
        }
        deactivateDebateUserAlertNotification(post,idDebate)
    }

    private void deactivateDebateUserAlertNotification(Post post, Integer idDebate){
        if (post.debates.last().kuorumUser == post.owner){
            DebateAlertNotification alert = DebateAlertNotification.findByKuorumUserAndPostAndIsActive(post.owner,post, true)
            if (alert){
                alert.isActive = false
                alert.save(flush:true)
                alert.insert()// SAVING IS NOT WORWING!!!!
            }
        }
    }

    private void sendDebateNotificationInterestedUsers(Post post, Integer idDebate){
//        PostVote.withStatelessSession{}
        // IF THIS METHOD CONSUME A LOT OF MEMORY TRY WITH withStatelessSession. It dosn't work on unit testing mode
        Set<MailUserData> notificationUsers = []
        KuorumUser debateOwner = post.debates.last().kuorumUser
        Boolean isFirstDebate = post.debates.size()==1

        //All interested people for their vote

        List<KuorumUser> notGenericMail = post.debates.collect{it.kuorumUser}
        notGenericMail.add(post.owner)
        notGenericMail = notGenericMail.unique()

//        PostVote.findAllByPostAndUserNotInList(post, notGenericMail).each{PostVote postVote -> //No funciona el dynamicFinder NOT_IN_LIST. Lo transforma a IN_LIST
        PostVote.findAllByPost(post).each{PostVote postVote ->
            if (!notGenericMail.contains(postVote.user)){
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEBATE_USERS
                debateNotification.isFirstDebate=isFirstDebate
                debateNotification.post = post
                debateNotification.debateWriter = debateOwner
                debateNotification.kuorumUser = postVote.user
                debateNotification.idDebate = idDebate
                if (!debateNotification.save(flush: true)){
                    log.warn("No se ha salvado la notificación de debate para ${postVote.user} del post $post que se notificaba por haber impulsado el post")
                }
                notificationUsers << new MailUserData(user:postVote.user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendDebateNotificationMailInterestedUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }
        //All interested people for his followings
        def interestedUsers = post.debates.collect{it.kuorumUser.followers}.flatten().unique()
        interestedUsers = interestedUsers.minus(notGenericMail.collect{it.id})

        interestedUsers.each {ObjectId userId ->
            KuorumUser user = KuorumUser.load(userId)
            DebateNotification alreadyNotified = DebateNotification.findByPostAndKuorumUserAndIdDebate(post,user,idDebate)
            if (!alreadyNotified){
                DebateNotification debateNotification = new DebateNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEBATE_USERS
                debateNotification.post = post
                debateNotification.debateWriter = debateOwner
                debateNotification.kuorumUser = user
                debateNotification.idDebate = idDebate
                //EL save no funciona sin flush ¬¬
                if (!debateNotification.save(flush: true)){
                    log.warn("No se ha salvado la notificación de debate para ${user} del post $post que se notificaba por seguir a algun politico o al usuario")
                }
                notificationUsers << new MailUserData(user:user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendDebateNotificationMailInterestedUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }

        if (notificationUsers)
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
                new MailUserData(user:user, bindings:[:])
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
            debateNotification.save(flush:true, failOnError: true)
            kuorumMailService.sendDebateNotificationMailAuthor(post)
        }
    }

    /**
    * A post has been supported by a politician
    *
    * Is an asynchronous procedure
    * @param post
    */
    void sendPostDefendedNotification(Post post){
        //An async task is not possible to test (at least I don't know how), for that this trick
        Environment.executeForCurrentEnvironment {
            development{
                grails.async.Promises.task{
                    syncPostDefendedNotification(post)
                }
            }
            test{
                syncPostDefendedNotification(post)
            }
            production{
                grails.async.Promises.task{
                    syncPostDefendedNotification(post)
                }
            }

        }
    }

    void syncPostDefendedNotification(Post post){
        if (!post.defender){
            throw new KuorumException("El post $post debe de estar apadrinado para poder enviar la notificacion", "")
        }
        sendPostDefendedNotificationAuthor(post)
        sendPostDefendedNotificationDefender(post)
        sendPostDefendedNotificationPoliticians(post)
        sendPostDefendedNotificationInterestedUsers(post)

    }

    /**
     * Prepare notification for the owner of the post
     * @param post
     */
    private void sendPostDefendedNotificationAuthor(Post post){
        DefendedPostAlert defendedNotification = new DefendedPostAlert()
        defendedNotification.mailType = MailType.NOTIFICATION_DEFENDED_POLITICIANS
        defendedNotification.post = post
        defendedNotification.kuorumUser = post.owner
        defendedNotification.defender = post.defender
        defendedNotification.save(flush:true)
        kuorumMailService.sendPostDefendedNotificationMailAuthor(post)

    }

    /**
     * Send a notification for the rest of the politicians who has written on the debate
     * @param post
     */
    private void sendPostDefendedNotificationPoliticians(Post post){
        Set<KuorumUser> politicians = post.debates.collect{it.kuorumUser} as Set<KuorumUser>
        politicians = politicians.minus(post.defender).minus(post.owner)
        if (politicians){
            Set<MailUserData> politicianUsers = politicians.collect{user ->
                DefendedPostNotification defendedNotification = new DefendedPostNotification()
                defendedNotification.mailType = MailType.NOTIFICATION_DEFENDED_POLITICIANS
                defendedNotification.post = post
                defendedNotification.kuorumUser = user
                defendedNotification.defender = post.defender
                defendedNotification.save(flush:true)
                new MailUserData(user:user, bindings:[:])
            }
            kuorumMailService.sendPostDefendedNotificationMailPoliticians(post, politicianUsers)
        }
    }

    /**
     * Prepare notification for the politician who has defended the post
     * @param post
     */
    private void sendPostDefendedNotificationDefender(Post post){
        kuorumMailService.sendPostDefendedNotificationMailDefender(post)
    }

    /**
     * Prepare notification for the people interested in this post:
     * - followers of the politician
     * - people who has voted the post
     *
     * @param post
     */
    private void sendPostDefendedNotificationInterestedUsers(Post post){
        Set<MailUserData> notificationUsers = []

        List<KuorumUser> notGenericMail = post.debates.collect{it.kuorumUser}
        notGenericMail.add(post.owner)
        notGenericMail.add(post.defender)
        notGenericMail = notGenericMail.unique()

        //All interested people for their vote
        PostVote.findAllByPostAndUserNotInList(post, notGenericMail).each{PostVote postVote ->
            if (postVote.user != post.owner){
                DefendedPostNotification debateNotification = new DefendedPostNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEFENDED_USERS
                debateNotification.post = post
                debateNotification.kuorumUser = postVote.user
                debateNotification.defender = post.defender
                debateNotification.save(flush:true)
                notificationUsers << new MailUserData(user:postVote.user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }

        def interestedUsers = (post.defender.followers + post.owner.followers).unique().minus(notGenericMail*.id)
        interestedUsers.each {ObjectId userId ->
            KuorumUser user = KuorumUser.load(userId)
            DefendedPostNotification alreadyNotified = DefendedPostNotification.findByPostAndKuorumUser(post,user)
            if (!alreadyNotified){
                DefendedPostNotification debateNotification = new DefendedPostNotification()
                debateNotification.mailType = MailType.NOTIFICATION_DEFENDED_USERS
                debateNotification.post = post
                debateNotification.kuorumUser = user
                debateNotification.defender = post.defender
                debateNotification.save(flush:true)
                notificationUsers << new MailUserData(user:user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }

        if (notificationUsers)
            kuorumMailService.sendPostDefendedNotificationMailInterestedUsers(post, notificationUsers)
    }


    void sendVictoryNotification(Post post){
        Notification notification = DefendedPostAlert.findByPostAndKuorumUser(post,post.owner);
        markAsInactive(post.owner, notification, Boolean.TRUE);
        //An async task is not possible to test (at least I don't know how), for that this trick
        Environment.executeForCurrentEnvironment {
            development{
                grails.async.Promises.task{
                    syncSendVictoryNotification(post)
                }
            }
            test{
                syncSendVictoryNotification(post)
            }
            production{
                grails.async.Promises.task{
                    syncSendVictoryNotification(post)
                }
            }

        }
    }

    private void syncSendVictoryNotification(Post post){
        Set<MailUserData> notificationUsers = []

        // Dfender notification
        VictoryNotification victoryDefenderNotification = new VictoryNotification()
        victoryDefenderNotification.post = post
        victoryDefenderNotification.mailType = MailType.NOTIFICATION_VICTORY_DEFENDER
        victoryDefenderNotification.kuorumUser = post.defender
        victoryDefenderNotification.politician=post.defender
        victoryDefenderNotification.save(flush:true)
        kuorumMailService.sendVictoryNotificationDefender(post)

        //All interested people for their vote
        PostVote.findAllByPostAndUserNotEqual(post, post.owner).each{PostVote postVote ->
            if (postVote.user != post.owner){
                VictoryNotification victoryNotification = new VictoryNotification()
                victoryNotification.post = post
                victoryNotification.kuorumUser = postVote.user
                victoryNotification.politician=post.defender
                victoryNotification.save(flush:true)
                notificationUsers << new MailUserData(user:postVote.user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendVictoryNotificationUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }
        //All interested people for his followings (USER WITH EMAIL)
        def interestedUsers = post.owner.followers.minus(post.defender.id)
        interestedUsers.each {ObjectId userId->
            KuorumUser user = KuorumUser.load(userId)
            VictoryNotification alreadyNotified = VictoryNotification.findByPostAndKuorumUser(post,user)
            if (!alreadyNotified){
                VictoryNotification victoryNotification = new VictoryNotification()
                victoryNotification.mailType = MailType.NOTIFICATION_DEFENDED_USERS
                victoryNotification.post = post
                victoryNotification.kuorumUser = user
                victoryNotification.politician=post.defender
                victoryNotification.save(flush:true)
                notificationUsers << new MailUserData(user:user, bindings:[:])
                if (notificationUsers.size() >= BUFFER_NOTIFICATIONS_SIZE){
                    kuorumMailService.sendVictoryNotificationUsers(post, notificationUsers)
                    notificationUsers.clear()
                }
            }
        }

        //All interested people for his followings (DEFENDER - NO MAIL)
        interestedUsers = post.defender.followers.minus(post.owner.id)
        interestedUsers.each {ObjectId userId->
            KuorumUser user = KuorumUser.load(userId)
            VictoryNotification alreadyNotified = VictoryNotification.findByPostAndKuorumUser(post,user)
            if (!alreadyNotified){
                VictoryNotification victoryNotification = new VictoryNotification()
                victoryNotification.mailType = MailType.NOTIFICATION_DEFENDED_USERS
                victoryNotification.post = post
                victoryNotification.kuorumUser = user
                victoryNotification.politician=post.defender
                victoryNotification.save(flush:true)
            }
        }

        if (notificationUsers)
            kuorumMailService.sendVictoryNotificationUsers(post, notificationUsers)
    }


    List searchRelatedUserToProject(Project project){
        KuorumUser.createCriteria().list(){
            and{
                personalData{
                    like('provinceCode',project.region?.iso3166_2 + '%')
                }
                or{
                    project.commissions.each{commision->
                        inList("relevantCommissions", commision)
                    }
                }
            }
        }
    }

    void sendProjectPublishNotification(Project project){
        List <KuorumUser> relatedUsers = searchRelatedUserToProject(project)
        Thread.start {
            kuorumMailService.sendSavedProjectToRelatedUsers(relatedUsers,project)
        }
    }

    void sendProjectClosedNotification(Project project){
        Environment.executeForCurrentEnvironment {
            development{
                grails.async.Promises.task{
                    syncSendProjectClosedNotification(project)
                }
            }
            test{
                syncSendProjectClosedNotification(project)
            }
            production{
                grails.async.Promises.task{
                    syncSendProjectClosedNotification(project)
                }
            }
        }
    }

    void syncSendProjectClosedNotification(Project project){

        if (project.status == ProjectStatusType.OPEN){
            throw new KuorumException("La ley debe de estar cerrada para que se notifique su cierre","error.project.notClosed")
        }

        ProjectVote.findAllByProject(project).each{ProjectVote projectVote->
            ProjectClosedNotification projectClosedNotification = new ProjectClosedNotification()
            projectClosedNotification.project = project
            projectClosedNotification.kuorumUser = projectVote.kuorumUser
            projectClosedNotification.save(flush:true)
        }

    }

    void sendOfferPurchasedNotification(KuorumUser user, OfferPurchased offerPurchased){
        kuorumMailService.sendPoliticianSubscription(user,offerPurchased.offerType)
        kuorumMailService.sendPoliticianSubscriptionToAdmins(user,offerPurchased.offerType)
    }
}
