import java.util.regex.Pattern

/**
 * Created by iduetxe on 22/06/14.
 */
public class TargetBlankCodec {


    private static Pattern PATTERN;
    static{
        PATTERN = ~/href=["']([^"']*)["']( target='_blank')*( rel='.*')*/
    }

    static encode = {target->
        target.replaceAll(PATTERN,{ processUrlLink(it[1]) })
    }

    static decode = {target->
        //TODO
        target
    }

    private static String processUrlLink(String link){
        def httpLink = link
        if (!link.startsWith("http")){
            httpLink = "http://${link}"
        }
        "href='${httpLink}' target='_blank' rel='nofollow noopener noreferrer'"
    }
}
