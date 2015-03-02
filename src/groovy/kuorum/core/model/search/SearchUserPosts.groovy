package kuorum.core.model.search

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 21/05/14.
 */
@Validateable
class SearchUserPosts extends Pagination{
    KuorumUser user
    /**
     * If null => All
     * If true => Only published
     * If false => Only drafts
     */
    Boolean publishedPosts = null

    /**
     * If null => All
     * If true => Only victories
     * If false => Only non victories
     */
    Boolean victory = null

}
