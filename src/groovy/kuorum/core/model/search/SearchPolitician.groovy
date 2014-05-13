package kuorum.core.model.search

import grails.validation.Validateable

/**
 * Created by iduetxe on 7/05/14.
 */
@Validateable
class SearchPolitician extends Pagination implements Serializable{

    String regionIso3166_2
    String institutionName

    static constraints = {
        regionIso3166_2 nullable: true
        institutionName nullable:false
    }
}