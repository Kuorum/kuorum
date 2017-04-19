package kuorum.politician

import kuorum.post.PostService
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.DebateService

class CampaignController {

    PostService postService;
    DebateService debateService;

    def findLiUserCampaigns(String userId){
        KuorumUser user = KuorumUser.get(new ObjectId(userId));
        List<PostRSDTO> posts = postService.findAllPosts(user).findAll{it.newsletter.status==CampaignStatusRSDTO.SENT};
        List<DebateRSDTO> debates = debateService.findAllDebates(user).findAll{it.newsletter.status==CampaignStatusRSDTO.SENT};
        render template: '/campaigns/cards/campaignsList', model: [posts:posts, debates:debates, showAuthor:true]

    }
}
