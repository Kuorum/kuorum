package kuorum.core.model.search

import grails.validation.Validateable

/**
 * Created by iduetxe on 18/02/14.
 *
 * Search options
 *
 * It is serializable because is going to be stored on user session (cookie)
 */
@Validateable
enum SearchType {
    ALL, CAUSE;

    static final SearchType safeParse(String rawType) {
        SearchType searchType = null;
        try {
            SearchType.valueOf(rawType)
        } catch (Exception) {
            searchType = null;
        }
        return searchType;
    }
}
