package kuorum.web.commands.domain

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.web.commands.profile.AccountDetailsCommand

@Validateable
class DeleteDomainCommand {

    String domainName
    String password

        static constraints = {
            domainName nullable: false, validator : {val, obj ->
                String domain = CustomDomainResolver.domain
                if (domain != val){
                    return "notValid"
                }
            }
            password nullable: false, validator: {val, obj ->
                if (!AccountDetailsCommand.isPasswordValid(val)){
                    return "notValid"
                }
            }
        }
}
