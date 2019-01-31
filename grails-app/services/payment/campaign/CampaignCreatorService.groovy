package payment.campaign

import kuorum.register.KuorumUserSession
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

interface CampaignCreatorService<RSDTO extends CampaignRSDTO, RDTO extends CampaignRDTO> {

    /**
     * Creates or updates a Campaign depending on campaignId
     * @param user
     * @param rdto
     * @param campaignId
     * @return
     */
    RSDTO save(KuorumUserSession user, RDTO rdto, Long campaignId)

    /**
     * Removes the campaign
     * @param user
     * @param campaignId
     * @return
     */
    void remove(KuorumUserSession user, Long campaignId)

    RSDTO find(KuorumUserSession user, Long campaignId)
    RSDTO find(String userId, Long campaignId)
    /**
     * Maps RSDTO to RDTO
     * @param rsdto
     * @return
     */
    RDTO map(RSDTO rsdto)

    /**
     * Builds the model and the view for the campaign.
     *
     * It is a helper for the campaign controller
     * @param rsdto
     * @return
     */
    def buildView(RSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params)


}