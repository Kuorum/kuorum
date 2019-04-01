package kuorum.web.commands.domain

import grails.validation.Validateable
import kuorum.core.customDomain.CustomDomainResolver

@Validateable
class DeleteDomainCommand {

    String domainName

        static constraints = {
            domainName nullable: false, validator : {val, obj ->
                String domain = CustomDomainResolver.domain
                if (domain != val){
                    return "notValid"
                }
            }
        }
}
