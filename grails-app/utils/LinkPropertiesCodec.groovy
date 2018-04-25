import grails.util.Holders
import kuorum.Region
import kuorum.core.model.UserType
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.CampaignRSDTO
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.ProposalRSDTO
import org.kuorum.rest.model.communication.event.EventRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.communication.survey.SurveyRSDTO
import org.kuorum.rest.model.notification.NotificationProposalCommentMentionRSDTO
import org.kuorum.rest.model.notification.NotificationProposalCommentRSDTO
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO
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
            case EventRSDTO:
            case CauseRSDTO:
            case PostRSDTO:
            case SurveyRSDTO:
            case DebateRSDTO:
            case ProposalRSDTO:
            case NotificationProposalCommentRSDTO:
            case NotificationProposalCommentMentionRSDTO:
            case SearchKuorumElementRSDTO:
            case KuorumUser:
                params = prepareParams(target)
                break
            case UserType:
                params = [userTypeUrl: transEnumToUrl(target)]
                break
            case Region:
                params = [regionName:target.name.encodeAsKuorumUrl(), iso3166_2:target.iso3166_2]
                break
            default:
                params = [:]
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
        if(!user.alias){log.warn("Preparing url params without alias => User: ${user}")}
        [
                userAlias:user.alias.toLowerCase()
        ]
    }

    private static def prepareParams(DebateRSDTO debate) {
        if(!debate.user.alias){log.warn("Preparing url params without alias => DebateRSDTO: ${debate.id}")}
        [
                userAlias: debate.user.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(debate),
                campaignId: debate.id
        ]
    }
    private static def prepareParams(EventRSDTO event) {
        if(!event.user.alias){log.warn("Preparing url params without alias => EventRSDTO: ${event.id}")}
        [
                userAlias: event.user.alias.toLowerCase(),
                eventId: event.id
        ]
    }

    private static def prepareParams(CampaignRSDTO campaignRSDTO) {
        if(!campaignRSDTO.user.alias){log.warn("Preparing url params without alias => CampaignRSDTO: ${campaignRSDTO.id}")}
        [
                userAlias: campaignRSDTO.user.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(campaignRSDTO),
                campaignId: campaignRSDTO.id
        ]
    }

    private static def prepareParams(SearchKuorumUserRSDTO searchKuorumUserRSDTO) {
        if(!searchKuorumUserRSDTO.alias){log.warn("Preparing url params without alias => SearchKuorumUserRSDTO: ${searchKuorumUserRSDTO.id}")}
        [
                userAlias: searchKuorumUserRSDTO.alias
        ]
    }
    private static def prepareParams(SearchKuorumElementRSDTO campaignRSDTO) {
        if(!campaignRSDTO.alias){log.warn("Preparing url params without alias => SearchKuorumElementRSDTO: ${campaignRSDTO.id}")}
        [
                userAlias: campaignRSDTO.alias,
                urlTitle: getNameTitleUrl(campaignRSDTO),
                campaignId: campaignRSDTO.id
        ]
    }

    private static def prepareParams(ProposalRSDTO proposalRSDTO) {
        if(!proposalRSDTO.debateUser.alias){log.warn("Preparing url params without alias => ProposalRSDTO.debateUser: ${proposalRSDTO.id}")}
        [
                userAlias: proposalRSDTO.debateUser.alias.toLowerCase(),
                urlTitle: getNameTitleUrl(proposalRSDTO),
                campaignId: proposalRSDTO.debateId
        ]
    }
    private static def prepareParams(NotificationProposalCommentRSDTO notificationProposalCommentRSDTO) {
        if(!notificationProposalCommentRSDTO.debateAlias){log.warn("Preparing url params without alias => NotificationProposalCommentRSDTO.debateUser: ${notificationProposalCommentRSDTO.debateId}")}
        [
                userAlias: notificationProposalCommentRSDTO.debateAlias.toLowerCase(),
                urlTitle: notificationProposalCommentRSDTO.debateTitle.encodeAsKuorumUrl(),
                campaignId: notificationProposalCommentRSDTO.debateId
        ]
    }
    private static def prepareParams(NotificationProposalCommentMentionRSDTO notificationProposalCommentMentionRSDTO) {
        if(!notificationProposalCommentMentionRSDTO.debateAlias){log.warn("Preparing url params without alias => NotificationProposalCommentMentionRSDTO.debateUser: ${notificationProposalCommentMentionRSDTO.debateId}")}
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

    private static String getNameTitleUrl(def debatePost){
        //debatePost is a DebateRSDTO or PostRSDTO or ProposalRSDTO
        String urlText = ""
        if (debatePost instanceof ProposalRSDTO){
            urlText = debatePost.debateTitle
        }else if (debatePost instanceof SearchKuorumElementRSDTO){
            urlText = debatePost.name
        }else{
            urlText = debatePost.title;
        }

        if (!urlText){
            // The post or the debate are not complete and the title is not set [Also is not published]
            urlText = debatePost.name
        }
        return urlText.encodeAsKuorumUrl()
    }


}
