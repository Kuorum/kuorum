package kuorum.users

import com.fasterxml.jackson.core.type.TypeReference
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import groovy.time.TimeCategory
import groovyx.gpars.GParsPool
import kuorum.causes.CausesService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.SolrType
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.solr.SearchSolrService
import kuorum.util.rest.RestKuorumApiService
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement
import org.kuorum.rest.model.domain.SocialRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.UserDataRDTO
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserDataDomainValidationDTO
import org.kuorum.rest.model.kuorumUser.domainValidation.UserPhoneValidationDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
import org.kuorum.rest.model.search.SearchResultsRSDTO
import org.kuorum.rest.model.search.SearchTypeRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.springframework.security.access.prepost.PreAuthorize

@Transactional
class KuorumUserService {

    def notificationService
    def indexSolrService
    KuorumMailService kuorumMailService
    def regionService
    SpringSecurityService springSecurityService
    SearchSolrService searchSolrService
    CausesService causesService
    RestKuorumApiService restKuorumApiService


    GrailsApplication grailsApplication

    def createFollower(KuorumUserSession follower, BasicDataKuorumUserRSDTO following) {
        if (follower.id.toString() == following.id){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (following.isFollowing){
            log.warn("Se ha intentado seguir a un usuario que ya exisitía")
        }else{
            Map<String, String> params = [userId: following.id.toString()]
            Map<String, String> query = [follower: follower.id.toString()]
            restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.USER_FOLLOWER,
                    params,
                    query,
                    null,
                    null
            )
        }
    }

    UserDataRDTO mapUserToRDTO(KuorumUserSession loggedUser){
        KuorumUserRSDTO userRSDTO = findUserRSDTO(loggedUser)
        return mapUserToRDTO(userRSDTO)
    }
    UserDataRDTO mapUserToRDTO(KuorumUserRSDTO kuorumUserRSDTO){
        UserDataRDTO userDataRDTO = new UserDataRDTO()
        userDataRDTO.id = kuorumUserRSDTO.id
        userDataRDTO.name = kuorumUserRSDTO.name
        userDataRDTO.surname = kuorumUserRSDTO.surname
        userDataRDTO.alias = kuorumUserRSDTO.alias
        userDataRDTO.email = kuorumUserRSDTO.email
        userDataRDTO.socialLinks = kuorumUserRSDTO.socialLinks
        userDataRDTO.bio = kuorumUserRSDTO.bio
        userDataRDTO.timeZoneId = kuorumUserRSDTO.timeZoneId
        return userDataRDTO
    }

