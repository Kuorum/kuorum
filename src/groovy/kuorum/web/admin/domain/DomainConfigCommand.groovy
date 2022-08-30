package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

@Validateable
class DomainConfigCommand {

    String name;
    LanguageRSDTO language;
    String mainColor;
    String mainColorShadowed;
    String secondaryColor;
    String secondaryColorShadowed;
    KuorumWebFont titleWebFont;
    String facebook;
    String twitter;
    String linkedIn;
    String instagram;
    String youtube;

    Boolean providerBasicEmailForm
    Boolean providerGoogle
    Boolean providerFacebook
    Boolean providerAoc

    private static final String HEX_PATTERN = '^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$';
    static constraints = {
        name nullable: false
        language nullable: false
        mainColor nullable: false, matches: HEX_PATTERN
        mainColorShadowed nullable: false, matches: HEX_PATTERN
        secondaryColor nullable: false, matches: HEX_PATTERN
        secondaryColorShadowed nullable: false, matches: HEX_PATTERN
        titleWebFont nullable: false
        facebook nullable: true
        twitter nullable: true
        linkedIn nullable: true
        instagram nullable: true
        youtube nullable: true
        providerBasicEmailForm nullable: true
        providerGoogle nullable: true
        providerFacebook nullable: true
        providerAoc nullable: true
    }
}
