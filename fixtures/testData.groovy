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
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)

    kuorumUserService.addFollower(juanjoAlvite,equo)
    kuorumUserService.addFollower(juanjoAlvite,ecologistasEnAccion)
    kuorumUserService.addFollower(juanjoAlvite,politician)
    kuorumUserService.addFollower(juanjoAlvite,peter)
    kuorumUserService.addFollower(peter,equo)
}