    KuorumUserRSDTO updateKuorumUser(KuorumUserSession user, UserDataRDTO userDataRDTO){
        updateKuorumUser(user.id.toString(), userDataRDTO)
    }
    KuorumUserRSDTO updateKuorumUser(String userId, UserDataRDTO userDataRDTO){
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [:]
        def apiResponse = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.USER_DATA,
                params,
                query,
                userDataRDTO,
                null
        )
        return apiResponse.data
    }

    private KuorumUserRSDTO updateKuorumUserOnRest(KuorumUser user){
        UserDataRDTO userDataRDTO = new UserDataRDTO()
        userDataRDTO.setEmail(user.getEmail())
        userDataRDTO.setName(user.getName())
        userDataRDTO.setSurname(user.getSurname())
        userDataRDTO.setBio(user.getBio())
        userDataRDTO.setSocialLinks(mapSocial(user))
        userDataRDTO.provinceCode = user.personalData.provinceCode
        userDataRDTO.alias=user.alias
        userDataRDTO.timeZoneId=user.timeZoneId
        return updateKuorumUser(user.id.toString(), userDataRDTO)
    }

    private SocialRDTO mapSocial(KuorumUser user){
        SocialRDTO social = new SocialRDTO()
        if (user.socialLinks){
            social.properties.each {
                if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                    social."${it.key}" = user.socialLinks."${it.key}"
            }
        }
        return social
    }

    def deleteFollower(KuorumUserSession follower, BasicDataKuorumUserRSDTO following) {
        if (follower.id == following.id){
            throw new KuorumException("No se pude seguir a uno mismo","error.following.sameUser")
        }
        if (!following.isFollowing){
            log.warn("Se ha intentado eliminar un follower que no existia")
        }else{
            Map<String, String> params = [userId: following.id]
            Map<String, String> query = [follower: follower.id.toString()]
            restKuorumApiService.delete(
                    RestKuorumApiService.ApiMethod.USER_FOLLOWER,
                    params,
                    query,
                    null
            )
        }
    }

    List<BasicDataKuorumUserRSDTO> findFollowers(String userId, Pagination pagination){

        Map<String, String> params = [userId: userId]
        Map<String, String> query = [page:Math.round(pagination.offset/pagination.max)]
        def apiResponse= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_FOLLOWER,
                params,
                query,
                new TypeReference<List<BasicDataKuorumUserRSDTO>>(){}
        )
        return apiResponse.data
    }

    List<BasicDataKuorumUserRSDTO> findFollowing(String userId, Pagination pagination){
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [page:Math.round(pagination.offset/pagination.max)]
        def apiResponse= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_FOLLOWER_FOLLOWING,
                params,
                query,
                new TypeReference<List<BasicDataKuorumUserRSDTO>>(){}
        )
        return apiResponse.data
    }

    /**
     * Obtain the Facebook friends of a user by its token (a valid access token for Facebook Graph API). With the
     * obtained users, a new thread is created to execute the calculation of recommended users by Facebook friends in
     * background.
     * @param token A valid access token for a user.
     */
    @Deprecated
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
    @Deprecated
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
     * Recommend politicians similar to user and that the user is not following
     *
     * @param user
     * @param pagination
     * @return
     */
    List<SearchKuorumUserRSDTO> recommendUsers(KuorumUser user, Pagination pagination = new Pagination()) {
        List<ObjectId> filterUsers = []
        if (springSecurityService.isLoggedIn()){
            KuorumUserSession userSession = springSecurityService.principal
            KuorumUser loggedUser = KuorumUser.findById(userSession.id)

            filterUsers  = []
            filterUsers.addAll(loggedUser.following?:[])
            filterUsers << loggedUser.id
            RecommendedUserInfo recommendedUserInfo = RecommendedUserInfo.findByUser(loggedUser)
            if(recommendedUserInfo){
                filterUsers.addAll(recommendedUserInfo.deletedRecommendedUsers)
            }else{
                log.info("User ${loggedUser.name} (${loggedUser.id}) has not calculated recommendedUserInfo")
            }
        }
        if (user) filterUsers << user.id
        bestUsers(filterUsers, pagination)
    }


    @PreAuthorize("hasPermission(#userAlias, 'org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO','edit')")
    KuorumUser findEditableUser(String userAlias){
        findByAlias(userAlias)
    }

    @Deprecated
    KuorumUser findByAlias(String userAlias){
        if (!userAlias)
            return null
        else
            return KuorumUser.findByAliasAndDomain(userAlias.toLowerCase(), CustomDomainResolver.domain)
    }

    @PreAuthorize("hasPermission(#user, 'edit')")
    KuorumUser updateUser(KuorumUser user){

        user.updateDenormalizedData()
        if (!user.save(flush:true)){
            def msg = "No se ha podido actualizar el usuario ${user.email}(${user.id})"
            log.error(msg)
            throw KuorumExceptionUtil.createExceptionFromValidatable(user, msg)
        }
        indexSolrService.deltaIndex()
        updateKuorumUserOnRest(user)
        if (springSecurityService.getPrincipal().id.toString().equals(user.id.toString())){
            springSecurityService.reauthenticate user.email
        }
        user
    }

    KuorumUser updateAlias(KuorumUser user, String newAlias){
        String currentAlias = user.alias
        if (user.alias != newAlias){
            try{
                newAlias = newAlias.toLowerCase()
                log.info("Updating alias ${user.alias} -> ${newAlias}")
                def res = KuorumUser.collection.update([_id:user.id],['$set':[alias:newAlias], '$push':[oldAlias:currentAlias]])
                user.refresh()
            }catch (Exception e){
                log.error("Erro updating user alias ${user.alias} -> ${newAlias}",e)
                def res = KuorumUser.collection.update([_id:user.id],['$set':[alias:currentAlias]])
                user.refresh()
                return null
            }
        }
        return user
    }

    KuorumUser updateEmail(KuorumUser user, String email){
        user.email = email
        user.save(flush:true)
        updateKuorumUserOnRest(user)
    }

    List<SearchKuorumUserRSDTO> bestUsers(List<ObjectId> userFiltered = [], Pagination pagination = new Pagination()){
        SearchParams searchParams = new SearchParams(pagination.getProperties().findAll{k,v->k!="class"})
        searchParams.max +=1
        searchParams.setType(SolrType.KUORUM_USER)
        searchParams.filteredUserIds = userFiltered.collect{it.toString()}

        SearchResultsRSDTO results = searchSolrService.searchAPI(searchParams)

        List<SearchKuorumUserRSDTO> politicians = results.data.collect{ SearchKuorumElementRSDTO searchElement ->
            SearchKuorumUserRSDTO searchKuorumUser = null
            if (searchElement.type != SearchTypeRSDTO.KUORUM_USER){
                log.error("Error suggesting user. It is not an user. It is a ${searchElement.type}")
            }else{
                searchKuorumUser = (SearchKuorumUserRSDTO) searchElement
                if (!searchElement.alias){
                    log.warn("Error suggested user [NO ALIAS]:: ${searchElement.name} | ${searchElement.id}" )
                    searchKuorumUser= null
                }
            }
            searchKuorumUser
        }
        // Filtering null
        politicians = politicians.findAll{it}
        return politicians?politicians[0..Math.min(pagination.max-1, politicians.size()-1)]:[]
    }

    void deleteAccount(KuorumUserSession user){

        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.USER,
                params,
                query
        )
    }

    BasicDataKuorumUserRSDTO findBasicUserRSDTO(KuorumUserSession loggedUser){
        return findBasicUserRSDTO(loggedUser.id.toString())
    }
    BasicDataKuorumUserRSDTO findBasicUserRSDTO(String userId, Boolean nullIfNotFound = false){
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [:]
        if (springSecurityService.isLoggedIn()){
            query.put("viewerUid", springSecurityService.principal.id.toString())
        }
        try {
            def apiResponse= restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.USER_DATA,
                    params,
                    query,
                    new TypeReference<BasicDataKuorumUserRSDTO>(){}
            )
            return apiResponse.data
        }catch (Exception e){
            if (nullIfNotFound){
                return null
            }else{
                throw e
            }
        }
    }

    KuorumUserRSDTO findUserRSDTO(KuorumUserSession loggedUser){
        return findUserRSDTO(loggedUser.id.toString())
    }

    KuorumUserRSDTO findUserRSDTO(String userId){
// CALLING API TO REMOVE CONTACT
        Map<String, String> params = [userId: userId]
        Map<String, String> query = [:]
        if (springSecurityService.isLoggedIn()){
            query.put("viewerUid", springSecurityService.principal.id.toString())
        }
        def apiResponse= restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER,
                params,
                query,
                new TypeReference<KuorumUserRSDTO>(){}
        )
        return apiResponse.data

    }

    boolean userDomainValidation(KuorumUserSession user, String ndi, String postalCode, Date birthDate){
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
            return false
        }
    }

    String sendSMSWithValidationCode(KuorumUserSession user, String phoneNumber){
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [phoneNumber:phoneNumber]
        try{
            def apiResponse= restKuorumApiService.post(
                    RestKuorumApiService.ApiMethod.USER_PHONE_DOMAIN_VALIDATION,
                    params,
                    query,
                    null,
                    new TypeReference<String>(){}
            )
            return apiResponse.responseData.str
        }catch (Exception e){
            log.error("Exception validating user: [Excpt: ${e}]")
            return false
        }
    }
    boolean userPhoneDomainValidation(KuorumUserSession user, String hash, String code){
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        UserPhoneValidationDTO userPhoneValidationDTO = new UserPhoneValidationDTO(code:code, hash:hash)
        try{
            def apiResponse= restKuorumApiService.put(
                    RestKuorumApiService.ApiMethod.USER_PHONE_DOMAIN_VALIDATION,
                    params,
                    query,
                    userPhoneValidationDTO,
                    new TypeReference<KuorumUserRSDTO>(){}
            )
            springSecurityService.reauthenticate user.email
            return springSecurityService.authentication.authorities.find{it.authority==UserRoleRSDTO.ROLE_USER_VALIDATED.toString()}
        }catch (Exception e){
            log.error("Exception validating user: [Excpt: ${e?.cause?.cause?.getMessage()}]")
            return false
        }
    }

    @Deprecated
    boolean isUserRegisteredCompletely(KuorumUser user){
        user.personalData.provinceCode != null
    }

    String generateValidAlias(String name, Boolean validEmptyAlias = false){
        String alias = name.replaceAll("[^a-zA-Z0-9]+","")
        alias = alias.substring(0, Math.min(alias.length(), KuorumUser.ALIAS_MAX_SIZE)).toLowerCase()
        if (!alias && validEmptyAlias){
            return ""
        }
        alias = alias?:new Double(Math.floor(Math.random()*Math.pow(10, KuorumUser.ALIAS_MAX_SIZE))).intValue()
        BasicDataKuorumUserRSDTO user = findBasicUserRSDTO(alias, true)
        while (user){
            alias = alias.take(alias.length() -2)
            alias = "${alias}${new Double(Math.floor(Math.random()*100)).intValue()}"
            user = findBasicUserRSDTO(alias, true)
        }
        return alias
    }
}
