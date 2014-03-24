import grails.util.Holders
import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Post
import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * Created by iduetxe on 24/03/14.
 */
class LinkPropertiesCodec {


    static encode = {target->

        def params = [:]
        switch (target){
            case Law:
                Law law = (Law) target
                params = lawLinkParams(law);
                break;
            case Post:
                params = lawLinkParams(target.law)
                String postTypeName =   translate("${PostType.canonicalName}.${target.postType}")
                params+= [
                        postId:target.id,
                        postTypeUrl:postTypeName.encodeAsKuorumUrl(),
                ]
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
                commision:commissionName.encodeAsKuorumUrl()
        ]
    }

    private static String translate(String txt){
        def messageSource =Holders.getApplicationContext().getBean("messageSource")
        messageSource.getMessage(txt,null,"default", new Locale("ES_es"))
    }
}
