package kuorum.users

import com.daureos.facebook.FacebookGraphService
import com.mongodb.*
import grails.transaction.Transactional
import groovy.time.TimeCategory
import kuorum.Institution
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.post.PostComment
import kuorum.project.Project
import kuorum.post.Cluck
import kuorum.post.Post
import org.codehaus.groovy.grails.commons.GrailsApplication

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    def kuorumMailService
    def regionService
    FacebookGraphService facebookGraphService

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
        user.institution = null
        user.politicalParty = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsOrganization(KuorumUser user){
        user.userType = UserType.ORGANIZATION
        user.personalData.userType = UserType.ORGANIZATION
        user.institution = null
        user.politicalParty = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsPolitician(KuorumUser user, Institution institution,  PoliticalParty politicalParty){
        if (!institution || !politicalParty){
            throw new KuorumException("Un politico debe de tener institucion y grupo parlamentario","error.politician.politicianData")
        }
        user.userType = UserType.POLITICIAN
        user.personalData.userType = UserType.POLITICIAN
        user.institution = institution
        user.politicalParty = politicalParty
        user.politicianOnRegion = institution.region
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
     * TODO: Improve activity algorithm and test facebook algorithm
     * Returns the best users ever including organizations, normalUsers and politicians
     * @param user
     * @param pagination
     * @return
     */
    List<KuorumUser> recommendedUsers(KuorumUser user, Pagination pagination = new Pagination()){

        if (!user){
            return recommendedUsers(pagination)
        }
        List<KuorumUser> kuorumUsers = KuorumUser.findAllByIdNotEqual(user.id)
//        List<KuorumUser> kuorumUsers = []
        def facebookFriends = facebookGraphService.getFriends()

        if(facebookFriends){
            //Primer criterio
            List<FacebookUser> facebookUsers = FacebookUser.findAllByIdNotEqual(user.id)
            kuorumUsers = orderUserByFacebookFriends(user, facebookUsers, kuorumUsers, facebookFriends)
        } else {
            //Segundo criterio
//            kuorumUsers =  orderUsersByActivity(user, kuorumUsers)[pagination.offset..pagination.max]
            kuorumUsers =  user.followers[0..Math.min(5, user.followers.size())].collect{KuorumUser.get(it)}
        }

        kuorumUsers as ArrayList<KuorumUser>
    }

    /**
     * TODO: Test facebook algorithm
     * Obtain the users that are in the application and are not friends of the current user.
     * @param user
     * @param facebookUsers
     * @param kuorumUsers
     * @param facebookFriends
     * @return
     */
    List<KuorumUser> orderUserByFacebookFriends(KuorumUser user, List<FacebookUser> facebookUsers, List<KuorumUser> kuorumUsers , facebookFriends){
        List<FacebookUser> faceBookUsersInKuorumNotFriendOfUser = facebookUsers.findAll{!(it.id in facebookFriends.it.id)}
        List<KuorumUser> kuorumUsersInKuorumNotFriendOfUser = faceBookUsersInKuorumNotFriendOfUser*.user

        if(kuorumUsersInKuorumNotFriendOfUser < 5){
            List<KuorumUser> kuorumUsersByActivity = orderUsersByActivity(user, kuorumUsers)[kuorumUsersInKuorumNotFriendOfUser.size()..<5]
            return kuorumUsersInKuorumNotFriendOfUser + kuorumUsersByActivity
        } else {
            return kuorumUsersInKuorumNotFriendOfUser[0..<5]
        }
    }

    /**
     * TODO: Improve activity algorithm
     * Sort users by the following criteria:
     * A = Region + Organization + Politician + Total Post + Total Clucks  + D x Total of Post Comments + V x Total of victories
     * Where:
     *  - Region = Config value of kuorum.recommendedUser.regionValue if the users are in the same region.
     *  - Organization = Config value of kuorum.recommendedUser.organizationValue if the user is Organization.
     *  - Politician = Config value of kuorum.recommendedUser.politicianValue if the user is Organization.
     *  - V = Config value of kuorum.recommendedUser.victoryValue.
     *  - D = Config value of kuorum.recommendedUser.debateValue.
     * @param user The user to compare with.
     * @param kuorumUsers The list of users to compare with the user.
     * @return A list of users sorted descending by the criteria.
     */
    List<KuorumUser> orderUsersByActivity(KuorumUser user, List<KuorumUser> kuorumUsers){
        def start = new Date()
        Integer formula = 0
        kuorumUsers.each{
            formula = 0
            if(it.personalData.provinceCode && user.personalData.provinceCode && it.personalData.provinceCode == user.personalData.provinceCode){
                formula += grailsApplication.config.kuorum.recommendedUser.regionValue
            }
            if(it.userType == UserType.POLITICIAN){
                formula += grailsApplication.config.kuorum.recommendedUser.politicianValue
            } else if(it.userType == UserType.ORGANIZATION){
                formula += grailsApplication.config.kuorum.recommendedUser.organizationValue
            }
            formula += Post.countByOwner(it)
            formula += Cluck.countByOwner(it)
            formula += grailsApplication.config.kuorum.recommendedUser.debateValue * PostComment.countByKuorumUser(it)
            formula += grailsApplication.config.kuorum.recommendedUser.victoryValue  * Post.countByOwnerAndVictory(it, true)
            it.metaClass.formula = formula
        }

        kuorumUsers.sort{-it.formula}

        def end = new Date()
        println(" --------------> Tiempo: ${TimeCategory.minus(end,start)}")

        kuorumUsers
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
        user.personalData.provinceCode = user.personalData.province.iso3166_2
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

        //Not enought posts this week
        if (mostActiveUsers.size() < pagination.max){
            def postCluckers = Cluck.collection.aggregate(
                    ['$match':[isFirstCluck:false,dateCreated:['$gt':startDate]]],
                    ['$group':[_id:'$owner',total:['$sum':1]]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$limit':pagination.max]
            )
            postCluckers.results().each{
                mostActiveUsers.add(KuorumUser.load(it._id))
            }
        }

        //Not enought activity
        if (mostActiveUsers.size() < pagination.max){
            log.info("No hay suficiente actividad, se usa la actividad general de cualquier usuario")
            BasicDBObject alreadyUsers = new BasicDBObject('$nin', mostActiveUsers.collect{it.id});
            def postCluckers = Cluck.collection.aggregate(
                    ['$match':[owner:alreadyUsers]],
                    ['$group':[_id:'$owner',total:['$sum':1]]],
                    ['$match':[total:['$gte':1]]],
                    ['$sort':[total:-1]],
                    ['$limit':pagination.max]
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

        DBCollection bestPoliticiansCollection = createUserScore(startDate)

        DBObject query = new BasicDBObject('user.userType',UserType.POLITICIAN.toString())
        List<Region> regions;
        if (user){
            regions = regionService.findUserRegions(user)
        }else{
            regions = [[iso3166_2:"EU-ES"], [iso3166_2:"EU"]]
        }
        DBObject inRegions = new BasicDBObject('$in',regions.collect{it.iso3166_2})
        query.append('user.politicianOnRegion.iso3166_2',inRegions)


        DBCursor cursor = bestPoliticiansCollection.find(query)
        DBObject sort = new BasicDBObject();
        sort.append('user.enabled',-1)
        sort.append('iso3166Length',-1);
        sort.append('score',-1);
        sort.append('numFollowers',-1)
        cursor.sort(sort)
        cursor.limit(pagination.max.intValue())
        cursor.skip(pagination.offset.intValue())

        cursor.collect {KuorumUser.get(it._id)}

    }

    private Date chapuSyncReloadScore = new Date() -1
    private DBCollection createUserScore(Date startDate){

        def tempCollectionName = "bestPoliticians"
        DBCollection userScoredCollection = Post.collection.getDB().getCollection(tempCollectionName);
        //TODO: HACER ESTO MEJOR QUE CON ESTE SYNC CHAAAPUUU
        synchronized (this){
            Boolean reloadScore = chapuSyncReloadScore < new Date() -1
            if (!reloadScore){
                return userScoredCollection
            }
            chapuSyncReloadScore = chapuSyncReloadScore.clearTime()+1

            log.warn("Calculando SCORE. Operacion lenta. Hay que cachearla o hacerla por la noche")

            //TODO: CACHE THIS QUERY
            //TODO: Use startDate. Now is getting best politicians ever
            String mapPost = """
                function() {
                    if (this.defender != undefined){
                        emit(this.defender, 3)
                    }
                    if (this.debates != undefined) {
                        this.debates.forEach( function(debate) {
                            emit(debate.kuorumUserId, 2)
                        });
                    }
                    emit(this.owner, 1)
                }
            """

            String reducePost = """
                function(key, values) {
                    var acc = 0;
                    values.forEach( function(value) {
                        acc +=value;
                    });
                    return acc;
                }
            """

            DBObject queryPost = new BasicDBObject();
            DBObject existsDefender = new BasicDBObject(); existsDefender.put('$exists','1');
            queryPost.put("defender",existsDefender);
            DBObject sortResult = new BasicDBObject();
            sortResult.put('value',-1)

            DBCollection postCollection = Post.collection

            MapReduceCommand bestPoliticians = new MapReduceCommand(
                    postCollection,
                    mapPost,
                    reducePost ,
                    tempCollectionName,
                    MapReduceCommand.OutputType.MERGE,null);
    //        bestPoliticians.sort = sortByValue
    //        bestPoliticians.limit = pagination.max

            MapReduceOutput result = Post.collection.mapReduce(bestPoliticians)


            def cpUserData = """
            function (){
                db.${tempCollectionName}.find().forEach(function(score){
                    var kuorumUser = db.kuorumUser.find({_id:score._id})[0]
                    var numFollowers = kuorumUser.followers.length
                    var iso3166Length = 0
                    if (kuorumUser.politicianOnRegion != undefined){
                        iso3166Length = kuorumUser.politicianOnRegion.iso3166_2.length
                    }

                    var newScore = {
                        _id: score._id,
                        score : score.value,
                        numFollowers:numFollowers,
                        iso3166Length : iso3166Length,
                        user:kuorumUser
                    };
                    db.${tempCollectionName}.save(newScore);
                });
            }
            """
            userScoredCollection.getDB().eval(cpUserData)

            def cpPoliticiansWithOutScore = """
            function (){
                db.kuorumUser.find({userType:'${UserType.POLITICIAN}'}).forEach(function(politician){
                    var score = db.${tempCollectionName}.find({_id:politician._id})[0];
                    var iso3166Length = 0
                    if (politician.politicianOnRegion != undefined){
                        iso3166Length = politician.politicianOnRegion.iso3166_2.length
                    }
                    if (score == undefined){
                        var numFollowers = politician.followers.length
                        var newScore = {
                            _id: politician._id,
                            score : 0,
                            numFollowers:numFollowers,
                            iso3166Length : iso3166Length,
                            user:politician
                        };
                        db.${tempCollectionName}.save(newScore);
                    }
                });
            }
            """
            userScoredCollection.getDB().eval(cpPoliticiansWithOutScore)
            DBObject index = new BasicDBObject("score", -1)
            index.append("iso3166Length", -1)
            userScoredCollection.createIndex(index)
            return userScoredCollection
        }
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

    Boolean checkIfIsPolititician(KuorumUser user){
        user.institution!=null
    }

    boolean isUserRegisteredCompletely(KuorumUser user){
        user.personalData.provinceCode != null
    }
}
