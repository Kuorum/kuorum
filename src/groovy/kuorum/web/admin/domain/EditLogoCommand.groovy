package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.domain.DomainRDTO


@Validateable
class EditLogoCommand {
//    public EditLogoCommand(){}
    public EditLogoCommand(DomainRDTO domainRDTO){
        this.photoId = domainRDTO.avatar?.id?.toString()
        this.imageLogo = domainRDTO.image?.id?.toString()
    }


    String photoId
    String imageLogo

    static constraints = {
        photoId nullable: true
        imageLogo nullable: true
    }

}
