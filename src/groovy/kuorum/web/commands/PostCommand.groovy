package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.model.PostType

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class PostCommand {
    String id
    String title
    String text
    String imageId
    PostType postType
    static constraints = {
        id nullable: true, blank: true //Para reusar este command en la edición
        title nullable: false, blank: false
        text nullable: false, blank: false
        postType nullable: false
    }
}
