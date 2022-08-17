package kuorum.web.commands.profile.funnel

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.util.cif.CalculaNif
import kuorum.web.binder.RegionBinder
import kuorum.web.commands.profile.AccountDetailsCommand
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing
import org.grails.databinding.SimpleMapDataBindingSource
import org.jsoup.Jsoup
import org.kuorum.rest.model.geolocation.RegionRSDTO

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class FunnelFillBasicDataCommand {

    FunnelFillBasicDataCommand() {}
    String name
    String email
    String phonePrefix
    String phone

    @BindUsing({ obj, source ->
        source['nid']?.toUpperCase().trim()
    })
    String nid;
    String bio;
    String bio2;
    String contactName;
    static constraints = {
        importFrom KuorumUser, include: ["alias"]
        name nullable: false, maxSize: 70
        // WILL BE IGNORED. IS ONLY FOR VIEW
        email nullable: true
        phonePrefix nullable: false
        phone nullable: false, matches: "^[0-9]{9}\$"
        nid nullable: false, matches: "^[A-Z][0-9]{8}", validator: { val, obj ->
            CalculaNif calculaNif = new CalculaNif(val)
            if (!calculaNif.isValid()) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"
            } else if (!calculaNif.isAsociacion()) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.notAsoc"
            }
        }
        bio nullable: false, maxSize: 500
        bio2 nullable: false, maxSize: 800
        contactName nullable: false, maxSize: 70
    }

    static Boolean isPasswordValid(KuorumUser user, String inputPassword) {
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        RegisterService registerService = (kuorum.register.RegisterService) appContext.registerService
        if (registerService.isPasswordSetByUser(user)) {
            org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder = (org.springframework.security.authentication.encoding.PasswordEncoder) appContext.passwordEncoder
            return inputPassword && passwordEncoder.isPasswordValid(user.password, inputPassword, null)
        } else {
            return true
        }
    }

    static String normalizeAlias(String alias) {
        String s = java.text.Normalizer.normalize(alias, java.text.Normalizer.Form.NFD)
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
        return s
    }

    // This method is a fake to simulate 2 fields in the funnel, but in DDBB will be saved only one field -> BIO
    void fillBioParts(String bio) {
        String bioTransformed = bio
                .replaceAll(/<h5>[^<]*<\/h5>/, "|")
                .replaceAll("<br/>", "")
        String[] bioParts = bioTransformed.split("\\|");
        switch (bioParts.size()) {
            case 0:
                this.bio = ""
                this.bio2 = ""
                break
            case 1:
                this.bio = bioParts[0]
                this.bio2 = ""
                break
            case 2:
                this.bio = bioParts[1]
                this.bio2 = ""
                break
            default:
                this.bio = bioParts[1]
                this.bio2 = bioParts[2]
        }
    }

    // This method builds the final bio recovering both bio fields
    String getCompleteBio(String title1, String title2) {
        return """
            <h5>${title1}</h5>
            <p>${this.bio}</p>
            <h5>${title2}</h5>
            <p>${this.bio2}</p>
        """

    }
}
