package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

@Validateable
class DomainConfigCommand {

    String name;
    LanguageRSDTO language;
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
        name nullable: false
        language nullable: false
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
