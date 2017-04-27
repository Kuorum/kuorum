package kuorum.web.commands.payment.post

import grails.validation.Validateable
import kuorum.web.constants.WebConstants
import org.grails.databinding.BindingFormat

/**
 * Created by toni on 26/4/17.
 */

@Validateable
class PostContentCommand {

    String title
    String body

    String fileType
    String headerPictureId
    String videoPost

    @BindingFormat(WebConstants.WEB_FORMAT_DATE)
    Date publishOn

    static constraints = {
        title nullable: false
        body nullable: false
        fileType nullable: true
        headerPictureId nullable: true
        videoPost nullable: true
        publishOn nullable: true, validator: { val, obj ->
        }
    }
}
