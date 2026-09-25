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
    String address;
    String cinfaCode;
    static constraints = {
        String ALLOWED_LETTERS = "CDEFGHJKLMNPQRSUVW"
        importFrom KuorumUser, include: ["alias"]
        name nullable: false, maxSize: 70
        // WILL BE IGNORED. IS ONLY FOR VIEW
        email nullable: true
        phonePrefix nullable: false
        phone nullable: false, matches: "^[0-9]{9}\$"
        address nullable: true, maxSize: 255, validator: { val, obj ->
            boolean hasCollaborator = CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false
            if (hasCollaborator && !val?.trim()) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.address.blank"
            }
            return true
        }
        cinfaCode nullable: true, validator: { val, obj ->
            boolean hasCollaborator = CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false
            if (!hasCollaborator) {
                return true
            }
            if (!val?.trim()) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.blank"
            }
            if (!(val ==~ /^[0-9]{5,6}$/)) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.cinfaCode.matches.error"
            }
            return true
        }
        nid nullable: false, validator: { val, obj ->
            boolean hasCollaborator = CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false

            if (hasCollaborator) {
                // PHARMACY RULES: format-only check, no checksum and no entity-type restriction
                // (NIF of a natural person, foreign NIE, or company CIF all pass as long as the
                // shape matches what Cinfa's own systems check: 9 characters, 8 digits + 1 letter
                // anywhere in the string).
                int letterCount = val ? val.findAll(/[A-Za-z]/).size() : 0
                int digitCount = val ? val.findAll(/[0-9]/).size() : 0
                boolean isValidFormat = val?.length() == 9 && letterCount == 1 && digitCount == 8

                if (!isValidFormat) {
                    return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.pharmacy.notPharmacy"
                }

                return true
            }

            // ASSOCIATION RULES (in Spain, Associations = letter G): still require a real,
            // checksum-valid CIF belonging to an association.
            CalculaNif calculaNif = new CalculaNif(val)

            if (!calculaNif.isValid()) {
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.invalid"
            }

            if (!calculaNif.isAsociacion()) {
                // It's a valid CIF, but the letter doesn't correspond to an association
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.notAsoc"
            }

            if (!val.matches("^(?![0-9]{8}[A-Z]\$)(?:[${ALLOWED_LETTERS}][0-9]{7}[A-Z]|[${ALLOWED_LETTERS}][0-9]{8}\$)")){
                return "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.nid.matches.error"
            }

            return true
        }
        bio nullable: true, blank: true, maxCharsHtml: 500, validator: { val, obj ->
            boolean hasCollaborator = CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false
            if (!val || Jsoup.parse(val).text().trim() == '') {
                return hasCollaborator ? "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.bio.blank.withCollaborator"
                        : "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.bio.blank"
            }
            return true
        }

        bio2 nullable: true, blank: true, maxCharsHtml: 800, validator: { val, obj ->
            boolean hasCollaborator = CustomDomainResolver.domainRSDTO?.contestApplicationWithCollaborator ?: false
            if (!val || Jsoup.parse(val).text().trim() == '') {
                return hasCollaborator ? "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.bio2.blank.withCollaborator"
                        : "kuorum.web.commands.profile.funnel.FunnelFillBasicDataCommand.bio2.blank"
            }
            return true
        }
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
        String bioTransformed = bio?.replaceAll(/<h5>[^<]*<\/h5>/, "|")?.replaceAll("<br/>", "") ?: ""
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
