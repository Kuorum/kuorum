package kuorum.core.model.solr

import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO


/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER("fa-user", null),
    NEWSLETTER("fa-envelope", null),
    POST("fa-newspaper", CampaignTypeRSDTO.POST),
    DEBATE("fa-comments", CampaignTypeRSDTO.DEBATE),
    EVENT("fa-calendar-check", null),
    SURVEY("fa-chart-pie", CampaignTypeRSDTO.SURVEY),
    PETITION("fa-microphone", CampaignTypeRSDTO.PETITION),
    PARTICIPATORY_BUDGET("fa-money-bill-alt", CampaignTypeRSDTO.PARTICIPATORY_BUDGET),
    DISTRICT_PROPOSAL("fa-rocket", CampaignTypeRSDTO.DISTRICT_PROPOSAL)

    String faIcon

    CampaignTypeRSDTO campaignType

    SolrType(String faIcon, CampaignTypeRSDTO campaignType) {
        this.faIcon = faIcon
        this.campaignType = campaignType
    }

    String getFaIcon() {
        return faIcon
    }

    static final SolrType safeParse(String rawType){
        SolrType solrType = null
        try{
            SolrType.valueOf(rawType)
        }catch (Exception){
            solrType = null
        }
        return solrType
    }

    static final SolrType getByCampaign(CampaignRSDTO campaignRSDTO){
        if (campaignRSDTO.getEvent()){
            return EVENT
        }
        return SolrType.values().find {it.campaignType == campaignRSDTO.campaignType}
    }
}
