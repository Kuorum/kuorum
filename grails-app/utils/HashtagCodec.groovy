/**
 * Handle the operations of the hashtag
 */
class HashtagCodec {
    static encode = {String target->
        if (target.startsWith("#"))
            target
        else
            "#$target"
    }

    static decode = {target->
        if (target.startsWith("#"))
            target[1..-1]
        else
            target
    }
}
