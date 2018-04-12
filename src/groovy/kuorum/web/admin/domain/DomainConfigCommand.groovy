package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class DomainConfigCommand {

    String slogan;
    String subtitle;
    String mainColor;
    String mainColorShadowed;
    String secondaryColor;
    String secondaryColorShadowed;
    String facebook;
    String twitter;
    String linkedIn;
    String googlePlus;
    String instagram;
    String youtube;


    static constraints = {
        slogan nullable:false
        subtitle nullable:false
        mainColor nullable:false
        mainColorShadowed nullable: false
        secondaryColor nullable: false
        secondaryColorShadowed nullable: false

        facebook nullable:true
        twitter nullable:true
        linkedIn nullable:true
        googlePlus nullable:true
        instagram nullable:true
        youtube nullable:true
    }
}
