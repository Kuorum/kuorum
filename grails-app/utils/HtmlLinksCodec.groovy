
import java.util.regex.Pattern

/**
 * Created with IntelliJ IDEA.
 * User: iduetxe
 * Date: 21/11/13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
class HtmlLinksCodec {

    private static final String[] EXTENSIONS =["ad","ae","af","al","am","an","ao","aq","ar","at","au","az","ba","bb","bd","be","bf","bg","bh","bi","bj","bm","bn","bo","br","bs","bt","bv","bw","by","bz","ca","cc","cf","cg","ch","ci","ck","cl","cm","cn","co","cr","cu","cy","cz","da","de","dk","dm","do","dz","ec","ee","eg","eh","es","et","fi","fj","fk","fm","fr","fx","ga","gb","gf","gh","gi","gl","gm","gn","go","gp","gr","gs","gt","gu","gw","gy","hk","hl","hn","hr","hu","id","ie","il","in","io","iq","ir","ir","is","it","jm","jo","jp","que","kg","ki","kp","kr","kw","ky","kz","la","lb","lc","li","lk","ls","lt","ly","ma","mc","md","mg","ml","mn","mo","mp","mr","ms","mt","mu","mv","mw","mx","my","mz","na","na","ne","ng","ni","nl","no","np","nr","nt","nz","om","pa","pe","pf","ph","pk","pl","pn","pr","pt","pw","py","qa","re","ro","ru","rw","sa","sb","sc","sd","se","sg","sh","si","si","sk","sm","sn","so","sr","ss","su","sv","sy","td","tf","tg","th","tj","tk","tm","tn","to","tr","tt","tv","tw","tz","ua","ug","uk","us","uy","uz","va","vc","ve","vg","vi","vn","vu","ws","ye","yu","za","zm","zr","zw","com","edu","gov","info","org","cat","name","museum","biz","aero","int","mil","net","eu","mobi"]
            .sort{it.length()*-1}// -1 => sorts EXTENSIONS in reverse order

    private static Pattern PATTERN;
    static{
        StringBuilder stringBuilder = new StringBuilder("http[s]{0,1}://[^ <>]*")
        String separator = "|"
        EXTENSIONS.each {
            stringBuilder.append(separator).append("[^ <>\n\r]+\\.").append(it).append("[^ \n\r]*")
            separator = "|"
        }
//        /http:\/\/[^ <>]*|[^ <>]*\.com|[^ <>]*\.es|[^ <>]*\.net|[^ <>]*\.gov/
        String patternRaw = stringBuilder.toString();
        PATTERN = Pattern.compile(patternRaw, Pattern.CASE_INSENSITIVE)
    }
    static encode = {target->

        target.replaceAll(PATTERN,{ wrapInHtmlLink(it) })
    }

    static decode = {target->
        //TODO: PEnsar
        target
    }

    private static String wrapInHtmlLink(String link){
        def httpLink = link
        if (!link.startsWith("http")){
            httpLink = "http://${link}"
        }
          "<a href='${httpLink}' target='_blank' rel='nofollow noopener noreferrer'>${link}</a>"
    }
}
