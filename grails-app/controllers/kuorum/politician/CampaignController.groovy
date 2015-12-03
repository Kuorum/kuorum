package kuorum.politician

import kuorum.users.KuorumUser
import kuorum.web.commands.campaign.CampaignPollCommand

class CampaignController {

    def saveCitizenPriorities(CampaignPollCommand campaignPollCommand) {
        if (!campaignPollCommand.validate()){
            KuorumUser politician = KuorumUser.get(campaignPollCommand.politicianId)


        }
    }
}
