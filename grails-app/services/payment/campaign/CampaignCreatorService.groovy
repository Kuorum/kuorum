package payment.campaign

import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignRDTO

interface CampaignCreatorService<RSDTO extends CampaignRSDTO, RDTO extends CampaignRDTO> {

    /**
     * Creates or updates a Campaign depending on postId
     * @param user
     * @param rdto
     * @param postId
     * @return
     */
    RSDTO save(KuorumUser user, RDTO rdto, Long campaignId)

    RSDTO find(KuorumUser user, Long campaignId)
    /**
     * Maps RSDTO to RDTO
     * @param rsdto
     * @return
     */
    RDTO map(RSDTO rsdto)


}