package kuorum.post

import com.mongodb.*
import grails.transaction.Transactional
import kuorum.RegionService
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.search.Pagination
import kuorum.law.Law
import kuorum.users.KuorumUser

@Transactional
class CluckService {

    def notificationService

    RegionService regionService;

    List<Cluck> lawClucks(Law law, Pagination pagination = new Pagination()) {
        Cluck.findAllByLawAndIsFirstCluck(law, Boolean.TRUE,[max: pagination.max, sort: "dateCreated", order: "desc", offset: pagination.offset])
    }

    private static Integer POOL_COLLECTIONS_MAX=10;
    private static Integer ELEMENTS_FOR_PROCESS_IN_MAPREDUCE=10000;
    private Integer actualCollectionIdx = 0;
    private synchronized getCollectionName(String prefixCollectionName){
        //TODO: THINK THIS. IS SO HARDCORE
        actualCollectionIdx++
        if (actualCollectionIdx%POOL_COLLECTIONS_MAX==0){
            actualCollectionIdx=0
        }
        "${prefixCollectionName}_$actualCollectionIdx"
    }

    List<Cluck> dashboardClucks(KuorumUser kuorumUser, Pagination pagination = new Pagination()){

        def userList = kuorumUser.following
        userList << kuorumUser.id
        DBObject usersInList = new BasicDBObject('$in', userList)
        DBObject filter = new BasicDBObject("owner", usersInList)
        DBObject regionInList = new BasicDBObject('$in',regionService.findUserRegions(kuorumUser).collect{it.iso3166_2});
        filter.append("region.iso3166_2", regionInList)
        String mapDashboardClucks = """
function(){
    if (this.cluckAction == "${CluckAction.DEBATE}"){
        emit(this.post,{val:2,cluck:this._id, lastUpdated:this.lastUpdated});
    }else if (this.cluckAction == "${CluckAction.DEFEND}"){
        emit(this.post,{val:3,cluck:this._id, lastUpdated:this.lastUpdated})
    }else if (this.cluckAction == "${CluckAction.VICTORY}"){
        emit(this.post,{val:3,cluck:this._id, lastUpdated:this.lastUpdated})
    }else{
        emit(this.post,{val:1,cluck:this._id, lastUpdated:this.lastUpdated})
    }
}
        """

        String reduceDashBoardClucks = """
function(key, values){
    var relevantDataCluck = values.pop()
    values.forEach(function(newRelevantCluck){
        if (newRelevantCluck.val > relevantDataCluck.val){
            relevantDataCluck = newRelevantCluck
        }else if (newRelevantCluck.val == relevantDataCluck.val){
            if (newRelevantCluck.lastUpdated < relevantDataCluck.lastUpdated){
                relevantDataCluck = newRelevantCluck
            }
        }
    })
    return relevantDataCluck;

}
"""
        String collectionName = getCollectionName("dashboard")
        MapReduceCommand dashboardClucks = new MapReduceCommand(
                Cluck.collection,
                mapDashboardClucks,
                reduceDashBoardClucks ,
                collectionName,
                MapReduceCommand.OutputType.REPLACE,filter);

        //Reduce de amount of initial data for reduce time processing only getting first ELEMENTS_FOR_PROCESS_IN_MAPREDUCE elements
        dashboardClucks.setLimit(ELEMENTS_FOR_PROCESS_IN_MAPREDUCE)
        dashboardClucks.setSort(new BasicDBObject('lastUpdated',-1))

        MapReduceOutput result = Cluck.collection.mapReduce(dashboardClucks)
        DBCursor cursor = result.getOutputCollection().find()

        DBObject sort = new BasicDBObject('value.lastUpdated',-1);
        cursor.sort(sort)
        cursor.limit(pagination.max.intValue())
        cursor.skip(pagination.offset.intValue())

        cursor.collect {Cluck.get(it.value.cluck)}

    }

    List<KuorumUser> findPostCluckers(Post post, Pagination pagination=new Pagination()){
        def cluks = Cluck.findAllByPost(post, [max:pagination.max, offset: pagination.offset, sort: "id", order: "desc" ])
        cluks.collect{it.owner}
    }

    List<Cluck> userClucks(KuorumUser kuorumUser, Pagination pagination = new Pagination()){
        Cluck.findAllByOwner(kuorumUser,[max:pagination.max, offset:pagination.offset, sort: 'lastUpdated', order: 'desc'])
    }

    Cluck createCluck(Post post, KuorumUser kuorumUser){

        if (isAllowedToCluck(post, kuorumUser)){
            Cluck cluck = new Cluck(
                    owner: kuorumUser,
                    postOwner: post.owner,
                    law: post.law,
                    region: post.law.region,
                    cluckAction: CluckAction.CLUCK
            )

            cluck.post = post
            if (!cluck.save()){
                KuorumExceptionUtil.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
            }
            notificationService.sendCluckNotification(cluck)
            //Atomic operation - non transactional
            post.save(flush:true)
            Post.collection.update([_id:post.id],[$inc:[numClucks:1]])
            post.refresh()

            cluck
        }else{
            Cluck.findByPostAndOwner(post, kuorumUser)
        }

    }

    Cluck createActionCluck(Post post, KuorumUser kuorumUser, CluckAction cluckAction){

        Cluck cluck =  Cluck.findByPostAndOwner(post, kuorumUser)
        if (!cluck){
            cluck = new Cluck(
                    owner: kuorumUser,
                    postOwner: post.owner,
                    law: post.law,
                    region: post.law.region,
                    cluckAction: cluckAction
            )
        }
        cluck.lastUpdated(new Date());

        if (!cluck.save()){
            KuorumExceptionUtil.createExceptionFromValidatable(cluck, "Error salvando el kakareo del post ${post}")
        }
        cluck
    }


    Boolean isAllowedToCluck(Post post, KuorumUser user){
        user && Cluck.countByPostAndOwner(post,user) == 0
    }
}
