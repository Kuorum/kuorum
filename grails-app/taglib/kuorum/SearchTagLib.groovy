package kuorum

import kuorum.core.model.solr.SolrElement

class SearchTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    static namespace = "searchUtil"
    private static final Integer MAX_LENGTH_TEXT = 300

    @Deprecated
    def highlightedField={attrs ->
        SolrElement element = attrs.solrElement
        String field = attrs.field
        Integer maxLength = attrs.maxLength?:MAX_LENGTH_TEXT

        String res = ""
        if (element.highlighting."$field"){
            res = element.highlighting."$field"
        }else if (element."${field}"){
            res = element."${field}"
            if (res){
                res = res.substring(0, Math.min(res.length(), maxLength))
            }
        }
        if (res && res.length() < element."${field}".length()){
            res += " ..."
        }
        out << res
    }
}
