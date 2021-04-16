package payment.campaign

import com.ecwid.mailchimp.method.v1_3.campaign.CampaignType
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
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

    /**
     * Search all campaigns (RSDTO type) of an user
     * @param user
     * @return
     */
    List<RSDTO> findAll(KuorumUserSession user)
    RSDTO find(KuorumUserSession user, Long campaignId)
    RSDTO find(String userId, Long campaignId)


    /**
     * Creates a new campaign using data from CAMPAIGN campaignId
     * @param userId
     * @param campaignId
     * @return
     */
    RSDTO copy(KuorumUserSession user, Long campaignId)
    RSDTO copy(String userId, Long campaignId) throws KuorumException

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