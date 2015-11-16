package kuorum.users

import com.mongodb.*
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovyx.gpars.GParsPool
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrElement
import kuorum.core.model.solr.SolrResults
import kuorum.core.model.solr.SolrType
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.post.PostComment
import kuorum.project.Project
import kuorum.register.RegisterService
import kuorum.solr.SearchSolrService
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    def kuorumMailService
    def regionService
    SpringSecurityService springSecurityService
    SearchSolrService searchSolrService;

    GrailsApplication grailsApplication

    def createFollower(KuorumUser follower, KuorumUser following) {
        if (follower == following){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (follower.following.contains(following.id)){
            log.warn("Se ha intentado seguir a un usuario que ya exisitía")
        }else{
            KuorumUser.collection.update([_id:follower.id],['$addToSet':[following:following.id]])
            KuorumUser.collection.update([_id:following.id],['$addToSet':[followers:follower.id]])
            follower.refresh()
            following.refresh()
            following.numFollowers = following.followers.size()
            following.save(flush: true)
    //        follower.following.add(following.id)
    //        following.followers.add(follower.id)
    //        follower.save()
    //        following.save()
            notificationService.sendFollowerNotification(follower, following)
        }
    }

    def deleteFollower(KuorumUser follower, KuorumUser following) {
        if (follower == following){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (!follower.following.contains(following.id)){
            log.warn("Se ha intentado eliminar un follower que no existia")
        }else{
            KuorumUser.collection.update([_id:follower.id],['$pull':[following:following.id]])
            KuorumUser.collection.update([_id:following.id],['$pull':[followers:follower.id]])
            follower.refresh()
            following.refresh()
            following.numFollowers = following.followers.size()
            following.save(flush: true)
        }
    }

    List<KuorumUser> findFollowers(KuorumUser user, Pagination pagination){
        List<KuorumUser> followers =[]
        def init = pagination.offset
        def end = Math.min(pagination.max+pagination.offset, user.followers.size()-1)
        if (init <= end){
            (init .. end).each{
                followers.add(KuorumUser.get(user.followers[(Integer)it]))
            }
        }
        followers
    }

    List<KuorumUser> findFollowing(KuorumUser user, Pagination pagination){
        List<KuorumUser> following =[]
        def init = pagination.offset
        def end = Math.min(pagination.max+pagination.offset, user.following.size()-1)
        if (init <= end){
            (init .. end).each{
                following.add(KuorumUser.get(user.following[(Integer)it]))
            }
        }
        following
    }

    KuorumUser convertAsUser(KuorumUser user){
        user.userType = UserType.PERSON
        user.personalData.userType = UserType.PERSON
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsOrganization(KuorumUser user){
        user.userType = UserType.ORGANIZATION
        user.personalData.userType = UserType.ORGANIZATION
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser modifyRoleDependingOnUserData(KuorumUser user){
        List<RoleUser> authorities = []
        if (!user.password || user.password.startsWith(RegisterService.PREFIX_PASSWORD)){
            RoleUser rolePartialUser = RoleUser.findByAuthority("ROLE_INCOMPLETE_USER") //SALENDA HA PUESTO LOS NOMBRES AL REVES ¬¬
            authorities.add(rolePartialUser)
        }else if (isUserRegisteredCompletely(user)){
            RoleUser roleNormalUser = RoleUser.findByAuthority("ROLE_USER")
            authorities.add(roleNormalUser)
        }else{
            RoleUser rolePartialUser = RoleUser.findByAuthority("ROLE_PASSWORDCHANGED") //SALENDA HA PUESTO LOS NOMBRES RARUNOS
            authorities.add(rolePartialUser)
        }
        if (UserType.POLITICIAN == user.userType){
            RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
            authorities.add(rolePolitician)
        }
        if (user.authorities.find{it.authority == "ROLE_ADMIN"}){
            RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_ADMIN")
            authorities.add(rolePolitician)
        }
        user.authorities = authorities
        user
    }

    KuorumUser convertAsPolitician(KuorumUser user, Region politicianOnRegion,  PoliticalParty politicalParty){
        if (!politicianOnRegion || !politicalParty){
            throw new KuorumException("Un politico debe de tener institucion y grupo parlamentario","error.politician.politicianData")
        }
        user.userType = UserType.POLITICIAN
        user.personalData.userType = UserType.POLITICIAN
        if (!user.professionalDetails) user.professionalDetails = new ProfessionalDetails()
        user.professionalDetails.politicalParty = politicalParty.name
        user.professionalDetails.region = politicianOnRegion
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.add(rolePolitician)
        user.save()
    }

    /**
     * Adds premium roles to @user
     * @param user
     * @return
     */
    KuorumUser convertAsPremium(KuorumUser user){
        RoleUser rolePremium = RoleUser.findByAuthority("ROLE_PREMIUM")
        addRole(user, rolePremium)
    }

    KuorumUser addRole(KuorumUser user, RoleUser role){
        user.authorities.add(role)
        user.lastUpdated = new Date()//Mongo is not detecting changes on list, and is not updating the user roles. Modifying a root field, object is detected as dirty and it saves the changes
        user.save(flush: true)
    }

    /**
     * Removes the premium roles
     *
     * @param user
     * @return
     */
    KuorumUser convertAsNormalUser(KuorumUser user){
        RoleUser rolePremium = RoleUser.findByAuthority("ROLE_PREMIUM")
        user.lastUpdated = new Date() //Mongo is not detecting changes on list, and is not updating the user roles. Modifying a root field, object is detected as dirty and it saves the changes
        user.authorities.remove(rolePremium)
        user.save(flush: true)
    }

    /**
     * Returns the recommended users by the giving user. The recommended user are stored in the collection
     * RecommendedUserInfo as a list of user's ids. The final user recommended are the result of the list recommendedUsers
     * minus deletedRecommendedUsers and minus the following users of the current user.
     * @param user The user which obtain its recommended users
     * @param pagination The pagination params
     * @return
     */
    List<KuorumUser> recommendedUsers(KuorumUser user, Pagination pagination = new Pagination(), Boolean recalculateActivityIfEmpty = Boolean.TRUE){

        if (!user){
            return recommendedUsers(pagination)
        }
        List<KuorumUser> kuorumUsers = []
        List<ObjectId> recommendedUsers

        RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(user)
        if(recommendedUserInfo){
            recommendedUsers = recommendedUserInfo.recommendedUsers - recommendedUserInfo.deletedRecommendedUsers - user.following -[user.id]
            Integer max = Math.min(recommendedUsers.size(),pagination.max)
            if (max > 0){
                kuorumUsers = recommendedUsers[pagination.offset..max-1].inject([]){ result, kuorumUser ->
                    result << KuorumUser.get(kuorumUser)
                    result
                }
            }
        }

        if (kuorumUsers.size()+1 < pagination.max){
            Integer max = pagination.max - kuorumUsers.size()-1;
            List<KuorumUser> mostActiveUsers = mostActiveUsersSince(new Date() -7 , new Pagination(max: max*2))
            List<KuorumUser> deletedRecommendedUsers = []
            if (recommendedUserInfo){
                deletedRecommendedUsers = recommendedUserInfo.deletedRecommendedUsers
            }
            mostActiveUsers = mostActiveUsers - deletedRecommendedUsers - user.following -[user.id]
            max = Math.min(max, mostActiveUsers.size()-1)
            if (mostActiveUsers) {
                kuorumUsers += mostActiveUsers[0..max]
            }
            if (!kuorumUsers && recalculateActivityIfEmpty){
                log.warn("No se han detectado sugerencias para el usuario ${user}, se vuelven a calcular. ")
                recommendedUsersByActivityAndUser(user);
                return recommendedUsers(user, pagination, false);
            }
        }

        kuorumUsers as ArrayList<KuorumUser>
    }

    /**
     * Obtain the Facebook friends of a user by its token (a valid access token for Facebook Graph API). With the
     * obtained users, a new thread is created to execute the calculation of recommended users by Facebook friends in
     * background.
     * @param token A valid access token for a user.
     */
    void checkFacebookFriendsByUserToken(String token){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        String loadUrl = "https://graph.facebook.com/me/friends?access_token=${URLEncoder.encode(token, 'UTF-8')}"
        URL url = new URL(loadUrl)
        JSONElement facebookFriends = JSON.parse(url.readLines().first())
        List<ObjectId> recommendedUsers
        Thread.start {
            Date start = new Date()
            log.debug "Start background execution of checkFacebookFriendsByUserToken on $start ..."
            List<FacebookUser> facebookUsers = FacebookUser.findAllByIdNotEqual(user.id)
            RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(user)
            if(!recommendedUserInfo){
                new RecommendedUserInfo(recommendedUsers: [], user: user).save()
            }
            recommendedUsers = recommendedUsersByFacebookFriends(user, facebookUsers, facebookFriends, recommendedUserInfo)
            if(recommendedUserInfo.recommendedUsers){
                recommendedUserInfo.recommendedUsers[0..<recommendedUsers.size()] = recommendedUsers
            } else {
                recommendedUserInfo.recommendedUsers = recommendedUsers
            }
            recommendedUserInfo.save(flush: true)
            log.debug "... End background execution of checkFacebookFriendsByUserToken on ${new Date()} with total execution time ${TimeCategory.minus(new Date(),start)}"
        }
    }

    /**
     * TODO: Improve algorithm with map-reduce?
     * Obtain the users that are in the application and are friends of the current user.
     * - If the list of FacebookUsers that are friends of the user is greater than the maxSize specified in
     * RecommendedUserInfo, the result is the list of this FacebookUsers from 0 to the maximum specified in the
     * constraint of RecommendedUserInfo.
     * - If the list of FacebookUsers that are friends of the user is less than thee maxSize specified in
     * RecommendedUserInfo, the result is the list of FacebookUsers plus the recommended users that the user had
     * previously (if exists the RecommendedUserInfo for the user).
     * @param user The user which obtain its recommended users by facebook friends.
     * @param facebookUsers The collection of FacebookUser in the application
     * @param facebookFriends The collection of Faccebook friends of the user
     * @return
     */
    List<ObjectId> recommendedUsersByFacebookFriends(KuorumUser user, List<FacebookUser> facebookUsers, facebookFriends, RecommendedUserInfo recommendedUserInfo){
        List<FacebookUser> faceBookUsersFriendsOfUser
        List<ObjectId> kuorumUsersFriendsOfUser = []
        List<Long> facebookFriendsId = facebookFriends.data.id.collect{it.toLong()}
        GParsPool.withPool{
            faceBookUsersFriendsOfUser = facebookUsers.findAllParallel{(it.uid in facebookFriendsId && !(it.user.id in recommendedUserInfo.recommendedUsers))}
        }
        if(faceBookUsersFriendsOfUser){
            kuorumUsersFriendsOfUser = faceBookUsersFriendsOfUser*.user
            if(kuorumUsersFriendsOfUser.size() > RecommendedUserInfo.constraints.recommendedUsers.maxSize){
                return kuorumUsersFriendsOfUser[0..<RecommendedUserInfo.constraints.recommendedUsers.maxSize]*.id
            } else {
                if(recommendedUserInfo && recommendedUserInfo.recommendedUsers){
                    return kuorumUsersFriendsOfUser*.id + recommendedUserInfo.recommendedUsers[0..<(RecommendedUserInfo.constraints.recommendedUsers.maxSize - kuorumUsersFriendsOfUser.size())]
                } else {
                    return kuorumUsersFriendsOfUser*.id
                }
            }
        } else {
            return kuorumUsersFriendsOfUser
        }
    }

    /**
     * TODO: Improve algorithm with map-reduce?
     * Obtain the recommended users list for all users by activity criteria. The users are looped by blocks.
     */
    void recommendedUsersByActivity(){
        Integer offset = 0
        List<KuorumUser> kuorumUsers

        for(offset=0; offset<100;offset+=100){
            kuorumUsers = KuorumUser.list(offset: offset, max: 100)
            kuorumUsers.each{ user ->
                recommendedUsersByActivityAndUser(user)
            }
        }
    }

    /**
     * TODO: Improve algorithm with map-reduce? Currently this method has a total execution time about 1 minute 30 seconds for 1 user.
     * The recommended users for a user are calculated by the activity formula. Then, the recommended users
     * are sort by the formula and stored in the RecommendedUserInfo collection for the user.
     * @param user the user which calculates the recommended users
     */
    void recommendedUsersByActivityAndUser(KuorumUser user){
        List<KuorumUser> otherUsers, kuorumUsersOrdered = []
        RecommendedUserInfo recommendedUserInfo

        Date start = new Date()
        println("Starting recommendedUsersByActivityAndUser...")
        otherUsers = KuorumUser.findAllByIdNotEqual(user.id)
        GParsPool.withPool{
            calculateActivityClosure.memoize()
            otherUsers = KuorumUser.findAllByIdNotEqual(user.id)
            otherUsers.collectParallel{ it.activityForRecommendation = calculateActivityClosure(it, user) }
            kuorumUsersOrdered = otherUsers.parallel.sort{-it.activityForRecommendation}.collection[0..<RecommendedUserInfo.constraints.recommendedUsers.maxSize]
            recommendedUserInfo = RecommendedUserInfo.findByUser(user)
            if(!recommendedUserInfo){
                recommendedUserInfo = new RecommendedUserInfo(recommendedUsers: kuorumUsersOrdered*.id, user: user).save()
            } else {
                recommendedUserInfo.recommendedUsers = kuorumUsersOrdered*.id
                recommendedUserInfo.save()
            }
        }
        println("... End of recommendedUsersByActivityAndUser on ${new Date()} with total execution time ${TimeCategory.minus(new Date(),start)}")
    }

    /**
     * Calculate the activity criteria for the giving two users. The formula used is:
     *   A = Region + Organization + Politician + Total Post + Total Clucks  + D x Total of Post Comments + V x Total of victories
     * Where:
     *  - Region = Config value of kuorum.recommendedUser.regionValue if the users are in the same region.
     *  - Organization = Config value of kuorum.recommendedUser.organizationValue if the user is Organization.
     *  - Politician = Config value of kuorum.recommendedUser.politicianValue if the user is Organization.
     *  - V = Config value of kuorum.recommendedUser.victoryValue.
     *  - D = Config value of kuorum.recommendedUser.debateValue.
     */
    def calculateActivityClosure = {KuorumUser activeUser, KuorumUser compareUser ->
        Integer formula = 0
        if(compareUser.personalData.provinceCode && activeUser.personalData.provinceCode && compareUser.personalData.provinceCode == activeUser.personalData.provinceCode){
            formula += grailsApplication.config.kuorum.recommendedUser.regionValue
        }
        if(compareUser.userType == UserType.POLITICIAN){
            formula += grailsApplication.config.kuorum.recommendedUser.politicianValue
        } else if(compareUser.userType == UserType.ORGANIZATION){
            formula += grailsApplication.config.kuorum.recommendedUser.organizationValue
        }
        formula += Post.countByOwner(compareUser)
        formula += Cluck.countByOwner(compareUser)
        formula += grailsApplication.config.kuorum.recommendedUser.debateValue * PostComment.countByKuorumUser(compareUser)
        formula += grailsApplication.config.kuorum.recommendedUser.victoryValue  * Post.countByOwnerAndVictory(compareUser, true)

        formula
    }


    private List<KuorumUser> recommendedUsersByPostVotes(UserType userType, KuorumUser user = null, Pagination pagination = new Pagination()){
        def orderUsersByVotes = Post.collection.aggregate([
                [$match:['ownerPersonalData.userType':userType.toString()]],
                [$group:[_id:'$owner', numVotes:[$sum:'$numVotes']]],
                [$match:[numVotes:[$gt:0]]],
                [$sort:[numVotes:-1]]
        ])

        def results = orderUsersByVotes.results();
        List<KuorumUser> users = []
        (pagination.offset .. pagination.offset+pagination.max-1).each {
            if (results.size()>it){
                def aggregationData = results.get(it.toInteger())
                users << KuorumUser.get(aggregationData._id)
            }
        }
        users
    }

    List<KuorumUser> recommendedUsers(Pagination pagination = new Pagination()){
        //TODO: Improve algorithm
        List<KuorumUser> res = KuorumUser.findAllByNumFollowersGreaterThanAndEnabled(-1,true,[sort:"numFollowers",order: "desc", max:pagination.max])
        res as ArrayList<KuorumUser>
    }

    List<KuorumUser> recommendPoliticians(KuorumUser user, Pagination pagination = new Pagination()){
        List<KuorumUser> politicians = bestPoliticiansSince(user, null, pagination, Boolean.TRUE);
    }

    List<KuorumUser> recommendOrganizations(KuorumUser user, Pagination pagination = new Pagination()){
        recommendedUsersByPostVotes(UserType.ORGANIZATION, user, pagination)
    }

    List<KuorumUser> recommendPersons(KuorumUser user, Pagination pagination = new Pagination()){
        recommendedUsersByPostVotes(UserType.PERSON, user, pagination)
    }

    KuorumUser updateUser(KuorumUser user){
        if (user.personalData.province){
            user.personalData.provinceCode = user.personalData.province.iso3166_2
        }
        modifyRoleDependingOnUserData(user)
        if (springSecurityService.getCurrentUser().equals(user)){
            springSecurityService.reauthenticate user.email
        }
        if (!user.save()){
            def msg = "No se ha podido actualizar el usuario ${user.email}(${user.id})"
            log.error(msg)
            throw KuorumExceptionUtil.createExceptionFromValidatable(user, msg)
        }

        indexSolrService.index(user)
        kuorumMailService.mailingListUpdateUser(user)

        user
    }

    KuorumUser createUser(KuorumUser user){

        user.verified = user.verified?:false

        if (!user.save()){
            log.error("No se ha podido actualizar el usuario ${user.email}(${user.id})")
            throw KuorumExceptionUtil.createExceptionFromValidatable(user, "No se ha podido actualizar el usuario ${user.email}(${user.id})")
        }
        indexSolrService.index(user)
        user
    }

    List<UserParticipating> listUserActivityPerProject(KuorumUser user){
        def userActivity = Post.collection.aggregate(
                [$match : ['$or':[[owner:user.id], [defender:user.id]]]],
                [$group :[_id:'$project',quantity:[$sum:1]]]
        )
        List<UserParticipating> activity = []
        userActivity.results().each{
            UserParticipating userParticipating = new UserParticipating(project: Project.get(it._id), numTimes: it.quantity)
            activity << userParticipating
        }
        activity
    }

    List<KuorumUser> mostActiveUsersSince(Date startDate, Pagination pagination = new Pagination()){
        //MONGO QUERY
        // aggregate([ {$match:{isFirstCluck:false}}, {$group:{_id:'$owner',total:{$sum:1}}}, {$match:{total:{$gt:0}}}, {$sort:{total:-1}}, {$limit:2} ])
        Integer max = pagination.max
        def postOwners = Cluck.collection.aggregate(
                ['$match':[isFirstCluck:true, dateCreated:['$gt':startDate]] ],
                ['$group':[_id:'$owner',total:['$sum':1]]],
                ['$match':[total:['$gte':1]]],
                ['$sort':[total:-1]],
//                ['$skip':pagination.offset],
                ['$limit':pagination.max]
        )
        List<KuorumUser> mostActiveUsers = postOwners.results().collect{
            KuorumUser.load(it._id)
        }
        max = pagination.max - mostActiveUsers.size()
        //Not enought posts this week
        if (max > 0){
            def postCluckers = Cluck.collection.aggregate(
                    ['$match':[isFirstCluck:false,dateCreated:['$gt':startDate]]],
                    ['$group':[_id:'$owner',total:['$sum':1]]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$limit':max]
            )
            postCluckers.results().each{
                mostActiveUsers.add(KuorumUser.load(it._id))
            }
        }
        max = pagination.max - mostActiveUsers.size()
        //Not enought activity
        if (max > 0){
            log.info("No hay suficiente actividad, se usa la actividad general de cualquier usuario")
            BasicDBObject alreadyUsers = new BasicDBObject('$nin', mostActiveUsers.collect{it.id});
            def postCluckers = Cluck.collection.aggregate(
                    ['$match':[owner:alreadyUsers]],
                    ['$group':[_id:'$owner',total:['$sum':1]]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$limit':max]
            )
            postCluckers.results().each{
                mostActiveUsers.add(KuorumUser.load(it._id))
            }
        }
        mostActiveUsers
    }

    List<KuorumUser> bestSponsorsEver(Pagination pagination = new Pagination()){
        bestSponsorsSince(null, pagination)
    }
    List<KuorumUser> bestSponsorsSince(Date startDate, Pagination pagination = new Pagination()){
//Not enought posts this week
        List<KuorumUser> bestSponsors = []

        if (startDate){
            def bestSponsorsByDate = Cluck.collection.aggregate(
                    ['$match':[sponsors: ['$exists':1],dateCreated:['$gt':startDate]]],
                    ['$unwind':'$sponsors'],
                    ['$group':[_id:'$sponsors.kuorumUserId',total:['$sum':'$sponsors.amount']]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$limit':pagination.max]
            )
            bestSponsors= bestSponsorsByDate.results().collect{
                KuorumUser.load(it._id)
            }
        }

        //Not enought sponsor
        if (bestSponsors.size() < pagination.max){
            log.info("Buscando los mejores sponsors de la historia de kuorum")
            BasicDBObject alreadyUsers = new BasicDBObject('$nin', bestSponsors.collect{it.id});
            def bestSponsorsEver = Cluck.collection.aggregate(
                    ['$match':[sponsors: ['$exists':1],owner:alreadyUsers]],
                    ['$unwind':'$sponsors'],
                    ['$group':[_id:'$sponsors.kuorumUserId',total:['$sum':'$sponsors.amount']]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$skip':pagination.offset],
                    ['$limit':pagination.max]
            )
            bestSponsorsEver.results().each{
                bestSponsors.add(KuorumUser.load(it._id))
            }
        }

        if (bestSponsors.size() < pagination.max){
            log.warn("Using default sponsors")
            def userNamesFake = ["eQuo", "PACMA"]

            userNamesFake.each {
                KuorumUser user = KuorumUser.findByName(it)
                if (user) bestSponsors.add(user)
            }
        }
        bestSponsors
    }

    List<KuorumUser> bestPoliticiansSince(KuorumUser user, Date startDate, Pagination pagination = new Pagination(), Boolean isEnabled = null){
        SearchParams searchParams = new SearchParams(pagination.getProperties());
        searchParams.max +=1
        List<Region> regions;
        if (user && user.userType != UserType.POLITICIAN){
            regions = regionService.findUserRegions(user)
        }else if(user){
            regions = regionService.findRegionsList(user.professionalDetails.region)
        }else{
            regions = [[iso3166_2:"EU-ES"], [iso3166_2:"EU"]]
        }
        searchParams.regionIsoCodes = regions.collect{it.iso3166_2}
        searchParams.setType(SolrType.POLITICIAN)
        if (user?.userType == UserType.POLITICIAN && user?.tags){
            String searchTag = user.tags.join(" ")
            //Se busca a todos los politicos con el *:* pero se ordena por score aquellos que compartan tag
            searchParams.word = "(${searchTag} OR (*:*)"
        }
        SolrResults results = searchSolrService.search(searchParams)

        List<KuorumUser> politicians = results.elements.collect{SolrElement solrElement ->
            KuorumUser.get(solrElement.getId())
        }
        if(user) politicians = politicians.findAll{it.id != user.id}
        return politicians?politicians[0..Math.min(pagination.max-1, politicians.size())]:[]
    }

    void deleteAccount(KuorumUser user){
        user.enabled = Boolean.FALSE
        String nombreEmail = user.email.split("@")[0]
        String domain = user.email.split("@")[1]
        String email = "BORRADO_${nombreEmail}@NO_EMAIL_${domain}"
        user.email = email
        if (!user.save(flush: true)) {
            //TODO: Gestion errores
            log.error("Error salvando usuario ${user.id}. ERRORS => ${user.errors}")
            throw new KuorumException("Error desactivando un usuario")
        }
    }

    boolean isUserRegisteredCompletely(KuorumUser user){
        user.personalData.provinceCode != null
    }

    boolean isUserConfirmedMail(KuorumUser user){
        !user.authorities.find{RoleUser role-> role.authority == "ROLE_INCOMPLETE_USER" }
    }
}
