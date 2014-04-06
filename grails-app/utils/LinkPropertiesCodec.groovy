import grails.util.Holders
import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.core.model.UserType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrLaw
import kuorum.core.model.solr.SolrPost
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 24/03/14.
 */
class LinkPropertiesCodec {


    static encode = {target->

        def params = [:]
        switch (target){
            case Law:
            case SolrLaw:
//                Law law = (Law) target
                params = prepareParams(target);
                break;
            case SolrPost:
                params = prepareParams(target)
                break;
            case Post:
                params = prepareParams(target.law)
                String postTypeName =   translate("${PostType.canonicalName}.${target.postType}")
                params+= [
                        postId:target.id,
                        postTypeUrl:postTypeName.encodeAsKuorumUrl(),
                ]
                break;
            case KuorumUser:
            case SolrKuorumUser:
                params = prepareParams(target)
                break;
            default:
                params = [:]
        }
        params
    }

    static decode = {target->
        //TODO
    }


    private static def prepareParams(Law law){
        String commissionName = translate("${CommissionType.canonicalName}.${law.commissions.first()}")
        [
                hashtag: law.hashtag.decodeHashtag(),
                regionName:law.region.name.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl(),
        ]
    }

    private static def prepareParams(KuorumUser user){
        //userTypeUrl is the name that match with UrlMappings to redirect to correct action
        String userTypeUrl = transEnumToUrl(user.userType)
        [
                id: user.id.toString(),
                urlName:user.name.encodeAsKuorumUrl(),
                userTypeUrl:userTypeUrl.encodeAsKuorumUrl(),
        ]
    }

    private static def prepareParams(SolrKuorumUser user){
        //userTypeUrl is the name that match with UrlMappings to redirect to correct action
        UserType userType = UserType.valueOf(user.subType.toString())
        String userTypeUrl = transEnumToUrl(userType)
        [
                id: user.id.toString(),
                urlName:user.name.encodeAsKuorumUrl(),
                userTypeUrl:userTypeUrl.encodeAsKuorumUrl(),
        ]
    }

    private static def prepareParams(SolrLaw law){
        String commissionName = translate("${CommissionType.canonicalName}.${law.commissions.first()}")
        [
                hashtag: law.hashtag.decodeHashtag(),
                regionName:law.regionName.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl()
        ]
    }

    private static def prepareParams(SolrPost post){
        String commissionName = translate("${CommissionType.canonicalName}.${post.commissions.first()}")
        String postTypeName =   translate("${PostType.canonicalName}.${post.subType}")
        [
                hashtag: post.hashtagLaw.decodeHashtag(),
                regionName:post.regionName.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl(),
                postId:post.id,
                postTypeUrl:postTypeName.encodeAsKuorumUrl(),
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
        }
    }


}
