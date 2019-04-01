package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.register.RegisterService
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

    AccountDetailsCommand(){}

    AccountDetailsCommand(KuorumUser user){
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
    KuorumUser user
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion
    String name
    String surname
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        AccountDetailsCommand.normalizeAlias(source["alias"])
    })
    String alias
    String email
    String phonePrefix
    String phone
    AvailableLanguage language
    String password
    String timeZoneId

    static constraints = {
        importFrom KuorumUser, include:["alias"]
        name nullable:false, maxSize: 17
        surname nullable:true
        user nullable: false
        password validator: {val, obj ->
            if (obj.user && !isPasswordValid(obj.user, val)){
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
            if (val && obj.user && val != obj.user.email && KuorumUser.findByEmailAndDomain(val, CustomDomainResolver.domain)){
                return "unique"
            }
        }
        language nullable:false
        phonePrefix nullable:true
        phone nullable: true
        homeRegion nullable: true
        timeZoneId nullable: true
    }

    static Boolean isPasswordValid(KuorumUser user, String inputPassword){
        Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
        RegisterService registerService = ( kuorum.register.RegisterService)appContext.registerService
        if (registerService.isPasswordSetByUser(user)){
            org.springframework.security.authentication.encoding.PasswordEncoder passwordEncoder = (org.springframework.security.authentication.encoding.PasswordEncoder)appContext.passwordEncoder
            return inputPassword && passwordEncoder.isPasswordValid(user.password, inputPassword, null)
        }else{
            return true
        }
    }

    static String normalizeAlias(String alias){
        String s = java.text.Normalizer.normalize(alias, java.text.Normalizer.Form.NFD)
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
        return s
    }
}
