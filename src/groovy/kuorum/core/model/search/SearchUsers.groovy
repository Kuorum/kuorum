package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.core.model.UserType

/**
 * Created by iduetxe on 7/05/14.
 */
@Validateable
class SearchUsers extends Pagination implements Serializable{

    String iso3166_2

    UserType userType

    static constraints = {
        iso3166_2 nullable: true
        userType nullable:true
    }
}