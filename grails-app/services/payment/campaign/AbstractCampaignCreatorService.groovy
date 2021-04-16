package payment.campaign


import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO

abstract class AbstractCampaignCreatorService<RSDTO extends CampaignRSDTO, RDTO extends CampaignRDTO> implements CampaignCreatorService<RSDTO, RDTO> {

    RestKuorumApiService restKuorumApiService


    @Override
    RSDTO copy(KuorumUserSession user, Long campaignId) {
        if (!user) {
            return null
        }
        return copy(user.getId().toString(), campaignId)
    }

    @Override
    RSDTO copy(String userId, Long campaignId) throws KuorumException {
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        RestKuorumApiService.ApiMethod apiMethod =  getCopyApiMethod();
        def response
        try {
            response = restKuorumApiService.post(
                    apiMethod,
                    params,
                    query,
                    null,
                    getRsdtoType()
            )
        } catch (Exception ex) {
            log.error("""Error copying campaign: -> ${ex.message}""")
            throw new KuorumException("No ha sido posible copiar la campaña", "campaign.copy.error")
        }

        response.data ?: null
    }

    protected abstract TypeReference<RSDTO> getRsdtoType();

    protected abstract RestKuorumApiService.ApiMethod getCopyApiMethod();

}
