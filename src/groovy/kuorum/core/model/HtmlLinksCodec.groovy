package kuorum.core.model

import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 21/11/13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
class HtmlLinksCodec {
    static encode = {target->
        String[] extensions= ["com", "es", "gov", "net", "org", "edu"]
        StringBuilder stringBuilder = new StringBuilder("http://[^ <>]*")
        String separator = "|"
        extensions.each {
            stringBuilder.append(separator).append("[^ <>]*\\.").append(it)
            separator = "|"
        }
//        /http:\/\/[^ <>]*|[^ <>]*\.com|[^ <>]*\.es|[^ <>]*\.net|[^ <>]*\.gov/
        Pattern pattern = Pattern.compile(stringBuilder.toString(), Pattern.CASE_INSENSITIVE)
        target.replaceAll(pattern,
                { wrapInHtmlLink(it) })
    }

    static decode = {target->
        //TODO: PEnsar
        target
    }

    private static String wrapInHtmlLink(String link){
        if (!link.startsWith("http")){
             link = "http://${link}"
        }
          "<a href='${link}' target='_blank noopener noreferrer'>${link}</a>"
    }
}
