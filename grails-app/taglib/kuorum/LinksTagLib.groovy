package kuorum

import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Post

class LinksTagLib {

    static namespace = "kLink"

    static defaultEncodeAs = 'html'
    static encodeAsForTags = [postLink: 'raw']

    def postLink={attrs, body ->
        Post post = attrs.post
        String elementId = attrs.elementId
        String cssClass = attrs.class
        String mapping = attrs.mapping
        def params = attrs.params?:[]

        String postTypeName =   message("${PostType.canonicalName}.${post.postType}",null,"", new Locale("ES_es"))

        params += lawParamsLink(post.law)
        params += [
                postId:post.id,
                postTypeUrl:postTypeName.encodeAsKuorumUrl(),
                elementId:elementId,
                class:cssClass
        ]
        out << g.link(mapping:mapping, params: params, body)
    }

    def lawLink={attrs, body ->
        Law law = attrs.law
        String elementId = attrs.elementId
        String cssClass = attrs.class
        String mapping = attrs.mapping
        def params = attrs.params?:[]
        params += lawParamsLink(law)
        params += [
                elementId:elementId,
                class:cssClass
        ]
        out << g.link(mapping:mapping, params: params, body)
    }

    private def lawParamsLink(Law law){
        String commissionName = message("${CommissionType.canonicalName}.${law.commissions.first()}",null,"otros", new Locale("ES_es"))
        [
                hashtag: law.hashtag.decodeHashtag(),
                regionName:law.region.name.encodeAsKuorumUrl(),
                commision:commissionName.encodeAsKuorumUrl()
        ]
    }
}
