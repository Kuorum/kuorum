import kuorum.post.Sponsor
import kuorum.users.KuorumUser

include "regions"
include "institutions"
include "parliamentaryGroups"
include "authoritiesData"
include "laws/*"
include "users/*"
include "posts/*"

fixture{

}
post {

    postService.savePost(abortoPurpose1)
    cluckService.createCluck(abortoPurpose1, juanjoAlvite)
    cluckService.createCluck(abortoPurpose1, ecologistasEnAccion)
    abortoPurpose1.refresh() // Is necesary because fixture sets the last cluck created to abortoPurpose1.firstCluck. I don't know why
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)


    kuorumUserService.createFollower(juanjoAlvite,equo)
    kuorumUserService.createFollower(juanjoAlvite,ecologistasEnAccion)
    kuorumUserService.createFollower(juanjoAlvite,politician)
    kuorumUserService.createFollower(juanjoAlvite,peter)
    kuorumUserService.createFollower(peter,equo)

    kuorumUserService.createFollower(noe,politician)

}