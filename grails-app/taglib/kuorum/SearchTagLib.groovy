package kuorum

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Entities
import org.jsoup.safety.Cleaner
import org.jsoup.safety.Whitelist
import org.kuorum.rest.model.search.SearchKuorumElementRSDTO

class SearchTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    static namespace = "searchUtil"
    private static final Integer MAX_LENGTH_TEXT = 300

    def highlightedField = { attrs ->
        SearchKuorumElementRSDTO element = attrs.searchElement
        String field = attrs.field
        Integer maxLength = attrs.maxLength?Integer.parseInt(attrs.maxLength):MAX_LENGTH_TEXT

        String res = ""
        if (element.highlighting?."$field"){
            res = element.highlighting."$field"
        } else if (element."${field}") {
            res = element."${field}"
            if (res) {
                res = res.substring(0, Math.min(res.length(), maxLength))
            }
        }
        if (res && res.length() < element."${field}".length()) {
            res += " ..."
        }
        Document doc = Jsoup.parse(res);
        // Clean the document.
        doc = new Cleaner(Whitelist.relaxed()).clean(doc);
        // Adjust escape mode
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        // Get back the string of the body.
        res = doc.body().html();
        out << res
    }
}
