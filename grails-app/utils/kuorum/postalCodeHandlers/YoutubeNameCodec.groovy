package kuorum.postalCodeHandlers
/**
 * Handle the operations of the hashtag
 */
class YoutubeNameCodec {

    private static final YOUTUBE_REGEX = ~/http[s]{0,1}:\/\/(w{3}\.){0,1}youtube\.com\/watch\?v=([a-zA-Z0-9_-]*)/
    private static final YOUTUBE_EMBEDDED_REGEX = ~/http[s]{0,1}:\/\/(w{3}\.){0,1}youtube\.com\/embed\/([a-zA-Z0-9_-]*)/
    private static final YOUTUBE_TINY_REGEX = ~/http[s]{0,1}:\/\/youtu\.be\/([a-zA-Z0-9_-]*)/


    private static final YOUTUBE_URL_PREFIX = "https://www.youtube.com/watch?v="

    static encode = {String target->
        if (target){
            return "$YOUTUBE_URL_PREFIX$target"
        }
        return "";
    }

    static decode = {target->
        String code = ""
        if (YOUTUBE_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_REGEX, '$2')
        if (YOUTUBE_EMBEDDED_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_EMBEDDED_REGEX, '$2')
        if (!code && YOUTUBE_TINY_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_TINY_REGEX, '$1')
        return code
    }
}
