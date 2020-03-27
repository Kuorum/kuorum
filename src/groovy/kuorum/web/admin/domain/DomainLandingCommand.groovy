package kuorum.web.admin.domain

import grails.validation.Validateable
import kuorum.web.commands.LinkCommand
import org.kuorum.rest.model.kuorumUser.UserRoleRSDTO

@Validateable
class DomainLandingCommand {

    String slogan;
    String subtitle;
    String domainDescription;

    List<LinkCommand> footerLinks

    List<UserRoleRSDTO> landingVisibleRoles


    static constraints = {
        slogan nullable:false
        subtitle nullable:false
        domainDescription nullable:true
        footerLinks minSize: 0, maxSize: 10
        landingVisibleRoles nullable: true, maxSize: 4
    }
}
