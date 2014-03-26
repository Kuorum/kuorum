package kuorum.law

import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.VoteType
import kuorum.solr.IndexSolrService
import kuorum.users.GamificationService
import kuorum.users.KuorumUser

@Transactional
class LawService {

    IndexSolrService indexSolrService
    GamificationService gamificationService

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
     * @param user
     * @param voteType
     * @return
     */
    LawVote voteLaw(Law law, KuorumUser user, VoteType voteType){
        LawVote lawVote = LawVote.findByKuorumUserAndLaw(user, law)
        if (lawVote){
            lawVote = changeLawVote(law, user, voteType, lawVote)
        }else if (!lawVote){
            lawVote = createLawVote(law,user,voteType)
            gamificationService.lawVotedAward(user, law)
        }
        lawVote
    }

    private LawVote changeLawVote(Law law, KuorumUser user, VoteType voteType, LawVote lawVote){
        VoteType orgVoteType = lawVote.voteType
        if (orgVoteType != voteType){
            lawVote.voteType = voteType
            lawVote.personalData = user.personalData
            if (!lawVote.save()){
                throw KuorumExceptionUtil.createExceptionFromValidatable(lawVote)
            }

            switch (orgVoteType){
                case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':-1]]); break;
                case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':-1]]); break;
                case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':-1]]); break;
                default: break;
            }

            switch (voteType){
                case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':1]]); break;
                case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':1]]); break;
                case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':1]]); break;
                default: break;
            }
            law.refresh()
        }
        lawVote
    }
    private LawVote createLawVote(Law law, KuorumUser user, VoteType voteType){
        LawVote lawVote = new LawVote()
        lawVote.law = law
        lawVote.kuorumUser = user
        lawVote.voteType = voteType
        lawVote.personalData = user.personalData
        if (!lawVote.save()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(lawVote)
        }
        switch (voteType){
            case VoteType.POSITIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.yes':1]]); break;
            case VoteType.ABSTENTION:   Law.collection.update([_id:law.id],['$inc':['peopleVotes.abs':1]]); break;
            case VoteType.NEGATIVE:     Law.collection.update([_id:law.id],['$inc':['peopleVotes.no':1]]); break;
            default: break;
        }
        law.refresh()
        lawVote
    }

    Law saveLaw(Law law){
        if (!law.save()){
           throw KuorumExceptionUtil.createExceptionFromValidatable(law)
        }
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
        indexSolrService.index(law)
    }

    Law unpublish(Law law){
        Law.collection.update([_id:law.id], ['$set':[published:Boolean.FALSE]])
        law.refresh()
    }

    Law closeLaw(Law law){
        Law.collection.update([_id:law.id], ['$set':[open:Boolean.FALSE]])
        law.refresh()
    }
}
