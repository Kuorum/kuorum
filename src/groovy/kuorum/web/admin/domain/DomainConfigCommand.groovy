package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

@Validateable
class DomainConfigCommand {

    String name;
    LanguageRSDTO language;
    Boolean validation
    String mainColor;
    String mainColorShadowed;
    String secondaryColor;
    String secondaryColorShadowed;
    String facebook;
    String twitter;
    String linkedIn;
    String instagram;
    String youtube;

    private static final String HEX_PATTERN = '^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$';
    static constraints = {
        name nullable: false
        language nullable: false
        validation nullable: true
        mainColor nullable:false, matches:HEX_PATTERN
        mainColorShadowed nullable: false, matches:HEX_PATTERN
        secondaryColor nullable: false, matches:HEX_PATTERN
        secondaryColorShadowed nullable: false, matches:HEX_PATTERN

        facebook nullable:true
        twitter nullable:true
        linkedIn nullable:true
        instagram nullable:true
        youtube nullable:true
    }
}
