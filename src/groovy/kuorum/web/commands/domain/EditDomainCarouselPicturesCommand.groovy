package kuorum.web.commands.domain

import grails.validation.Validateable

/**
 * Created by guille on 10/5/18.
 **/

@Validateable
class EditDomainCarouselPicturesCommand {
//    DomainRSDTO domain
    String slideId1
    String slideId2
    String slideId3
    String carouselFooter1
    String carouselFooter2
    String carouselFooter3

    static constraints = {
        slideId1 nullable: false
        slideId2 nullable: false
        slideId3 nullable: false
        carouselFooter1 nullable: true
        carouselFooter2 nullable: true
        carouselFooter3 nullable: true
    }

}
