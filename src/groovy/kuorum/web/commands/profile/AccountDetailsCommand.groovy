package kuorum.web.commands.profile

import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.SpringSecurityUiService
import grails.validation.Validateable
import kuorum.RegionService
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.security.authentication.dao.SaltSource

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class AccountDetailsCommand {
    KuorumUser user;
    String alias;
    String email;
    AvailableLanguage language;
    String password;


    static constraints = {
        user nullable: false
        password nullable: false,  validator: {val, obj ->
            if (val && obj.user && !isPasswordValid(val, obj.user)){
                return "notValid"
            }
        }
        alias nullable: true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        email nullable: false, email:true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAlias(val)){
                return "unique"
            }
        }
        language nullable:false
    }

    private static Boolean isPasswordValid(String inputPassword, KuorumUser user){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder = (org.springframework.security.authentication.encoding.PasswordEncoder)appContext.passwordEncoder
        passwordEncoder.isPasswordValid(user.password, inputPassword, null)
    }
}
