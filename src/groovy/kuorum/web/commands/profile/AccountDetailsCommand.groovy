package kuorum.web.commands.profile

import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.SpringSecurityUiService
import grails.validation.Validateable
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.web.binder.RegionBinder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing
import org.springframework.security.authentication.dao.SaltSource

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class AccountDetailsCommand {

    public AccountDetailsCommand(){}
    public AccountDetailsCommand(KuorumUser user){
        this.user = user
        this.name = user.name
        this.alias = user.alias
        this.email = user.email
        this.phone = user.personalData?.telephone?:''
        this.phonePrefix = user.personalData?.phonePrefix?:''
        this.language = user.language
        this.homeRegion = user.personalData?.province
    }
    KuorumUser user;
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion
    String name;
    String alias;
    String email;
    String phonePrefix;
    String phone;
    AvailableLanguage language;
    String password;


    static constraints = {
        importFrom KuorumUser, include:["alias"]
        name nullable:false, maxSize: 17
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
        phonePrefix nullable:true
        phone nullable: true
        homeRegion nullable: true
    }

    private static Boolean isPasswordValid(String inputPassword, KuorumUser user){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder = (org.springframework.security.authentication.encoding.PasswordEncoder)appContext.passwordEncoder
        passwordEncoder.isPasswordValid(user.password, inputPassword, null)
    }
}
