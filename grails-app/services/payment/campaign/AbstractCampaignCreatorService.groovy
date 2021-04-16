package payment.campaign

import com.ecwid.mailchimp.method.v1_3.campaign.CampaignType
import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.mail.KuorumMailService
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import kuorum.web.commands.payment.survey.SurveyReportType
import org.kuorum.rest.model.communication.CampaignRDTO
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.survey.*
import org.kuorum.rest.model.communication.survey.answer.QuestionAnswerRDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

abstract class AbstractCampaignCreatorService<RSDTO extends CampaignRSDTO, RDTO extends CampaignRDTO> implements CampaignCreatorService<SurveyRSDTO, SurveyRDTO> {

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
        Map<String, String> params = [userId: "90090", campaignId: campaignId.toString()]
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

        response.data ?: null;
    }

    protected abstract TypeReference<RSDTO> getRsdtoType();

    protected abstract RestKuorumApiService.ApiMethod getCopyApiMethod();

}
