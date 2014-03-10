package kuorum.law

import com.mongodb.DBObject
import grails.converters.JSON
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.VoteType
import kuorum.solr.IndexSolrService
import kuorum.users.KuorumUser
import kuorum.web.commands.LawCommand
import org.bson.BSON
import org.bson.BSONObject
import org.grails.datastore.mapping.mongo.engine.MongoEntityPersister

@Transactional
class LawService {

    IndexSolrService indexSolrService

    /**
     * Find the law associated to the #hashtag
     *
     * Return null if not found
     *
     * @param hashtag
     * @return Return null if not found
     */
    Law findLawByHashtag(String hashtag) {
        Law.findByHashtag(hashtag)
    }

    /**
     * An user votes a law and generates all associated events
     *
     * @param law
     * @param kuorumUser
     * @param voteType
     * @return
     */
    LawVote voteLaw(Law law, KuorumUser kuorumUser, VoteType voteType){
        LawVote lawVote = new LawVote()
        lawVote.law = law
        lawVote.kuorumUser = kuorumUser
        lawVote.voteType = voteType
        lawVote.personalData = kuorumUser.personalData
        if (!lawVote.save()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(lawVote)
        }
        lawVote
    }

    Law saveLaw(Law law){
        if (!law.save()){
           throw KuorumExceptionUtil.createExceptionFromValidatable(law)
        }
        indexSolrService.index(law)
        law
    }

    Law updateLaw(Law law){
        //Transaction only with atomic operation on mongo
        // If someone votes while someone saves the law it is possible to lose data for overwriting
        law.mongoUpdate()
        indexSolrService.index(law)
        law
    }

    Law publish(Law law){
        Law.collection.update([_id:law.id], ['$set':[published:Boolean.TRUE]])
        law.refresh()
    }

    Law unpublish(Law law){
        Law.collection.update([_id:law.id], ['$set':[published:Boolean.FALSE]])
        law.refresh()
    }
}
