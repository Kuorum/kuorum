package kuorum

import kuorum.post.Post

class PostTagLib {

    static namespace = "kPost"

    static defaultEncodeAs = 'html'
    static encodeAsForTags = [postLink: 'raw']

    def postLink={attrs, body ->
        Post post = attrs.post
//        out << g.link(mapping:"PURPOSEShow", params: [postId:post.id], body)
//        out << g.link(mapping:"${post.postType}Show", params: [postId:post.id], body)
        out << g.link(mapping:"postShow", params: [postId:post.id, postType:post.postType.urlText, hashtag:post.law.hashtag.decodeHashtag()], body)
    }
}
