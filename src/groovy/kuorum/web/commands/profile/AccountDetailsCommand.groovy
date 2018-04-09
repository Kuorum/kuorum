package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.web.binder.RegionBinder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class AccountDetailsCommand {

    public AccountDetailsCommand(){}
    public AccountDetailsCommand(KuorumUser user){
        this.user = user
        this.name = user.name
        this.surname = user.surname
        this.alias = user.alias
        this.email = user.email
        this.phone = user.personalData?.telephone?:''
        this.phonePrefix = user.personalData?.phonePrefix?:''
        this.language = user.language
        this.homeRegion = user.personalData?.province
        this.timeZoneId = user.timeZone?.getID()
    }
    KuorumUser user;
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion
    String name;
    String surname;
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        AccountDetailsCommand.normalizeAlias(source["alias"])
    })
    String alias;
    String email;
    String phonePrefix;
    String phone;
    AvailableLanguage language;
    String password;
    String timeZoneId

    static constraints = {
        importFrom KuorumUser, include:["alias"]
        name nullable:false, maxSize: 17
        surname nullable:true
        user nullable: false
        password nullable: false,  validator: {val, obj ->
            if (val && obj.user && !isPasswordValid(val, obj.user)){
                return "notValid"
            }
        }
        alias nullable: false, maxSize: 15, matches: KuorumUser.ALIAS_REGEX, validator: {val, obj ->
            if (val && obj.user && val != obj.user.alias && KuorumUser.findByAliasAndDomain(val.toLowerCase(), CustomDomainResolver.domain)){
                return "unique"
            }
            if (!val && obj.user && obj.user.enabled){
                return "nullable"
            }
        }
        email nullable: false, email:true, validator: {val, obj ->
            if (val && obj.user && val != obj.user.email && KuorumUser.findByEmail(val)){
                return "unique"
            }
        }
        language nullable:false
        phonePrefix nullable:true
        phone nullable: true
        homeRegion nullable: true
        timeZoneId nullable: true
    }

    private static Boolean isPasswordValid(String inputPassword, KuorumUser user){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder = (org.springframework.security.authentication.encoding.PasswordEncoder)appContext.passwordEncoder
        passwordEncoder.isPasswordValid(user.password, inputPassword, null)
    }

    public static String normalizeAlias(String alias){
        String s = java.text.Normalizer.normalize(alias, java.text.Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
