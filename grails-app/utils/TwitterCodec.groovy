/**
 * Handle the operations of the hashtag
 */
class TwitterCodec {
    static encode = {String target->
        String nickName=getNormalizedNickName(target)

        nickName?"@${nickName.trim()}":""
    }

    static decode = {target->
        String nickName=getNormalizedNickName(target)
        nickName?"https://twitter.com/${nickName.trim()}":""
    }

    static String getNormalizedNickName(def target){
        //Remove url params
        String nickName = target.replaceAll(/\?.*$/,"")
        //Remove special chars
        nickName = nickName.replaceAll(/[^\w:\-_\/\d]/,"")

        if (nickName.startsWith("http")){
            nickName = nickName.split("/").last()
        }
        return nickName.toLowerCase();
    }
}
