import kuorum.post.Sponsor
import kuorum.users.KuorumUser

include "regions"
include "institutions"
include "parliamentaryGroups"
include "authoritiesData"
include "laws/*"
include "users/*"
include "posts/*"
//include "clucks/*"

fixture{

}
post {

    postService.savePost(abortoPurpose1)
    Sponsor sponsor = new Sponsor(kuorumUser: equo, amount: 5)
    postService.sponsorAPost(abortoPurpose1, sponsor)
    //cluckService.createCluck(abortoPurpose1,peter)

    juanjoAlvite.following = [equo]
    juanjoAlvite.save()
}