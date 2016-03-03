/**
 * Handle the operations of the hashtag
 */
class TwitterCodec {
    static encode = {String target->
        String nickName=target.replaceAll(/[^\w:\-_\/\d]/,"")
        if (nickName.startsWith("http"))
            nickName = nickName.split("/").last()

        "@${nickName.trim()}".toLowerCase()
    }

    static decode = {target->
        String nickName=target.replaceAll(/[^\w:\-_\/\d]/,"")

        if (nickName.startsWith("http")){
            nickName = target.split("/").last()
        }
        "https://twitter.com/${nickName.trim()}".toLowerCase()
    }
}
