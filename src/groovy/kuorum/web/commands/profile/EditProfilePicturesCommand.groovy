package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.users.KuorumUser

/**
 * Created by toni on 6/4/17.
 */
@Validateable
class EditProfilePicturesCommand {

    public EditProfilePicturesCommand(){}
    public EditProfilePicturesCommand(KuorumUser user){
        this.photoId = user.avatar?.id?.toString()
        this.imageProfile = user.imageProfile?.id?.toString()
    }


    String photoId
    String imageProfile

    static constraints = {
        photoId nullable: true
        imageProfile nullable: true
    }

}
