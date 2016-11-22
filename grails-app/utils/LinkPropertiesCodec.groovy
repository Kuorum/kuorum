import grails.util.Holders
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrProject
import kuorum.core.model.solr.SolrPost
import kuorum.project.Project
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.kuorum.rest.model.tag.CauseRSDTO

/**
 * Created by iduetxe on 24/03/14.
 */
class LinkPropertiesCodec {

    private static final Integer NUM_CHARS_URL_POST_TITLE = 35

    static encode = {target->

        def params = [:]
        switch (target){
            case CauseRSDTO:
                params = prepareParams(target);
                break
            case Project:
            case SolrProject:
//                Project project = (Project) target
                params = prepareParams(target);
                break;
            case SolrPost:
                params = prepareParams(target)
                break;
            case Post:
                params = prepareParams(target.project)
                params+= [
                        postId:target.id,
                        postBrief:target.title[0..Math.min(NUM_CHARS_URL_POST_TITLE, target.title.size()-1)].encodeAsKuorumUrl()
                ]
                break;
            case KuorumUser:
            case SolrKuorumUser:
                params = prepareParams(target)
                break;
            case UserType:
                params = [userTypeUrl: transEnumToUrl(target)]
                break;
            case Region:
                params = [regionName:target.name.encodeAsKuorumUrl(), iso3166_2:target.iso3166_2]
                break;
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
        [
                userAlias:user.alias.toLowerCase()
        ]
    }

    private static def prepareParams(SolrKuorumUser user){
        //userTypeUrl is the name that match with UrlMappings to redirect to correct action
//        UserType userType = UserType.valueOf(user.subType.toString())
//        String userTypeUrl = transEnumToUrl(userType)
//        [
//                id: user.id.toString(),
//                urlName:user.name.encodeAsKuorumUrl(),
//                userTypeUrl:userTypeUrl.encodeAsKuorumUrl(),
//        ]
        [
                userAlias:user.name
        ]
    }

    private static def prepareParams(SolrProject project){
        String commissionName = translate("${CommissionType.canonicalName}.${project.commissions.first()}")
        [
                hashtag: project.hashtag.decodeHashtag(),
                regionName:project.regionName.encodeAsKuorumUrl(),
//                institutionName:project.institutionName.encodeAsKuorumUrl(),
                commission:commissionName.encodeAsKuorumUrl()
        ]
    }

    private static def prepareParams(SolrPost post){
        String commissionName = translate("${CommissionType.canonicalName}.${post.commissions.first()}")
        [
                hashtag: post.hashtagProject.decodeHashtag(),
                regionName:post.regionName.encodeAsKuorumUrl(),
//                institutionName:post.institutionName.encodeAsKuorumUrl(),
                commission:commissionName.encodeAsKuorumUrl(),
                postId:post.id,
                postBrief:post.name[0..Math.min(NUM_CHARS_URL_POST_TITLE, post.name.size()-1)].encodeAsKuorumUrl()
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


}
