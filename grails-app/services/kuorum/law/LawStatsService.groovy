package kuorum.law

import grails.transaction.Transactional
import kuorum.Region
import kuorum.core.model.Law.LawBasicStats
import kuorum.core.model.Law.LawRegionStats
import kuorum.post.Post

@Transactional
class LawStatsService {

    LawBasicStats calculateLawStats(Law law){
        //TODO: Cache this method
        LawBasicStats lawStats = new LawBasicStats()
        Post post = Post.findByLaw(law,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
        lawStats.lastActivity = post?.dateCreated

        lawStats.numPosts = Post.collection.count([law:law.id,published:true])
        lawStats.numPostsWithManyVotes = Post.collection.count([law:law.id,published:true, numVotes:['$gt':10]])

        lawStats
    }

    LawRegionStats calculateRegionStats(Law law, Region region){
        //TODO: Cache this method
        LawRegionStats stats = new LawRegionStats()
//        LawVote.collection.
    }
}
