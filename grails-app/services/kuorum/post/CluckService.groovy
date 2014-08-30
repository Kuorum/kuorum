package kuorum.post

import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class CluckService {

    def notificationService

    List<Cluck> lawClucks(Law law, Pagination pagination = new Pagination()) {
        Cluck.findAllByLawAndIsFirstCluck(law, Boolean.TRUE,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    List<Cluck> dashboardClucks(KuorumUser kuorumUser, Pagination pagination = new Pagination()){

        def criteria = Cluck.createCriteria()
        def userList = kuorumUser.following.collect{KuorumUser.load(it)}
        userList << kuorumUser
        def result = criteria.list(max:pagination.max, offset:pagination.offset) {
            or {
                'in'("owner",userList)
                'in'("defendedBy",userList)
                'in'("sponsors.kuorumUserId",userList)
            }
            order("lastUpdated","desc")
        }
        result

    }

    List<KuorumUser> findPostCluckers(Post post, Pagination pagination=new Pagination()){
        def cluks = Cluck.findAllByPost(post, [max:pagination.max, offset: pagination.offset, sort: "id", order: "desc" ])
        cluks.collect{it.owner}
    }

    List<Cluck> userClucks(KuorumUser kuorumUser, Pagination pagination = new Pagination()){

        def criteria = Cluck.createCriteria()
        def result = criteria.list(max:pagination.max, offset:pagination.offset) {
            or {
                'eq'("owner",kuorumUser)
                'eq'("defendedBy",kuorumUser)
                'eq'("sponsors.kuorumUserId",kuorumUser.id)
            }
            order("lastUpdated","desc")
        }
        result

    }

    Cluck createCluck(Post post, KuorumUser kuorumUser){

        if (isAllowedToCluck(post, kuorumUser)){
            Cluck cluck = new Cluck(
                    owner: kuorumUser,
                    postOwner: post.owner,
                    post: post,
                    law: post.law,
            )
            Cluck firstCluck = null
            if (post.owner == kuorumUser){
                cluck.isFirstCluck = Boolean.TRUE
                firstCluck = cluck
            }else{
                firstCluck = post.firstCluck
            }
            if (!cluck.save()){
                KuorumExceptionUtil.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
            }
            notificationService.sendCluckNotification(cluck)
            //Atomic operation - non transactional
            post.save(flush:true)
            Post.collection.update([_id:post.id],[$inc:[numClucks:1], $set:[firstCluck:firstCluck.id]]) //The first cluck is set again because GORM overwrite with the new cluck
            post.refresh()

            cluck
        }else{
            Cluck.findByPostAndOwner(post, kuorumUser)
        }

    }


    Boolean isAllowedToCluck(Post post, KuorumUser user){
        user && Cluck.countByPostAndOwner(post,user) == 0
    }
}
