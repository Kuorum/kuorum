package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class DomainConfigStep1Command {

    String slogan
    String subtitle
    String logoName
    String colorHexCode
    String slideId1
    String slideId2
    String slideId3
    static constraints = {
        slogan nullable:false
        subtitle nullable:false
        logoName nullable: true // Handle on the command checking the uplaoded logo
        colorHexCode nullable:false
        slideId1 nullable:false
        slideId2 nullable:false
        slideId3 nullable:false
    }
}
