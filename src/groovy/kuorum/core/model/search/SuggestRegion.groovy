package kuorum.core.model.search

import grails.validation.Validateable

/**
 * Created by iduetxe on 9/12/15.
 */
@Validateable
class SuggestRegion {

    String word;
    static constraints = {
        word nullable:true
    }
}
