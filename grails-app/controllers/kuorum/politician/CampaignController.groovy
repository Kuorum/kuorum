package kuorum.politician

import kuorum.campaign.CampaignService
import kuorum.campaign.PollCampaignVote
import kuorum.users.KuorumUser
import kuorum.web.commands.campaign.CampaignPollCommand

class CampaignController {

    CampaignService campaignService;

    def saveCitizenPriorities(CampaignPollCommand campaignPollCommand) {
        KuorumUser politician = KuorumUser.get(campaignPollCommand.politicianId)
        if (!campaignPollCommand.validate()){
            flash.error="Not saved"
        }else{
            PollCampaignVote pollCampaign = new PollCampaignVote(
                    politician: politician,
                    userEmail: campaignPollCommand.email,
                    values: campaignPollCommand.causes
            )
            try{
                campaignService.savePollResponse(pollCampaign)
                flash.message = "Thank you"
            }catch (Exception e){
               flash.error = "Internal error"
            }

        }

        redirect mapping:"userShow", params:politician.encodeAsLinkProperties()
    }
}
