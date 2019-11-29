package kuorum.postalCodeHandlers
/**
 * Handle the operations of the hashtag
 */
class YoutubeNameCodec {

    private static final YOUTUBE_REGEX = ~/http[s]{0,1}:\/\/(w{3}\.){0,1}youtube\.com\/watch\?.*v=([a-zA-Z0-9_-]*)&*.*/
    private static final YOUTUBE_EMBEDDED_REGEX = ~/http[s]{0,1}:\/\/(w{3}\.){0,1}youtube\.com\/embed\/([a-zA-Z0-9_-]*)&*.*/
    private static final YOUTUBE_TINY_REGEX = ~/http[s]{0,1}:\/\/youtu\.be\/([a-zA-Z0-9_-]*)[&?]*.*/


    private static final YOUTUBE_URL_PREFIX = "https://www.youtube.com/watch?v="

    static encode = {String target->
        String youtubeId = this.decode(target);
        youtubeId = youtubeId == ""?target:youtubeId

        if (youtubeId){
            return "$YOUTUBE_URL_PREFIX$youtubeId"
        }
        return ""
    }

    static decode = {target->
        String code = ""
        if (YOUTUBE_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_REGEX, '$2')
        if (YOUTUBE_EMBEDDED_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_EMBEDDED_REGEX, '$2')
        if (!code && YOUTUBE_TINY_REGEX.matcher(target).matches())
            code = target.replaceAll(YOUTUBE_TINY_REGEX, '$1')
        if (!code){
            log.warn("Decoding ID of a youtube video not found :: ${target}")
        }
        return code
    }
}
