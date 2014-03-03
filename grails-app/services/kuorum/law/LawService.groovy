package kuorum.law

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.VoteType
import kuorum.users.KuorumUser

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
}
