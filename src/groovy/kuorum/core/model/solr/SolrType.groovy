package kuorum.core.model.solr

import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.CampaignTypeRSDTO


/**
 * Grouped types on solr
 */
enum SolrType {

    KUORUM_USER("fa-user", null, true, false),
    NEWSLETTER("fa-envelope", null, false, false),
    POST("fa-newspaper", CampaignTypeRSDTO.POST, true, true),
    DEBATE("fa-comments", CampaignTypeRSDTO.DEBATE, true, true),
    EVENT("fa-calendar-star", null, true, true),
    SURVEY("fa-chart-pie", CampaignTypeRSDTO.SURVEY, true, true),
    PETITION("fa-microphone", CampaignTypeRSDTO.PETITION, true, false),
    PARTICIPATORY_BUDGET("fa-money-bill-alt", CampaignTypeRSDTO.PARTICIPATORY_BUDGET, true, false),
    DISTRICT_PROPOSAL("fa-rocket", CampaignTypeRSDTO.DISTRICT_PROPOSAL, false, false),
    CONTEST("fa-trophy", CampaignTypeRSDTO.CONTEST, true, true),
    CONTEST_APPLICATION("fa-scroll", CampaignTypeRSDTO.CONTEST_APPLICATION, true, false);


    String faIcon

    Boolean searchable

    Boolean header

    CampaignTypeRSDTO campaignType

    SolrType(String faIcon, CampaignTypeRSDTO campaignType, Boolean searchable, Boolean header) {
        this.faIcon = faIcon
        this.campaignType = campaignType
        this.searchable = searchable
        this.header = header
    }

    String getFaIcon() {
        return faIcon
    }

    Boolean getSearchable() {
        return searchable
    }

    Boolean getHeader() {
        return header
    }

    static final SolrType safeParse(String rawType) {
        SolrType solrType = null
        try {
            SolrType.valueOf(rawType)
        } catch (Exception) {
            solrType = null
        }
        return solrType
    }

    static final SolrType getByCampaign(CampaignRSDTO campaignRSDTO) {
        if (campaignRSDTO.getEvent()) {
            return EVENT
        }
        return getByCampaignType(campaignRSDTO.campaignType)
    }

    static final SolrType getByCampaignType(CampaignTypeRSDTO campaignType) {
//        if (campaignType..getEvent()){
//            return EVENT
//        }
        if (campaignType == null) {
            return null;
        }
        return SolrType.values().find { it.toString() == campaignType.toString() }
    }
}
