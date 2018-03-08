package kuorum.core.model.solr
/**
 * this class storage the highlighted elements returned by solr.
 * If is called and there is no highlighted, then returns original element
 */
@Deprecated
public class SolrHighlighting{
    def storage = [:]
    SolrElement element
    def propertyMissing(String field, value) {
        storage[name] = value
    }
    def propertyMissing(String field) {
        if (storage.containsKey(field))
            storage[field]
//        else if(element.hasProperty(field))
//            element."$field"
        else
            null
    }
}