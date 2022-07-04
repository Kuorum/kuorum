package kuorum.web.commands.profile.funnel

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.web.binder.RegionBinder
import kuorum.web.commands.profile.AccountDetailsCommand
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing
import org.grails.databinding.SimpleMapDataBindingSource
import org.kuorum.rest.model.geolocation.RegionRSDTO

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class FunnelFillBasicDataCommand {

    FunnelFillBasicDataCommand() {}

    FunnelFillBasicDataCommand(KuorumUser user) {
        this.name = user.name
        this.email = user.email
        this.phone = user.personalData?.telephone ?: ''
        this.phonePrefix = user.personalData?.phonePrefix ?: ''
        this.bio = user.bio

    }

    String name
    String email
    String phonePrefix
    String phone

    String nid;
    String bio;
    static constraints = {
        importFrom KuorumUser, include: ["alias"]
        name nullable: false
        // WILL BE IGNORED. IS ONLY FOR VIEW
        email nullable: true
        phonePrefix nullable: false
        phone nullable: false
        nid nullable: false
        bio nullable: false, maxSize: 500
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
}
