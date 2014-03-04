package kuorum.law

import com.mongodb.DBObject
import grails.converters.JSON
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.VoteType
import kuorum.users.KuorumUser
import kuorum.web.commands.LawCommand
import org.bson.BSON
import org.bson.BSONObject
import org.grails.datastore.mapping.mongo.engine.MongoEntityPersister

@Transactional
class LawService {

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
        law.save()
    }

    Law updateLaw(Law law){
        //Transaction only with atomic operation on mongo
        // If someone votes while someone saves the law it is possible to lose data for overwriting
        law.mongoUpdate()
        law
    }
}
