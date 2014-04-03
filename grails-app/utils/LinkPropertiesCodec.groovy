import grails.util.Holders
import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrLaw
import kuorum.core.model.solr.SolrPost
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.commons.ApplicationHolder

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
                params = lawLinkParams(target);
                break;
            case SolrPost:
                params = lawLinkParams(target)
                break;
            case Post:
                params = lawLinkParams(target.law)
                String postTypeName =   translate("${PostType.canonicalName}.${target.postType}")
                params+= [
                        postId:target.id,
                        postTypeUrl:postTypeName.encodeAsKuorumUrl(),
                ]
                break;
            case KuorumUser:
            case SolrKuorumUser:
                params = [id:target.id]
                break;
            default:
                params = [:]
        }
        params
    }

    static decode = {target->
        //TODO
    }


    private static def lawLinkParams(Law law){
        String commissionName = translate("${CommissionType.canonicalName}.${law.commissions.first()}")
        [
                hashtag: law.hashtag.decodeHashtag(),
                regionName:law.region.name.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl(),
        ]
    }

    private static def lawLinkParams(SolrLaw law){
        String commissionName = translate("${CommissionType.canonicalName}.${law.commissions.first()}")
        [
                hashtag: law.hashtag.decodeHashtag(),
                regionName:law.regionName.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl()
        ]
    }

    private static def lawLinkParams(SolrPost post){
        String commissionName = translate("${CommissionType.canonicalName}.${post.commissions.first()}")
        String postTypeName =   translate("${PostType.canonicalName}.${post.subType}")
        [
                hashtag: post.hashtag.decodeHashtag(),
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
}
