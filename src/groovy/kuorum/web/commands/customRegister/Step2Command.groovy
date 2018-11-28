package kuorum.web.commands.customRegister

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.Gender
import kuorum.users.KuorumUser
import kuorum.web.binder.RegionBinder
import kuorum.web.commands.profile.AccountDetailsCommand
import org.grails.databinding.BindUsing

/**
 * Commands for the steps after the first login
 * This is for second step
 */
@Validateable
class Step2Command {

    Step2Command(){}

    Step2Command(KuorumUser user, String recommendedAlias){
        this.user = user
        this.alias = user.alias?:recommendedAlias
        this.language = user.language
        this.homeRegion = user.personalData.province
//        this.phonePrefix = user.personalData?.phonePrefix
//        this.phone = user.personalData?.telephone
//        this.name = user.name
//        this.surname = user.surname
//        this.userType=user.userType
    }

    KuorumUser user
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        AccountDetailsCommand.normalizeAlias(source["alias"])
    })
    String alias
//    String name
//    String surname
//    String password;
    Gender gender
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion
    AvailableLanguage language

//    UserType userType;
    static constraints = {
        importFrom AccountDetailsCommand, include:["alias", "language", "homeRegion", "user"]
        gender nullable:false
        homeRegion nullable: false
//        userType nullable:false;
    }
}
