import grails.util.Holders
import kuorum.Region
import kuorum.core.model.UserType
import kuorum.project.Project
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.ProposalRSDTO
import org.kuorum.rest.model.communication.event.EventRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.BasicParticipatoryBudgetRSDTO
import org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.notification.NotificationProposalCommentMentionRSDTO
import org.kuorum.rest.model.notification.NotificationProposalCommentRSDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchDistrictProposalRSDTO
import org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO
import org.kuorum.rest.model.tag.CauseRSDTO

/**
 * Created by iduetxe on 24/03/14.
 */
class LinkPropertiesCodec {

    private static final Integer NUM_CHARS_URL_POST_TITLE = 35

    static encode = {target->

        def params = [:]
        switch (target) {
            case UserType:
                params = [userTypeUrl: transEnumToUrl(target)]
                break
            case Region:
                params = [regionName:target.name.encodeAsKuorumUrl(), iso3166_2:target.iso3166_2]
                break
            default:
                params = prepareParams(target)
        }
        params
    }

    static decode = {target->
        //TODO
    }

    private static def prepareParams(CauseRSDTO cause){
        [causeName:cause.getName()]
    }

    private static def prepareParams(Project project){
        KuorumUser owner = project.owner
        [
                hashtag: project.hashtag.decodeHashtag(),
                userAlias: owner.alias
        ]
    }

    private static def prepareParams(KuorumUser user){
        [
                userAlias:user.alias.toLowerCase()
        ]
    }
    private static def prepareParams(KuorumUserSession user){
        [
                userAlias:user.alias.toLowerCase()
        ]
    }

    private static def prepareParams(BasicDataKuorumUserRSDTO user){
        [
                userAlias:user.alias.toLowerCase()
        ]
    }

    private static def prepareParams(DebateRSDTO debate) {
        [
                userAlias: debate.user.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(debate),
                campaignId: debate.id
        ]
    }
    private static def prepareParams(EventRSDTO event) {
        [
                userAlias: event.user.alias.toLowerCase(),
                eventId: event.id
        ]
    }

    private static def prepareParams(CampaignRSDTO campaignRSDTO) {
        [
                userAlias: campaignRSDTO.user.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(campaignRSDTO),
                campaignId: campaignRSDTO.id
        ]
    }

    private static def prepareParams(BasicParticipatoryBudgetRSDTO campaignRSDTO) {
        [
                userAlias: campaignRSDTO.userAlias.toLowerCase(),
                urlTitle: getNameTitleUrl(campaignRSDTO),
                campaignId: campaignRSDTO.id
        ]
    }

    private static def prepareParams(DistrictProposalRSDTO districtProposalRSDTO) {
        [
                userAlias: districtProposalRSDTO.participatoryBudget.userAlias.toLowerCase(),
                participatoryBudgetTitle: districtProposalRSDTO.participatoryBudget.title.encodeAsKuorumUrl(),
                participatoryBudgetId: districtProposalRSDTO.participatoryBudget.id,
                urlTitle: getNameTitleUrl(districtProposalRSDTO),
                campaignId: districtProposalRSDTO.id
        ]
    }
    private static def prepareParams(SearchDistrictProposalRSDTO districtProposalRSDTO) {
        [
                userAlias: districtProposalRSDTO.participatoryBudget.userAlias.toLowerCase(),
                participatoryBudgetTitle: districtProposalRSDTO.participatoryBudget.title.encodeAsKuorumUrl(),
                participatoryBudgetId: districtProposalRSDTO.participatoryBudget.id,
                urlTitle: getNameTitleUrl(districtProposalRSDTO),
                campaignId: districtProposalRSDTO.id
        ]
    }

    private static def prepareParams(SearchKuorumUserRSDTO searchKuorumUserRSDTO) {
        [
                userAlias: searchKuorumUserRSDTO.alias
        ]
    }
    private static def prepareParams(SearchKuorumElementRSDTO campaignRSDTO) {
        [
                userAlias: campaignRSDTO.alias,
                urlTitle: getNameTitleUrl(campaignRSDTO),
                campaignId: campaignRSDTO.id
        ]
    }

    private static def prepareParams(ProposalRSDTO proposalRSDTO) {
        [
                userAlias: proposalRSDTO.debateUser.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(proposalRSDTO),
                campaignId: proposalRSDTO.debateId
        ]
    }
    private static def prepareParams(NotificationProposalCommentRSDTO notificationProposalCommentRSDTO) {
        [
                userAlias: notificationProposalCommentRSDTO.debateAlias.toLowerCase(),
                urlTitle: notificationProposalCommentRSDTO.debateTitle.encodeAsKuorumUrl(),
                campaignId: notificationProposalCommentRSDTO.debateId
        ]
    }
    private static def prepareParams(NotificationProposalCommentMentionRSDTO notificationProposalCommentMentionRSDTO) {
        [
                userAlias: notificationProposalCommentMentionRSDTO.debateAlias.toLowerCase(),
                urlTitle: notificationProposalCommentMentionRSDTO.debateTitle.encodeAsKuorumUrl(),
                campaignId: notificationProposalCommentMentionRSDTO.debateId
        ]
    }

    private static String translate(String txt){
        def messageSource =Holders.getApplicationContext().getBean("messageSource")
        messageSource.getMessage(txt,null,"default", new Locale("ES_es"))
    }

    private static String transEnumToUrl(UserType userType){
        /*
        This returns have to match with:
            citizienShow:      "/ciudadanos/$urlName-$id"
            organizacionShow:  "/organizaciones/$urlName-$id" (controller: "kuorumUser", action: "showOrganization")
            politicianShow:    "/politicos/$urlName-$id"      (controller: "kuorumUser", action: "showPolitician")

         */
        switch (userType){
            case UserType.PERSON: return "ciudadanos"
            case UserType.ORGANIZATION: return "organizaciones"
            case UserType.POLITICIAN: return "politicos"
            case UserType.CANDIDATE: return "candidato"
        }
    }

    private static String getNameTitleUrl(def campaign){
        //debatePost is a DebateRSDTO or PostRSDTO or ProposalRSDTO
        String urlText = ""
        if (campaign instanceof ProposalRSDTO){
            urlText = campaign.debateTitle
        }else if (campaign instanceof SearchKuorumElementRSDTO){
            urlText = campaign.name
        }else{
            urlText = campaign.title;
        }

        if (!urlText){
            // The post or the debate are not complete and the title is not set [Also is not published]
            urlText = campaign.name
        }
        return urlText.encodeAsKuorumUrl()
    }


}
