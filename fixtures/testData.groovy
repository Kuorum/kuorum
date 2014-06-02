import kuorum.core.model.CommitmentType
import kuorum.core.model.VoteType
import kuorum.post.PostComment
import kuorum.post.Sponsor

include "testBasicData"

fixture{

}
post {
    postService.publishPost(abortoPurpose2)
    postService.publishPost(parquesNacionalesPurpose1)
    postService.publishPost(codigoPenalQuestion1)

    postService.savePost(abortoPurpose1, abortoPurpose1.law, abortoPurpose1.owner)
    postService.publishPost(abortoPurpose1)

    PostComment deabate1_abortoPurpose1 = new PostComment(
            text:"Politician Debate 1",
            dateCreated:new Date() -1 ,
            deleted:Boolean.FALSE,
            moderated:Boolean.FALSE,
            kuorumUser:politician)
    postService.addDebate(abortoPurpose1, deabate1_abortoPurpose1)

    PostComment deabate2_abortoPurpose1 = new PostComment(
        text:"Peter Debate 1",
        dateCreated:new Date(),
        deleted:Boolean.FALSE,
        moderated:Boolean.FALSE,
        kuorumUser:peter)

    postService.addDebate(abortoPurpose1, deabate2_abortoPurpose1)


    cluckService.createCluck(abortoPurpose1, juanjoAlvite)
    cluckService.createCluck(abortoPurpose1, ecologistasEnAccion)
    abortoPurpose1.refresh() // Is necesary because fixture sets the last cluck created to abortoPurpose1.firstCluck. I don't know why
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)
    Sponsor sponsor2 = new Sponsor(kuorumUser: ecologistasEnAccion, amount: 7)
    postService.sponsorAPost(abortoPurpose1, sponsor2)


    postService.savePost(parquesNacionalesPurpose2, parquesNacionalesPurpose2.law, parquesNacionalesPurpose2.owner)
    postService.publishPost(parquesNacionalesPurpose2)
    parquesNacionalesPurpose2.refresh()
    cluckService.createCluck(parquesNacionalesPurpose2, juanjoAlvite)
    postService.defendPost(parquesNacionalesPurpose2, CommitmentType.ADDED_AS_AMENDMENT, politician)


    kuorumUserService.createFollower(juanjoAlvite,equo)
    kuorumUserService.createFollower(juanjoAlvite,ecologistasEnAccion)
    kuorumUserService.createFollower(juanjoAlvite,politician)
    kuorumUserService.createFollower(juanjoAlvite,peter)
    kuorumUserService.createFollower(peter,equo)
    kuorumUserService.createFollower(peter,juanjoAlvite)

    kuorumUserService.createFollower(noe,politician)

    postVoteService.votePost(parquesNacionalesPurpose2,juanjoAlvite, false )
    postVoteService.votePost(parquesNacionalesPurpose2,admin, false )
    postVoteService.votePost(parquesNacionalesPurpose2,carmen, false )
    postVoteService.votePost(parquesNacionalesPurpose2,ecologistasEnAccion, false )
    postVoteService.votePost(parquesNacionalesPurpose2,noe, false )
    postVoteService.votePost(parquesNacionalesPurpose2,politician, true )
    postVoteService.votePost(parquesNacionalesPurpose2,peter, false )


    postService.defendPost(codigoPenalQuestion1, CommitmentType.ASKED_ON_CONGRESS, politician)
    postService.victory(codigoPenalQuestion1, codigoPenalQuestion1.owner)

    lawService.voteLaw(aborto,carmen,VoteType.NEGATIVE)
    lawService.voteLaw(codigoPenal,carmen,VoteType.NEGATIVE)
    lawService.voteLaw(parquesNacionales,carmen,VoteType.POSITIVE)
    lawService.voteLaw(prohibicionFraking,carmen,VoteType.NEGATIVE)

    lawService.voteLaw(aborto,noe,VoteType.NEGATIVE)
    lawService.voteLaw(codigoPenal,noe,VoteType.POSITIVE)
    lawService.voteLaw(parquesNacionales,noe,VoteType.POSITIVE)
    lawService.voteLaw(prohibicionFraking,noe,VoteType.POSITIVE)

    lawService.voteLaw(aborto,equo,VoteType.NEGATIVE)
    lawService.voteLaw(codigoPenal,equo,VoteType.POSITIVE)
    lawService.voteLaw(parquesNacionales,equo,VoteType.POSITIVE)
    lawService.voteLaw(prohibicionFraking,equo,VoteType.NEGATIVE)

    lawService.voteLaw(aborto,ecologistasEnAccion,VoteType.ABSTENTION)
    lawService.voteLaw(codigoPenal,ecologistasEnAccion,VoteType.ABSTENTION)
    lawService.voteLaw(parquesNacionales,ecologistasEnAccion,VoteType.POSITIVE)
    lawService.voteLaw(prohibicionFraking,ecologistasEnAccion,VoteType.NEGATIVE)
}