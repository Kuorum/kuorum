package kuorum.politician

import kuorum.campaign.Campaign
import kuorum.campaign.CampaignService
import kuorum.campaign.PollCampaignVote
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser
import kuorum.web.commands.campaign.CampaignPollCommand
import org.bson.types.ObjectId

class CampaignController {

    CampaignService campaignService;

    def saveCitizenPriorities(CampaignPollCommand campaignPollCommand) {
        String message = "";
        String error = ""
        if (!campaignPollCommand.validate()){
            message="Invalid parameters"
        }else{
            PollCampaignVote pollCampaign = new PollCampaignVote(
                    politician: campaignPollCommand.politician,
                    userEmail: campaignPollCommand.email,
                    values: campaignPollCommand.causes,
                    campaign: campaignPollCommand.campaign
            )
            try{
                campaignService.savePollResponse(pollCampaign)
                message = g.message(code: 'campaign.vote.successful')
            }catch (KuorumException ke){
                log.error("Error saving vote on the campaign ${campaignPollCommand.campaign}.", ke)
                error = g.message(code: ke.errors.get(0).code)
            }catch (Exception e){
                log.error("Error saving vote on the campaign ${campaignPollCommand.campaign}.", e)
                error = "Internal error: ${e.getMessage()}"
            }

        }
//        redirect mapping:"userShow", params:campaignPollCommand.politician.encodeAsLinkProperties()
        render """{"message": "${message}", "error": "${error}"}"""
    }
}
