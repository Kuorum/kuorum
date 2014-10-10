package kuorum.users

import com.mongodb.*
import grails.transaction.Transactional
import kuorum.Institution
import kuorum.ParliamentaryGroup
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.UserType
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.post.Cluck
import kuorum.post.Post

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    def kuorumMailService

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
        user.parliamentaryGroup = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsOrganization(KuorumUser user){
        user.userType = UserType.ORGANIZATION
        user.personalData.userType = UserType.ORGANIZATION
        user.institution = null
        user.parliamentaryGroup = null
        RoleUser rolePolitician = RoleUser.findByAuthority("ROLE_POLITICIAN")
        user.authorities.remove(rolePolitician)
        user.save()
    }

    KuorumUser convertAsPolitician(KuorumUser user, Institution institution,  ParliamentaryGroup parliamentaryGroup){
        if (!institution || !parliamentaryGroup){
            throw new KuorumException("Un politico debe de tener institucion y grupo parlamentario","error.politician.politicianData")
        }
        user.userType = UserType.POLITICIAN
        user.personalData.userType = UserType.POLITICIAN
        user.institution = institution
        user.parliamentaryGroup = parliamentaryGroup
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
     * Returns the best users ever including organizations, normalUsers and politicians
     * @param user
     * @param pagination
     * @return
     */
    List<KuorumUser> recommendedUsers(KuorumUser user, Pagination pagination = new Pagination()){
        if (!user){
            return recommendedUsers(pagination)
        }
        //TODO: Improve algorithm
        List<KuorumUser> res = []
//        List<KuorumUser> res = KuorumUser.findAllByNumFollowersGreaterThanAndEmailNotEqual(-1,user.email,[sort:"numFollowers",order: "desc", max:pagination.max])
        def userList = user.following.collect{KuorumUser.load(it)}
        userList << user
        def result = KuorumUser.createCriteria().list(max:pagination.max, offset:pagination.offset) {
            'gt'("numFollowers",0)
            not {'in'("id",userList)}
            order("numFollowers","desc")
        }
        result as ArrayList<KuorumUser>
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
        List<KuorumUser> politicians = bestPoliticiansSince(null, pagination, Boolean.TRUE);
    }

    List<KuorumUser> recommendOrganizations(KuorumUser user, Pagination pagination = new Pagination()){
        recommendedUsersByPostVotes(UserType.ORGANIZATION, user, pagination)
    }

    List<KuorumUser> recommendPersons(KuorumUser user, Pagination pagination = new Pagination()){
        recommendedUsersByPostVotes(UserType.PERSON, user, pagination)
    }

    KuorumUser updateUser(KuorumUser user){
        if (!user.save()){
            log.error("No se ha podido actualizar el usuario ${user.email}(${user.id})")
            throw KuorumExceptionUtil.createExceptionFromValidatable(user)
        }
        indexSolrService.index(user)
        kuorumMailService.mailingListUpdateUser(user)

        user
    }

    KuorumUser createUser(KuorumUser user){

        user.verified = user.verified?:false

        if (!user.save()){
            log.error("No se ha podido actualizar el usuario ${user.email}(${user.id})")
            throw KuorumExceptionUtil.createExceptionFromValidatable(user)
        }
        indexSolrService.index(user)
        user
    }

    List<UserParticipating> listUserActivityPerLaw(KuorumUser user){
        def userActivity = Post.collection.aggregate(
                [$match : ['$or':[[owner:user.id], [defender:user.id]]]],
                [$group :[_id:'$law',quantity:[$sum:1]]]
        )
        List<UserParticipating> activity = []
        userActivity.results().each{
            UserParticipating userParticipating = new UserParticipating(law:Law.get(it._id), numTimes: it.quantity)
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

    List<KuorumUser> bestPoliticiansSince(Date startDate, Pagination pagination = new Pagination(), Boolean isEnabled = null){
        //TODO: CACHE THIS QUERY
        //TODO: Use startDate. Now is getting best politicians ever

        String mapPost = """
            function() {
                if (this.defender != undefined){
                    emit(this.defender, 2)
                }
                if (this.debate != undefined) {
                    this.debates.forEach( function(value) {
                            emit(value.kuorumUserId, 1)
                    });
                }
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
        def tempCollectionName = "bestPoliticians"
        MapReduceCommand bestPoliticians = new MapReduceCommand(
                postCollection,
                mapPost,
                reducePost ,
                tempCollectionName,
                MapReduceCommand.OutputType.REPLACE, null);
//        bestPoliticians.sort = sortByValue
        bestPoliticians.limit = pagination.max

        DBCollection bestPoliticiansCollection = Post.collection.getDB().getCollection(tempCollectionName);
        log.warn("Realizando un MAP REDUCE. Operación lenta que no se debe ejecturar muchas veces")
        MapReduceOutput result = Post.collection.mapReduce(bestPoliticians)

        List<KuorumUser> politicians = bestPoliticiansCollection.find()
                .sort(sortResult)
                .skip((int) pagination.offset)
                .limit(pagination.max)
                .collect{KuorumUser.load(it._id)}

        if (politicians.size() < pagination.max){
            log.info("Poniendo politicos en función del número de seguidores que tiene")
            def userList = politicians.collect{it.id}
            def politicianByFollowers = KuorumUser.createCriteria().list(max:pagination.max, offset:pagination.offset) {
                'eq'("userType", UserType.POLITICIAN)
                if (isEnabled != null) 'eq'("enabled", isEnabled)
                if (userList)
                    not {'in'("id",userList)}

                and{
                    order('enabled','desc')
                    order("numFollowers","desc")
                }
            }
            politicians.addAll(politicianByFollowers)
        }

        politicians

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
}
