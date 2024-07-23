package kuorum
/**
 * Handle the operations of the hashtag
 */
class YoutubeNameCodec {

    // YouTube regex from https://regex101.com/r/OY96XI/1
    private static final YT_FULL_REGEX = ~/(?:(?:https?:)?\/\/)?(?:[0-9A-Z-]+\.)?(?:youtu\.be\/|youtube(?:-nocookie)?\.com\S*?[^\w\s-])([\w-]{11})(?=[^\w-]|$)(?![?=&+%\w.-]*(?:['\"][^<>]*>|<\/a>))[?=&+%\w.-]*/
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
        def matcher = YT_FULL_REGEX.matcher(target)
        if (matcher.find()) {
            code = matcher.group(1)
        }

        if (!code){
            log.warn("Decoding ID of a youtube video not found :: ${target}")
        }
        return code
    }

}
