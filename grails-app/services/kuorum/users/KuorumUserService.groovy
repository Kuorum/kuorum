package kuorum.users

import com.fasterxml.jackson.core.type.TypeReference
import com.mongodb.BasicDBObject
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovyx.gpars.GParsPool
import kuorum.causes.CausesService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.kuorumUser.UserParticipating
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrType
import kuorum.mail.KuorumMailAccountService
import kuorum.post.Cluck
import kuorum.post.Post
import kuorum.project.Project
import kuorum.solr.SearchSolrService
import kuorum.util.rest.RestKuorumApiService
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.UserDataRDTO
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserDataDomainValidationDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.SearchTypeRSDTO
import org.springframework.security.access.prepost.PreAuthorize

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    def kuorumMailService
    def regionService
    SpringSecurityService springSecurityService
    SearchSolrService searchSolrService;
    KuorumMailAccountService kuorumMailAccountService;
    CausesService causesService
    RestKuorumApiService restKuorumApiService


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
            addFollowerAsContact(follower, following)
        }
    }

    private addFollowerAsContact(KuorumUser follower, KuorumUser following){
        Map<String, String> params = [userId: following.id.toString()]
        Map<String, String> query = [followerId: follower.id.toString()]
        restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FOLLOWER,
                params,
                query,
                null,
                null
        )
    }

    private void updateKuorumUserOnRest(KuorumUser user){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        UserDataRDTO userDataRDTO = new UserDataRDTO();
        userDataRDTO.setEmail(user.getEmail());
        userDataRDTO.setName(user.getName());
        restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_DATA,
                params,
                query,
                userDataRDTO,
                null
        )
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
            deleteFollowerAsContact(follower, following)
        }
    }

    private deleteFollowerAsContact(KuorumUser follower, KuorumUser following){
        Map<String, String> params = [userId: following.id.toString()]
        Map<String, String> query = [followerId: follower.id.toString()]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER_CONTACT_FOLLOWER,
                params,
                query
        )
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
        List<ObjectId> notValidUsers = user.following +[user.id]

        RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(user)
        if(recommendedUserInfo){
            notValidUsers = recommendedUserInfo.deletedRecommendedUsers + user.following +[user.id]
            recommendedUsers = recommendedUserInfo.recommendedUsers - notValidUsers
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
            mostActiveUsers = mostActiveUsers - notValidUsers
            max = Math.min(max, mostActiveUsers.size()-1)
            if (mostActiveUsers) {
                kuorumUsers += mostActiveUsers[0..max]
            }
            if (!kuorumUsers && recalculateActivityIfEmpty){
                log.warn("No se han detectado sugerencias para el usuario ${user}")
//                recommendedUsersByActivityAndUser(user);
//                return recommendedUsers(user, pagination, false);
                log.info("Se cojen los últimos usuarios que no sean ya seguidores de ${user}")
                kuorumUsers = KuorumUser.findAllByIdNotInList(notValidUsers, [sort:"numFollowers",order: "desc", max:pagination.max])
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

    List<KuorumUser> recommendedUsers(Pagination pagination = new Pagination()){
        //TODO: Improve algorithm
        List<KuorumUser> res = KuorumUser.findAllByNumFollowersGreaterThanAndEnabled(-1,true,[sort:"numFollowers",order: "desc", max:pagination.max])
        res as ArrayList<KuorumUser>
    }

    /**
     * Recommend politicians similar to user and that the user is not following
     *
     * @param user
     * @param pagination
     * @return
     */
    List<KuorumUser> recommendUsers(KuorumUser user, Pagination pagination = new Pagination()) {

        KuorumUser loggedUser = springSecurityService.getCurrentUser();
        List<ObjectId> filterPoliticians = []
        if (loggedUser){
            filterPoliticians  = []
            filterPoliticians.addAll(loggedUser.following?:[])
            filterPoliticians << loggedUser.id
            RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(loggedUser)
            if(recommendedUserInfo){
                filterPoliticians.addAll(recommendedUserInfo.deletedRecommendedUsers)
            }else{
                log.warn("User ${loggedUser.name} (${loggedUser.id}) has not calculated recommendedUserInfo")
            }
        }

        bestUsers(user, filterPoliticians, pagination);
    }


    @PreAuthorize("hasPermission(#userAlias, 'kuorum.users.KuorumUser','edit')")
    KuorumUser findEditableUser(String userAlias){
        findByAlias(userAlias)
    }

    KuorumUser findByAlias(String userAlias){
        if (!userAlias)
            return null
        else
            return KuorumUser.findByAliasAndDomain(userAlias.toLowerCase(), CustomDomainResolver.domain)
    }

    KuorumUser findByOldAlias(String oldUserAlias){
        if (!oldUserAlias)
            return null
        else
            return KuorumUser.findByOldAliasAndDomain(oldUserAlias.toLowerCase(),CustomDomainResolver.domain)
    }

    @PreAuthorize("hasPermission(#user, 'edit')")
    KuorumUser updateUser(KuorumUser user){
        KuorumUser.withNewTransaction {
            if (user.personalData.province){
                user.personalData.provinceCode = user.personalData.province.iso3166_2
                user.personalData.country = regionService.findCountry(user.personalData.province)
            }
            if (springSecurityService.getCurrentUser().equals(user)){
                springSecurityService.reauthenticate user.email
            }
            user.updateDenormalizedData()
            if (!user.save(flush:true)){
                def msg = "No se ha podido actualizar el usuario ${user.email}(${user.id})"
                log.error(msg)
                throw KuorumExceptionUtil.createExceptionFromValidatable(user, msg)
            }
        }
        indexSolrService.deltaIndex()
        kuorumMailService.mailingListUpdateUser(user)
        updateKuorumUserOnRest(user);


        user
    }

    KuorumUser updateUserRelevance(KuorumUser user, Long relevance){
        user["relevance"] = relevance
        user.save() //Not user updateUser because relevance is a little special
    }

    Long getUserRelevance(KuorumUser user){
        user["relevance"]?:0
    }
    KuorumUser updateAlias(KuorumUser user, String newAlias){
        String currentAlias = user.alias
        if (user.alias != newAlias){
            try{
                newAlias = newAlias.toLowerCase()
                log.info("Updating alias ${user.alias} -> ${newAlias}")
                def res = KuorumUser.collection.update([_id:user.id],['$set':[alias:newAlias], '$push':[oldAlias:currentAlias]])
                user.refresh()
                kuorumMailAccountService.changeAliasAccount(currentAlias, newAlias)
            }catch (Exception e){
                log.error("Erro updating user alias ${user.alias} -> ${newAlias}",e)
                def res = KuorumUser.collection.update([_id:user.id],['$set':[alias:currentAlias]])
                user.refresh()
                return null;
            }
        }
        return user;
    }

    KuorumUser updateEmail(KuorumUser user, String email){
        user.email = email;
        user.save(flush:true)
        updateKuorumUserOnRest(user);
    }


    KuorumUser createUser(KuorumUser user){

        KuorumUser.withTransaction {
            user.verified = user.verified?:false

            if (!user.save()){
                log.error("No se ha podido actualizar el usuario ${user.email}(${user.id})")
                throw KuorumExceptionUtil.createExceptionFromValidatable(user, "No se ha podido actualizar el usuario ${user.email}(${user.id})")
            }
        }
        indexSolrService.deltaIndex()
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

    List<KuorumUser> bestUsers(KuorumUser user, List<ObjectId> userFiltered = [], Pagination pagination = new Pagination()){
        SearchParams searchParams = new SearchParams(pagination.getProperties().findAll{k,v->k!="class"});
        searchParams.max +=1
//        List<Region> regions;
//        String politicalParty = ""
//        if (isPaymentUser(user)){
//            regions = regionService.findUserRegions(user)
//        }else if(user){
//            regions = regionService.findRegionsList(user.professionalDetails?.region)
//            politicalParty = user?.professionalDetails?.politicalParty?:''
//        }else{
////            regions = [[iso3166_2:"EU-ES"], [iso3166_2:"EU"]]
//        }
//        if (regions){
//            searchParams.boostedRegions = regions.collect{it.iso3166_2}
//        searchParams.regionIsoCodes = regions.collect{it.iso3166_2}
//        }
        searchParams.setType(SolrType.KUORUM_USER)
        searchParams.filteredUserIds = userFiltered.collect{it.toString()}
        if (user) searchParams.filteredUserIds << user.id.toString()

        SearchResultsRSDTO results = searchSolrService.searchAPI(searchParams)

        List<KuorumUser> politicians = results.data.collect{ SearchKuorumElementRSDTO searchElement ->
            KuorumUser politician = null;
            if (searchElement.type != SearchTypeRSDTO.KUORUM_USER){
                log.error("Error suggesting user. It is not an user. It is a ${searchElement.type}")
            }else{
                politician = KuorumUser.get(searchElement.getId())
                if (!politician){
                    log.warn("Error suggested user :: ${searchElement.name} | ${searchElement.id}" )
                }else if (!politician.alias){
                    log.warn("Error suggested user [NO ALIAS]:: ${searchElement.name} | ${searchElement.id}" )
                    politician = null;
                }
            }
            politician
        }
        // Filtering null
        politicians = politicians.findAll{it}
        return politicians?politicians[0..Math.min(pagination.max-1, politicians.size()-1)]:[]
    }

    void deleteAccount(KuorumUser user){

        user.enabled = Boolean.FALSE
        String nombreEmail = user.email.split("@")[0]
        String domain = user.email.split("@")[1]
        String fechaBaja = new Date().getTime().toString()
        String email = "BORRADO_${nombreEmail}_${fechaBaja}@NO-EMAIL-${domain}"
        user.email = email
        user.alias = fechaBaja + user.alias
        if (user.alias.size() > 15) user.alias = user.alias.substring(user.alias.size() - 15 , user.alias.size() - 1)
        user.authorities.remove(RoleUser.findByAuthority("ROLE_USER"));
        user.authorities.add(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER"));

        if (!user.save(flush: true)) {
            //TODO: Gestion errores
            log.error("Error salvando usuario ${user.id}. ERRORS => ${user.errors}")
            throw new KuorumException("Error desactivando un usuario")
        }                   
        indexSolrService.deltaIndex()

        // CALLING API TO REMOVE CONTACT
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER,
                params,
                query
        )
    }

    KuorumUserRSDTO findUserRSDTO(String userId){
// CALLING API TO REMOVE CONTACT
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [:]
        def apiResponse= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER,
                params,
                query,
                new TypeReference<KuorumUserRSDTO>(){}
        )
        return apiResponse.data

    }

    boolean userDomainValidation(KuorumUser user, String ndi, String postalCode, Date birthDate){
// CALLING API TO REMOVE CONTACT
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        UserDataDomainValidationDTO userDataDomainValidationDTO = new UserDataDomainValidationDTO()
        userDataDomainValidationDTO.postalCode=postalCode
        userDataDomainValidationDTO.ndi=ndi
        userDataDomainValidationDTO.birthDate=kuorum.util.TimeZoneUtil.convertToUserTimeZone(birthDate, TimeZone.getTimeZone("UTC"))
        try{
            def apiResponse= restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.USER_DOMAIN_VALIDATION,
                    params,
                    query,
                    userDataDomainValidationDTO,
                    new TypeReference<KuorumUserRSDTO>(){}
            )
            springSecurityService.reauthenticate user.email
            return springSecurityService.authentication.authorities.find{it.authority==UserRoleRSDTO.ROLE_USER_VALIDATED.toString()}
        }catch (Exception e){
            log.error("Exception validating user: [Excpt: ${e}]")
            return false;
        }

    }

    @Deprecated
    boolean isUserRegisteredCompletely(KuorumUser user){
        user.personalData.provinceCode != null
    }

    boolean isUserConfirmedMail(KuorumUser user){
        !user.authorities.find{RoleUser role-> role.authority == "ROLE_INCOMPLETE_USER" }
    }

    String generateValidAlias(String name, Boolean validEmptyAlias = false){
        String alias = name.replaceAll("[^a-zA-Z0-9]+","");
        alias = alias.substring(0, Math.min(alias.length(), KuorumUser.ALIAS_MAX_SIZE)).toLowerCase()
        if (!alias && validEmptyAlias){
            return "";
        }
        alias = alias?:new Double(Math.floor(Math.random()*Math.pow(10, KuorumUser.ALIAS_MAX_SIZE))).intValue()
        KuorumUser user = KuorumUser.findByAliasAndDomain(alias, CustomDomainResolver.domain)
        while (user){
            alias = alias.take(alias.length() -2)
            alias = "${alias}${new Double(Math.floor(Math.random()*100)).intValue()}"
            user = KuorumUser.findByAliasAndDomain(alias, CustomDomainResolver.domain)
        }
        return alias;
    }
}
