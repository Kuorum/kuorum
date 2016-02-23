/**
 * Handle the operations of the hashtag
 */
class TwitterCodec {
    static encode = {String target->
        String nickName=target
        if (target.startsWith("http"))
            nickName = target.split("/").last()
        else if (target.startsWith("@"))
            nickName = target.substring(1)

        "@${nickName.trim()}".toLowerCase()
    }

    static decode = {target->
        String nickName=target
        if (target.startsWith("@"))
            nickName = "${target.substring(1)}"
        else if (target.startsWith("http")){
            nickName = target.split("/").last()
        }
        "https://twitter.com/${nickName.trim()}".toLowerCase()
    }
}
