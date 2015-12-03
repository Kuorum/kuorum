package kuorum.politician

import kuorum.campaign.Campaign
import kuorum.campaign.CampaignService
import kuorum.campaign.PollCampaignVote
import kuorum.users.KuorumUser
import kuorum.web.commands.campaign.CampaignPollCommand
import org.bson.types.ObjectId

class CampaignController {

    CampaignService campaignService;

    def saveCitizenPriorities(CampaignPollCommand campaignPollCommand) {
        if (!campaignPollCommand.validate()){
            flash.error="Not saved"
        }else{
            PollCampaignVote pollCampaign = new PollCampaignVote(
                    politician: campaignPollCommand.politician,
                    userEmail: campaignPollCommand.email,
                    values: campaignPollCommand.causes,
                    campaign: campaignPollCommand.campaign
            )
            try{
                campaignService.savePollResponse(pollCampaign)
                flash.message = "Thank you"
            }catch (Exception e){
               flash.error = "Internal error"
            }

        }
        redirect mapping:"userShow", params:campaignPollCommand.politician.encodeAsLinkProperties()
    }
}
